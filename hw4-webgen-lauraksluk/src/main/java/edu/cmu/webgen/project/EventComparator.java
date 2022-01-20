package edu.cmu.webgen.project;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        if (!o1.getStartDate().equals(o2.getStartDate()))
            return o1.getStartDate().compareTo(o2.getStartDate());
        if (!o1.getEndDate().equals(o2.getEndDate()))
            return o1.getEndDate().compareTo(o2.getEndDate());
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
