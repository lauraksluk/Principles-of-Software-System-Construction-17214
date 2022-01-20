package edu.cmu.webgen.project;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The {@link ProjectBuilder} gets raw data/events from the {@link edu.cmu.webgen.parser.ProjectParser}
 * and constructs project's structure with classes in the  {@link edu.cmu.webgen.project} package.
 * <p>
 * The project builder gets raw events like entering and leaving a directory and finding
 * files.
 * <p>
 * The top-level directory corresponds to the project and it should not have any content
 * other than metadata and subdirectories. The subdirectories correspond to articles
 * and events, which may internally have other articles and events.
 * <p>
 * It uses a temporary data structure to collect the information and create the target
 * structure step by step. As different directories are explored, internal information
 * is stored in a stack with an entry to collect information about the current directory
 * and all parent directories.
 */
public class ProjectBuilder {

    private final Stack<DirectoryBuilder> dirStack = new Stack<>();
    private final HashMap<Object, Set<Topic>> topics = new HashMap<Object, Set<Topic>>();


    public ProjectBuilder(@NotNull String projectDirName, @NotNull LocalDateTime created,
                          @NotNull LocalDateTime lastUpdate) {
        this.dirStack.push(
                new DirectoryBuilder(projectDirName, created, lastUpdate, true, false));
    }

    @NotNull
    public Project buildProject() throws ProjectFormatException {
        assert this.dirStack.size() == 1;
        return this.dirStack.pop().buildProject(this.topics);
    }

    public void openDirectory(String directoryName, LocalDateTime folderCreated, LocalDateTime folderLastUpdate) {
        DirectoryBuilder builder = new DirectoryBuilder(
                directoryName, folderCreated, folderLastUpdate, false, dirStack.size() == 1);
        this.dirStack.push(builder);
    }

    public void finishDirectory() {
        assert !this.dirStack.isEmpty();
        DirectoryBuilder builder = this.dirStack.pop();
        assert !this.dirStack.isEmpty();
//        if (builder.isEvent(builder.metadata)) {
//            // if in the project directory, build an article, otherwise build a subarticle
//            if (dirStack.size() == 1)
//                dirStack.peek().addEvent(builder.buildEvent());
//            else
//                dirStack.peek().addSubEvent(builder.buildSubEvent());
//        } else {
        // if in the project directory, build an article, otherwise build a subarticle

        if (this.dirStack.size() == 1) {
            var article = builder.buildArticle();
            this.dirStack.peek().addArticle(article);
            this.topics.put(article, Topic.from(article.getMetadata()));
        } else if (this.dirStack.size() == 2) {
            var subArticle = builder.buildSubArticle();
            this.dirStack.peek().addSubArticle(subArticle);
            this.topics.put(subArticle, Topic.from(subArticle.getMetadata()));
        } else {
            var subSubArticle = builder.buildArticleLeaf();
            this.dirStack.peek().addSubSubArticle(subSubArticle);
            this.topics.put(subSubArticle, Topic.from(subSubArticle.getMetadata()));
        }

    }

    public void foundMetadata(Map<String, String> metadata) {
        assert !this.dirStack.isEmpty();
        this.dirStack.peek().addMetadata(new Metadata(metadata));
    }

    public void foundTextDocument(List<FormattedTextContent> text, Map<String, String> rawMetadata,
                                  LocalDateTime fileCreated, LocalDateTime fileLastUpdate, long fileSize) throws ProjectFormatException {
        assert !this.dirStack.isEmpty();
        Metadata metadata = new Metadata(rawMetadata);
        this.dirStack.peek().addMetadata(metadata);
        var doc = new FormattedTextDocument(text, metadata, fileCreated, fileLastUpdate, fileSize);
        this.dirStack.peek().addContent(doc);
        this.topics.put(doc, Topic.from(metadata));
    }

    public void foundYoutubeVideo(String youtubeId, Map<String, String> rawMetadata, LocalDateTime created,
                                  LocalDateTime lastUpdate, long size) throws ProjectFormatException {
        assert !this.dirStack.isEmpty();
        this.dirStack.peek().addContent(
                new YoutubeVideo(youtubeId, new Metadata(rawMetadata), created, lastUpdate)
        );
    }

