package edu.cmu.webgen.rendering;

import edu.cmu.webgen.WebGen;
import edu.cmu.webgen.WebGenArgs;
import edu.cmu.webgen.project.*;
import edu.cmu.webgen.rendering.data.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class uses the internal data structures and creates pages to be rendered.
 * <p>
 * This class performs all the decision on what pages to create, what is
 * contained on them, and how they link to each other.
 * <p>
 * It creates objects from the `Website` class and passes them to the template engine
 * for the actual rendering.
 */
public class Renderer {
    public static final String EVENTS_ADDRESS = "/events/";
    public static final String TOPICS_ADDRESS = "/topics/";
    public static final String ARTICLES_ADDRESS = "/articles/";
    public static final String ENTRY_ADDRESS = "/p/";
    public static final String HOME_ADDRESS = "/";
    private final SiteLink HOME_LINK = new SiteLink(createURL(HOME_ADDRESS), "Home");
    private final SiteLink ARTICLES_LINK = new SiteLink(createURL(ARTICLES_ADDRESS), "Articles");
    private final SiteLink EVENTS_LINK = new SiteLink(createURL(EVENTS_ADDRESS), "Events");
    private final SiteLink TOPICS_LINK = new SiteLink(createURL(TOPICS_ADDRESS), "Topics");

    public final File targetDirectory;
    public final TemplateEngine templateEngine;
    public final String siteGenerationTime;
    public final WebGenArgs.ArticleSorting sorting;
    public List<SiteLink> headers = null;

    public Renderer(File targetDirectory, WebGenArgs.ArticleSorting sorting, TemplateEngine templateEngine) {
        this.targetDirectory = targetDirectory;
        this.templateEngine = templateEngine;
        this.sorting = sorting;
        this.siteGenerationTime = WebGen.readableFormat(LocalDateTime.now());
    }

    /**
     * create a Pagination object with links to the different pages of a result
     *
     * @param selectedPageIdx index of the selected page
     * @param pageCount       number of pages
     * @param genLink         function to generate the link to a specific page number
     * @return returns a Pagination object that can be rendered
     */
    private static Pagination createPagination(int selectedPageIdx, int pageCount, Function<Integer, SiteURL> genLink) {
        if (pageCount == 1)
            return new Pagination(Collections.emptyList());

        assert selectedPageIdx < pageCount;
        assert pageCount > 0;
        List<List<SiteLink>> links = new ArrayList<>();
        int segmentStart = 0;
        int segmentEnd = pageCount - 1;
        if (pageCount > 10) {
            if (selectedPageIdx < 5) segmentEnd = 8;
            else if (selectedPageIdx > pageCount - 6) {
                segmentStart = pageCount - 9;
            } else {
                segmentStart = selectedPageIdx - 3;
                segmentEnd = selectedPageIdx + 3;
            }
        }
        if (segmentStart != 0) {
            links.add(List.of(new SiteLink(genLink.apply(0), "1", false)));
        }
        List<SiteLink> segmentLinks = new ArrayList<>(segmentEnd - segmentStart);
        for (int idx = segmentStart; idx <= segmentEnd; idx++) {
            segmentLinks.add(new SiteLink(genLink.apply(idx), "" + (idx + 1), selectedPageIdx == idx));
        }
        links.add(segmentLinks);
        if (segmentEnd != pageCount - 1) {
            links.add(List.of(new SiteLink(genLink.apply(pageCount - 1), "" + pageCount,
                    selectedPageIdx == pageCount - 1)));
        }

        return new Pagination(links);
    }

    public String createPaginatedPath(String basePath, int page) {
        assert basePath.startsWith("/");
        if (page == 0) return basePath;
        return basePath + (page + 1) + "/";
    }

    /**
     * returns relative path back to / from the current path
     *
     * @param currentPath path from where the relative path to root is computed
     * @return this is "." if we are in the root directory, ".." if we are a level above, "../.." above, etc
     */
    public String getRelPath(String currentPath) {
        assert currentPath.startsWith("/");
        int nestingLevel = (int) currentPath.chars().filter((c) -> c == '/').count();
        assert nestingLevel > 0;
        if (nestingLevel == 1) return ".";
        String path = "../".repeat(nestingLevel - 1);
        return path.substring(0, path.length() - 1);
    }

