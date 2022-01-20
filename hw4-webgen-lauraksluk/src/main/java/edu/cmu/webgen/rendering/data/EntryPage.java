package edu.cmu.webgen.rendering.data;

import java.util.List;

public abstract class EntryPage extends Page {
    private final List<SiteLink> topics;
    private final List<ContentFragment> content;

    protected EntryPage(SiteData siteData, String pageTitle, List<SiteLink> breadcrumbs, List<SiteLink> topics,
                        List<ContentFragment> content) {
        super(siteData, pageTitle, breadcrumbs);
        this.topics = topics;
        this.content = content;
    }

    public List<SiteLink> getTopics() {
        return this.topics;
    }

    public List<ContentFragment> getContent() {
        return this.content;
    }

}
