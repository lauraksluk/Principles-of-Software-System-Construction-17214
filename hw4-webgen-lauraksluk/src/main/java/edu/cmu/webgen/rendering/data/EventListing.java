package edu.cmu.webgen.rendering.data;

import java.util.List;

public class EventListing {
    private final SiteURL address;
    private final String title;
    private final String startDate;
    private final List<EventListing> childEvents;

    public EventListing(SiteURL address, String title, String startDate, List<EventListing> childEvents) {
        this.address = address;
        this.title = title;
        this.startDate = startDate;
        this.childEvents = childEvents;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public SiteURL getAddress() {
        return this.address;
    }

    public String getTitle() {
        return this.title;
    }

    public List<EventListing> getChildEvents() {
        return this.childEvents;
    }
}