    /**
     * create link to targetPath from the currentPath
     *
     * @param targetPath target path (starts and ends with a "/")
     * @return the SiteURL object for this path
     */
    public SiteURL createURL(String targetPath) {
        assert targetPath.startsWith("/");
        assert targetPath.endsWith("/");
        return new SiteURL(targetPath + "index.html");
    }

    /**
     * create all the files for this project
     */
    public void renderProject(Project project) throws IOException {
        // render main page
        renderHomepage(project);

        //render each entry
        renderArticles(project);
//        renderEvents(project);

        //lists
        renderArticleList(project);
//        renderEventList(project);
        renderTopicList(project);

        //each topic has a page
        renderTopics(project);

        //basic static elements
        copyCSS();
    }

    public void renderHomepage(Project project) throws IOException {
        String relPath = getRelPath(HOME_ADDRESS);
        List<ArticlePreview> articles = project.getArticles().stream().sorted((o1, o2) -> {
                    if (this.sorting == WebGenArgs.ArticleSorting.PINNED) {
                        if (project.isArticlePinned(o1) && !project.isArticlePinned(o2)) return -1;
                        if (!project.isArticlePinned(o1) && project.isArticlePinned(o2)) return 1;
                    }
                    if (this.sorting == WebGenArgs.ArticleSorting.PUBLISHED_FIRST)
                        if (!o1.getPublishedDate().equals(o2.getPublishedDate()))
                            return 0 - o1.getPublishedDate().compareTo(o2.getPublishedDate());
                    if (this.sorting == WebGenArgs.ArticleSorting.PUBLISHED_LAST)
                        if (!o1.getPublishedDate().equals(o2.getPublishedDate()))
                            return o1.getPublishedDate().compareTo(o2.getPublishedDate());
                    if (this.sorting == WebGenArgs.ArticleSorting.EDITED)
                        if (!o1.getLastUpdate().equals(o2.getLastUpdate()))
                            return o1.getLastUpdate().compareTo(o2.getLastUpdate());
                    return o1.getTitle().compareTo(o2.getTitle());
                }).limit(5).
                map(a -> renderArticlePreview(a, relPath, "", project)).collect(Collectors.toList());
//        List<Website.EventListing> upcomingEvents = genEventListing(project.getUpcomingEvents(5));
        List<EventListing> upcomingEvents = Collections.emptyList(); // not yet implemented
        SiteData siteData = genSiteData(project, relPath);
        Homepage homepage = new Homepage(
                siteData,
                articles,
                upcomingEvents,
                ARTICLES_LINK.getAddress(),
                EVENTS_LINK.getAddress());
        File targetFile = new File(this.targetDirectory, "index.html");
        this.templateEngine.render(homepage.getTemplate(), homepage, targetFile);
    }


    public List<EventListing> genEventListing(List<Event> events) {
        throw new UnsupportedOperationException("Not yet implemented.");
//        List<Website.EventListing> result = new ArrayList<>();
//        for (Event e : events) {
//            List<Event> subEvents = getChildEvents(e);
//            List<Website.EventListing> subEventListing = genSubEventListing(subEvents);
//
//            Website.EventListing eventListing = new Website.EventListing(getEventURL(e), e.getTitle(), WebGen.readableFormat(e.getStartDate()), subEventListing);
//            result.add(eventListing);
//        }
//        return result;
    }

    public void copyCSS() throws IOException {
        File cssDir = new File(targetDirectory, "css");
        cssDir.mkdir();
        InputStream source = this.getClass().getResourceAsStream("/css/main.css");
        OutputStream out = new FileOutputStream(new File(cssDir, "main.css"));
        IOUtils.copy(source, out);
    }

    public void renderArticles(Project project) throws IOException {
        for (ArticleComponent article : project.getArticles()) {
            renderArticle(project, article);
            for (ArticleComponent subArticle : article.getChildren()) {
                renderArticle(project, subArticle);
                for (ArticleComponent subSubArticle : subArticle.getChildren()) {
                    renderArticle(project, subSubArticle);
                }
            }
        }
    }

