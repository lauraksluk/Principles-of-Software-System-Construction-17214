package edu.cmu.webgen.rendering.data;

import java.util.Collections;
import java.util.List;

public class EventListPage extends PaginatedPage {
    final private List<EventPreview> events;


    public EventListPage(SiteData siteData, String pageTitle, boolean hasPagination, Pagination pagination,
                         List<EventPreview> events) {
        super(siteData, pageTitle, Collections.emptyList(), hasPagination, pagination);
        this.events = events;
    }

    public String getTemplate() {
        return "event-list.html";
    }

    public List<EventPreview> getEvents() {
        return this.events;
    }


}
