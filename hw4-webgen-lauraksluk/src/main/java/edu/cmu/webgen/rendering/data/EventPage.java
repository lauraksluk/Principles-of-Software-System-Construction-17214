package edu.cmu.webgen.rendering.data;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class EventPage extends EntryPage {
    private final @NotNull String startDate;
    private final @NotNull String endDate;

    public EventPage(SiteData siteData, String pageTitle, List<SiteLink> breadcrumbs, @NotNull String startDate,
                     @NotNull String endDate, List<SiteLink> topics, List<ContentFragment> content) {
        super(siteData, pageTitle, breadcrumbs, topics, content);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTemplate() {
        return "event.html";
    }


    public @NotNull String getStartDate() {
        return this.startDate;
    }

    public @NotNull String getEndDate() {
        return this.endDate;
    }


}
