package edu.cmu.webgen.rendering.data;

import java.util.List;

public abstract class Page {
    private final SiteData siteData;
    private final String pageTitle;
    private final List<SiteLink> breadcrumbs;

    public Page(SiteData siteData, String pageTitle, List<SiteLink> breadcrumbs) {
        this.siteData = siteData;
        this.pageTitle = pageTitle;
        this.breadcrumbs = breadcrumbs;
    }

    public String getProjectTitle() {
        return this.siteData.projectTitle();
    }

    public List<SiteLink> getHeaders() {
        return this.siteData.headers();
    }

    public List<SiteLink> getBreadcrumbs() {
        return this.breadcrumbs;
    }

    public String getPageTitle() {
        return this.pageTitle;
    }

    public String getRelPath() {
        return this.siteData.relPath();
    }

    public String getOrganization() {
        return this.siteData.organization();
    }

    public String getLastUpdated() {
        return this.siteData.lastUpdated();
    }

    public SiteData getSiteData() {
        return this.siteData;
    }

    abstract public String getTemplate();
}