    public void renderArticle(Project project, ArticleComponent article) throws IOException {
        String pagePath = getArticlePath(article, project);
        String relPath = getRelPath(pagePath);
        SiteData siteData = genSiteData(project, relPath);
        List<SiteLink> topics = project.getTopics(article)
                .stream().sorted().map(this::mkTopicLink).collect(Collectors.toList());
        List<SiteLink> breadcrumbs = getBreadcrumbs(article, project);

        ArticlePage page = new ArticlePage(
                siteData,
                article.getTitle(),
                breadcrumbs,
                WebGen.readableFormat(article.getPublishedDate()),
                topics,
                getArticleContent(article, relPath, project));
        File targetFile = new File(new File(this.targetDirectory, pagePath), "index.html");
        this.templateEngine.render(page.getTemplate(), page, targetFile);
    }

    /**
     * links for breadcrumb navigation for Entries
     *
     * @param a target entry
     * @param proj the current Project
     * @return list of links to this and its parent entries
     */
    public List<SiteLink> getBreadcrumbs(ArticleComponent a, Project proj) {
        List<SiteLink> result = new ArrayList<>();
        HashMap<Object, List<Object>> allParents = new HashMap<>();
        for (ArticleComposite ac : proj.getArticles()) {
            allParents.putAll(ac.getAllTraversals());
        }
        List<Object> p = allParents.get(a);
        while (!p.isEmpty()) {
            ArticleComponent parent = (ArticleComponent) p.remove(0);
            result.add(new SiteLink(getArticleURL(parent, proj), parent.getTitle(), true));
        }
        result.add(new SiteLink(getArticleURL(a, proj), a.getTitle(), true));
        return result;
    }

