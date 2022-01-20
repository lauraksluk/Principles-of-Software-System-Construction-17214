package edu.cmu.webgen.project;

import edu.cmu.webgen.WebGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

public class ArticleComposite implements ArticleComponent, Comparable<ArticleComposite> {
    protected @NotNull final String directoryName;
    protected @NotNull final Set<Topic> topics = new HashSet<>();
    private final LocalDateTime lastUpdate;
    private final LocalDateTime created;
    protected @Nullable String id = null;
    protected @NotNull Metadata metadata = new Metadata();
    private final List<ArticleComponent> children;
    protected @NotNull final List<AbstractContent> content;

    public ArticleComposite(List<AbstractContent> content, @NotNull String directoryName,
                            @NotNull LocalDateTime created, @NotNull LocalDateTime lastUpdate) {
        this.content = content;
        this.directoryName = directoryName;
        this.created = created;
        this.lastUpdate = lastUpdate;
        children = new ArrayList<ArticleComponent>();
    }

    @Override
    public int compareTo(@NotNull ArticleComposite that) {
        return this.getTitle().compareTo(that.getTitle());
    }

    @Override
    public String getId() {
        if (this.id == null)
            this.id = WebGen.genId(getTitle());
        return this.id;
    }

    @Override
    public LocalDateTime getLastUpdate() {
        Optional<LocalDateTime> childrenLastUpdate = children
                .stream().map(ArticleComponent::getLastUpdate).max(LocalDateTime::compareTo);
        LocalDateTime last = this.lastUpdate;
        if (childrenLastUpdate.isPresent() && childrenLastUpdate.get().compareTo(last) > 0) {
            last = childrenLastUpdate.get();
        }
        return last;
    }

    @Override
    public LocalDateTime getCreated() {
        Optional<LocalDateTime> childrenCreated = children
                .stream().map(ArticleComponent::getCreated).max(LocalDateTime::compareTo);
        LocalDateTime last = this.created;
        if (childrenCreated.isPresent() && childrenCreated.get().compareTo(last) > 0) {
            last = childrenCreated.get();
        }
        return last;
    }

    @Override
    public LocalDateTime getPublishedDate() {
        if (this.metadata.has("date")) {
            try {
                return WebGen.parseDate(this.metadata.get("date"));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        }
        return getLastUpdate();
    }

    @Override
    public String getTitle() {
        if (this.metadata.has("title"))
            return this.metadata.get("title");
        for (AbstractContent n : this.content)
            if (n.hasTitle())
                return Objects.requireNonNull(n.getTitle());
        return this.directoryName;
    }

    @Override
    public void addContent(AbstractContent newContent) {
        this.content.add(newContent);
    }

    @Override
    public List<AbstractContent> getContent() {
        return this.content;
    }

    @Override
    public void addMetadata(Metadata m) {
        this.metadata = this.metadata.concat(m);
        this.topics.addAll(Topic.from(m));
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

    public void addSubArticle(ArticleComponent a) {
        children.add(a);
    }

    public void removeSubArticle(ArticleComponent a) {
        children.remove(a);
    }

    public List<ArticleComponent> getChildren() {
        return children;
    }

    public Set<Topic> getTopics(HashMap<Object, Set<Topic>> topics) {
        Set<Topic> result = new HashSet<>();
        for (AbstractContent ac : getContent()) {
            result.addAll(topics.getOrDefault(ac, new HashSet<>()));
        }
        for (ArticleComponent child : getChildren()) {
            result.addAll(child.getTopics(topics));
        }
        return result;
    }

    public List<AbstractContent> getAllContent() {
        List<AbstractContent> allContent = new ArrayList<>();
        allContent.addAll(getContent());
        for (ArticleComponent child : children) {
            allContent.addAll(child.getAllContent());
        }
        return allContent;
    }

    public HashMap<Object, String> getAllPaths() {
        HashMap<Object, String> result = new HashMap<>();
        String path1 = getId() + "/";
        result.put(this, path1);
        for (ArticleComponent child : children) {
            String path2 = child.getId() + "/";
            result.put(child, path1 + path2);
            for (ArticleComponent c : child.getChildren()) {
                String path3 = c.getId() + "/";
                result.put(c, path1 + path2 + path3);
            }
        }
        return result;
    }

    public HashMap<Object, List<Object>> getAllTraversals() {
        HashMap<Object, List<Object>> traversals = new HashMap<>();
        List<Object> l1 = new ArrayList<>();
        traversals.put(this, l1);
        for (ArticleComponent child : children) {
            List<Object> l2 = new ArrayList<>();
            l2.add(this);
            traversals.put(child, l2);
            for (ArticleComponent c : child.getChildren()) {
                List<Object> l3 = new ArrayList<>();
                l3.addAll(l2);
                l3.add(child);
                traversals.put(c, l3);
            }
        }
        return traversals;
    }

    @Override
    public List<Object> getAllArticles() {
        List<Object> result = new ArrayList<>();
        result.add(this);
        for (ArticleComponent child : children) {
            result.addAll(child.getAllArticles());
        }
        return result;
    }

}