    public void foundImage(File file, LocalDateTime fileCreated, LocalDateTime fileLastUpdate, long size) throws ProjectFormatException {
        assert !this.dirStack.isEmpty();
        this.dirStack.peek().addContent(
                new Image(file, fileCreated, fileLastUpdate, size)
        );
    }

    public void foundVideo(File file, LocalDateTime fileCreated, LocalDateTime fileLastUpdate, long size) throws ProjectFormatException {
        assert !this.dirStack.isEmpty();
        this.dirStack.peek().addContent(
                new Video(file, fileCreated, fileLastUpdate, size)
        );
    }

    private static class DirectoryBuilder {
        private final String directoryName;
        private final LocalDateTime created;
        private final LocalDateTime lastUpdate;
        private final boolean isTopLevelDirectory;
        private final boolean isProjectDirectory;
        private final List<AbstractContent> content = new ArrayList<>();
        private final List<ArticleComposite> innerArticles = new ArrayList<>();
        private final List<ArticleLeaf> innerSubSubArticles = new ArrayList<>();
        private final List<ArticleComponent> innerSubArticles = new ArrayList<>();
        private final List<Event> innerEvents = new ArrayList<>();
        private Metadata metadata = new Metadata();

        private DirectoryBuilder(@NotNull String directoryName, @NotNull LocalDateTime created, @NotNull LocalDateTime lastUpdate, boolean isProjectDirectory, boolean isTopLevelDirectory) {
            this.directoryName = directoryName;
            this.created = created;
            this.lastUpdate = lastUpdate;
            this.isProjectDirectory = isProjectDirectory;
            this.isTopLevelDirectory = isTopLevelDirectory;
        }

        private ArticleComposite buildArticle() {
            assert this.isTopLevelDirectory;
            var newArticle = new ArticleComposite(this.content, this.directoryName, this.created, this.lastUpdate);
            for (ArticleComponent subArticle : this.innerSubArticles) {
                newArticle.addSubArticle(subArticle);
            }
            newArticle.addMetadata(this.metadata);
            return newArticle;
        }

        private ArticleComponent buildSubArticle() {
            assert !this.isTopLevelDirectory;
            var newSubArticle = new ArticleComposite(this.content, this.directoryName, this.created, this.lastUpdate);
            for (ArticleComponent subArticle : this.innerSubSubArticles) {
                newSubArticle.addSubArticle(subArticle);
            }
            newSubArticle.addMetadata(this.metadata);
            return newSubArticle;
        }

        private ArticleLeaf buildArticleLeaf() {
            assert !this.isTopLevelDirectory;
            if (this.innerArticles.size() > 0)
                throw new ProjectFormatException("Not supporting sub-sub-sub-articles (technical limitation)");
            var n = new ArticleLeaf(this.content, this.directoryName, this.created, this.lastUpdate);
            n.addMetadata(this.metadata);
            return n;
        }


        private Project buildProject(HashMap<Object, Set<Topic>> topics) throws ProjectFormatException {
            assert this.isProjectDirectory;
            if (!this.metadata.has("title"))
                throw new ProjectFormatException("Project has no title. Provide a .yml file with a \"title\" entry in the project directory.");
            if (!this.metadata.has("organization"))
                throw new ProjectFormatException("Project has no organization. Provide a .yml file with an \"organization\" entry in the project directory.");

            return new Project(this.metadata.get("title"), this.metadata.get("organization"), this.innerArticles,
                    this.innerEvents, topics);
        }


        private void addArticle(ArticleComposite article) {
            this.innerArticles.add(article);
        }

//        public void addEvent(Event entry) {
//            innerEvents.add(entry);
//        }

        private void addSubArticle(ArticleComponent subArticle) {
            this.innerSubArticles.add(subArticle);
        }

        private void addSubSubArticle(ArticleLeaf subSubArticle) {
            this.innerSubSubArticles.add(subSubArticle);
        }

        private void addMetadata(Metadata thatMetadata) {
            this.metadata = this.metadata.concat(thatMetadata);
        }

        private void addContent(AbstractContent node) throws ProjectFormatException {
            if (this.isProjectDirectory)
                throw new ProjectFormatException("Project may not contain content other than articles and events, found " + node);
            this.content.add(node);
        }
    }
}
