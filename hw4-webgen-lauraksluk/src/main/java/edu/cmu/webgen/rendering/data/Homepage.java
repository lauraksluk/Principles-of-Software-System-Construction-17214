package edu.cmu.webgen.rendering.data;

import java.util.Collections;
import java.util.List;

public class Homepage extends Page {
    final private List<ArticlePreview> articles;
    final private List<EventListing> events;
    private final SiteURL articlesLink;
    private final SiteURL eventsLink;

    public Homepage(SiteData siteData, List<ArticlePreview> articles, List<EventListing> events, SiteURL articlesLink,
                    SiteURL eventsLink) {
        super(siteData, "Featured articles", Collections.emptyList());
        this.articles = articles;
        this.events = events;
        this.articlesLink = articlesLink;
        this.eventsLink = eventsLink;
    }

    @Override
    public String getTemplate() {
        return "homepage.html";
    }

    public List<ArticlePreview> getArticles() {
        return articles;
    }

    public List<EventListing> getEvents() {
        return events;
    }

    public SiteURL getArticlesLink() {
        return articlesLink;
    }

    public SiteURL getEventsLink() {
        return eventsLink;
    }
}