    public void renderEvent(Project project, Event event) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented.");
//        String pagePath = getEventPath(event);
//        String relPath = getRelPath(pagePath);
//        Website.SiteData siteData = genSiteData(project, relPath);
//        List<Website.SiteLink> topics = event.getTopics().stream().sorted().map(this::mkTopicLink).collect(Collectors.toList());
//        List<Website.SiteLink> breadcrumbs = Collections.emptyList(); //TODO getBreadcrumbs(event);
//
//        Website.EventPage page = new Website.EventPage(
//                siteData,
//                event.getTitle(),
//                breadcrumbs,
//                WebGen.readableFormat(event.getStartDate()),
//                WebGen.readableFormat(event.getEndDate()),
//                topics,
//                getEventContent(event, relPath)); // like getArticleContent?
//        File targetFile = new File(new File(targetDirectory, pagePath), "index.html");
//        templateEngine.render(page.getTemplate(), page, targetFile);
    }

    /**
     * basic site information used on every page, including title and owner
     *
     * @param project the project to render
     * @param relPath relative path to the current page
     * @return SiteData object
     */
    public SiteData genSiteData(Project project, String relPath) {
        return new SiteData(
                relPath,
                project.getTitle(),
                project.getOwnerOrg(),
                genHeaders(project),
                this.siteGenerationTime);
    }

    public ContentFragment getSubArticlesFragment(ArticleComponent sub, String relPath, Project proj) throws IOException {
        StringWriter w = new StringWriter();
        this.templateEngine.render("article-preview",
                renderArticlePreview(sub, relPath, "Read on: ", proj), w);
        return new ContentFragment(sub.getTitle(), w.toString());
    }


    /**
     * creates a ContentFragment object that contains HTML output for a node.
     * The HTML output for a node is created by invoking the rendering engine on a template
     *
     * @param storyNode node to render
     * @param relPath   relative path of the current page
     * @return a ContentFragment
     */
    public ContentFragment getStoryContentFragment(AbstractContent storyNode, String relPath) throws IOException {
        try {
            return storyNode.getContentFragment(templateEngine);
        } catch (Exception e) {
            throw new RuntimeException("unsupported story content " + storyNode);
        }
    }

    /**
     * collect all the content fragments of an entry
     */
    public List<ContentFragment> getArticleContent(ArticleComponent story, String relPath, Project proj) throws IOException {
        List<ContentFragment> result = new ArrayList<>();
        for (AbstractContent n : story.getContent()) {
            result.add(getStoryContentFragment(n, relPath));
        }
        for (ArticleComponent a : story.getChildren()) {
            result.add(getSubArticlesFragment(a, relPath, proj));
        }
        return result;
    }

    public void renderTopics(Project project) throws IOException {
        for (Topic topic : findAllTopics(project)) {
            renderTopic(project, topic);
        }
    }


    public List<Object> findAllArticles(Project project) {
        List<Object> result = new ArrayList<>();
        for (ArticleComposite a : project.getArticles()) {
            result.addAll(a.getAllArticles());
        }
        return result;
    }


    public void renderTopic(Project project, Topic topic) throws IOException {
        List<Object> allArticles = new WebGen().findArticlesByTopic(project, topic);
        List<List<Object>> articlePages = WebGen.paginateContent(allArticles.iterator(), 5);
        String basePath = getTopicPath(topic);
        for (int pageIdx = 0; pageIdx < articlePages.size(); pageIdx++) {
            String pagePath = createPaginatedPath(basePath, pageIdx);

            List<Object> article = articlePages.get(pageIdx);
            Pagination pagination = createPagination(pageIdx, articlePages.size(), (i) -> createURL(createPaginatedPath(basePath, i)));
            List<ArticlePreview> previews = new ArrayList<>();
            String relPath = getRelPath(pagePath);
            for (Object s : article) {
                previews.add(renderArticlePreview((ArticleComponent) s, relPath, "", project));
            }

            ArticleListPage page = new ArticleListPage(
                    genSiteData(project, relPath),
                    "Articles for: " + topic.name(),
                    hasPagination(pagination),
                    pagination,
                    previews);
            File targetFile = new File(new File(this.targetDirectory, pagePath), "index.html");
            this.templateEngine.render(page.getTemplate(), page, targetFile);
        }
    }

    public boolean hasPagination(Pagination pagination) {
        if (pagination.getPages().size() == 0) return false;
        if (pagination.getPages().size() > 1) return true;
        return pagination.getPages().get(0).size() != 1;
    }

    public Set<Topic> findAllTopics(Project project) {
        Set<Topic> topics = new HashSet<>();
        for (ArticleComponent a : project.getArticles()) {
            topics.addAll(project.getTopics(a));
        }
        return topics;
    }

    public void renderTopicList(Project project) throws IOException {
        List<List<Topic>> topicPages = WebGen.paginateContent(findAllTopics(project).iterator(), 5);
        String basePath = TOPICS_ADDRESS;
        for (int pageIdx = 0; pageIdx < topicPages.size(); pageIdx++) {
            String pagePath = createPaginatedPath(basePath, pageIdx);
            List<SiteLink> topics = topicPages.get(pageIdx).stream().map(this::mkTopicLink).collect(Collectors.toList());
            Pagination pagination = createPagination(pageIdx, topicPages.size(),
                    (i) -> createURL(createPaginatedPath(basePath, i)));
            TopicListPage page = new TopicListPage(
                    genSiteData(project, getRelPath(pagePath)),
                    "Topics",
                    hasPagination(pagination),
                    pagination,
                    topics);
            File targetFile = new File(new File(this.targetDirectory, pagePath), "index.html");
            this.templateEngine.render(page.getTemplate(), page, targetFile);
        }
    }

    public SiteLink mkTopicLink(Topic topic) {
        return new SiteLink(createURL(getTopicPath(topic)), topic.name());
    }

    public String getTopicPath(Topic topic) {
        return TOPICS_ADDRESS + topic.getId() + "/";
    }

    public SiteURL getArticleURL(ArticleComponent entry, Project proj) {
        return createURL(getArticlePath(entry, proj));
    }

    private HashMap<Object, String> getAllPaths(Project project) {
        HashMap<Object, String> result = new HashMap<>();
        for (ArticleComposite a : project.getArticles()) {
            result.putAll(a.getAllPaths());
        }
        return result;
    }

    /**
     * get a path with all the parents of other (sub)articles
     * or (sub)events -- this corresponds to the relative URL of
     * the article
     * @param article
     */

    public String getArticlePath(ArticleComponent article, Project proj) {
        HashMap<Object, String> all = getAllPaths(proj);
        String path = all.get(article);
        //String path = article.getId() + "/";
        return ENTRY_ADDRESS + path;
    }


    public void renderArticleList(Project project) throws IOException {
        List<List<Object>> articlePages = WebGen.paginateContent(findAllArticles(project).iterator(), 5);
        String basePath = ARTICLES_ADDRESS;
        for (int pageIdx = 0; pageIdx < articlePages.size(); pageIdx++) {
            String pagePath = createPaginatedPath(basePath, pageIdx);
            List<Object> articles = articlePages.get(pageIdx);
            Pagination pagination = createPagination(pageIdx, articlePages.size(),
                    (i) -> createURL(createPaginatedPath(basePath, i)));
            List<ArticlePreview> previews = new ArrayList<>();
            String relPath = getRelPath(pagePath);
            for (Object s : articles) {
                previews.add(renderArticlePreview((ArticleComponent) s, relPath, "", project));
            }

            ArticleListPage page = new ArticleListPage(
                    genSiteData(project, relPath),
                    "Articles",
                    hasPagination(pagination),
                    pagination,
                    previews);
            File targetFile = new File(new File(this.targetDirectory, pagePath), "index.html");
            this.templateEngine.render(page.getTemplate(), page, targetFile);
        }
    }


    public void renderEventList(Project project) throws IOException {
        throw new UnsupportedOperationException("Events not yet implemented");
//        List<List<Event>> eventPages = paginateContent(findAllEvents(project).iterator(), 5);
//        String basePath = EVENTS_ADDRESS;
//        for (int pageIdx = 0; pageIdx < eventPages.size(); pageIdx++) {
//            String pagePath = createPaginatedPath(basePath, pageIdx);
//            List<Event> events = eventPages.get(pageIdx);
//            Website.Pagination pagination = createPagination(pageIdx, eventPages.size(), (i) -> createURL(createPaginatedPath(basePath, i)));
//            List<Website.EventPreview> previews = new ArrayList<>();
//            String relPath = getRelPath(pagePath);
//            for (Event s : events)
//                previews.add(renderEventPreview(s, relPath));
//            Website.EventListPage page = new Website.EventListPage(
//                    genSiteData(project, relPath),
//                    "Events",
//                    hasPagination(pagination),
//                    pagination,
//                    previews);
//            File targetFile = new File(new File(targetDirectory, pagePath), "index.html");
//            templateEngine.render(page.getTemplate(), page, targetFile);
//        }
    }

    /**
     * collect links for the navigation bar in the page header
     *
     * @param project project to be rendered
     * @return a list of named links
     */
    public List<SiteLink> genHeaders(Project project) {
        if (this.headers == null) {
            this.headers = new ArrayList<>(3);
            this.headers.add(this.HOME_LINK);
            this.headers.add(this.ARTICLES_LINK);
//            headers.add(EVENTS_LINK);
            if (!findAllTopics(project).isEmpty())
                this.headers.add(this.TOPICS_LINK);
        }
        return this.headers;
    }

    /**
     * create a preview snippet of an article
     *
     * @param article the article
     * @param relPath the relative path of the current page
     * @param prefix  a prefix for the article's title
     * @return an ArticlePreview object for the template engine
     */
    public ArticlePreview renderArticlePreview(ArticleComponent article, String relPath, String prefix, Project proj) {
        StringWriter w = new StringWriter();
        int previewLength = 200;
        for (AbstractContent c : article.getContent()) {
            if (c instanceof FormattedTextDocument) {
                if (previewLength > 0) {
                    previewLength = ((FormattedTextDocument) c).toPreview(w, previewLength);
                }
            }
        }

        return new ArticlePreview(
                prefix,
                article.getTitle(),
                WebGen.readableFormat(article.getPublishedDate()),
                w.toString(),
                relPath,
                getArticleURL(article, proj));
    }

    /**
     * create a preview snippet of an event
     *
     * @param event   the event
     * @param relPath the relative path of the current page
     * @return an EventPreview object for the template engine
     */
    public EventPreview renderEventPreview(Event event, String relPath) throws IOException {
//        StringWriter w = new StringWriter();
//        int previewLength = 200;
//        for (AbstractContent c : event.getContent())
//            if (c instanceof FormattedTextDocument)
//                if (previewLength > 0)
//                    previewLength = ((FormattedTextDocument) c).toPreview(w, previewLength);
//
//        return new Website.EventPreview(
//                event.getTitle(),
//                WebGen.readableFormat(event.getStartDate()),
//                WebGen.readableFormat(event.getEndDate()),
//                w.toString(),
//                relPath, getEventURL(event));
        throw new UnsupportedOperationException("Events not yet implemented");
    }
}
