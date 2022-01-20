package edu.cmu.webgen.project;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a project with articles and events.
 */
public class Project {
    private String ownerOrg;
    private final List<Event> events;
    private final List<ArticleComposite> articles;
    private final String title;
    private final HashMap<Object, Set<Topic>> topics;

    public Project(String title, String ownerOrg, List<ArticleComposite> articles, List<Event> events, HashMap<Object, Set<Topic>> topics) {
        this.title = title;
        this.ownerOrg = ownerOrg;
        this.articles = new ArrayList<>(articles);
        Collections.sort(this.articles);
        this.events = new ArrayList<>(events);
        Collections.sort(this.events);
        this.topics = new HashMap<>(topics);
    }


    @Override
    public String toString() {
        return "Project %s by %s with %d articles and %d events".formatted(
                this.title, this.ownerOrg, this.articles.size(), this.events.size());
    }

    /**
     * return the title of this project
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }


    public List<Event> getUpcomingEvents(int max) {
        var now = LocalDateTime.now();
        return this.events.stream().
                filter(e -> e.getStartDate().isAfter(now)).
                sorted(Comparator.comparing(Event::getStartDate)).limit(max).toList();
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public List<ArticleComposite> getArticles() {
        return this.articles;
    }


    public @NotNull Set<Topic> getTopics(Object projectPart) {
        Set<Topic> result = new HashSet<>();

        // event also object + has topics

        if (projectPart instanceof ArticleComponent) {
            result.addAll(((ArticleComponent) projectPart).getTopics(topics));
        }

        return result;
    }

    public @NotNull Set<Topic> getAllTopics() {
        Set<Topic> result = new HashSet<>();
        for (Set<Topic> t : this.topics.values())
            result.addAll(t);
        return result;
    }

    public void setTopics(Object projectPart, Set<Topic> newTopics) {
        this.topics.put(projectPart, newTopics);
    }

    public String getOwnerOrg() {
        return this.ownerOrg;
    }

    public boolean isArticlePinned(ArticleComponent article) {
        return article.getMetadata().has("pinned") && !article.getMetadata().get("pinned").equals("false");
    }
}
