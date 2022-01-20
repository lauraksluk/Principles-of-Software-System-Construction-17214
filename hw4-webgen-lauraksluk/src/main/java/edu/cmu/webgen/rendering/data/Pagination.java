package edu.cmu.webgen.rendering.data;

import java.util.List;

public class Pagination {
    //pages are grouped in sections interrupted by "..." in the rendering
    private final List<List<SiteLink>> pages;

    public Pagination(List<List<SiteLink>> pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (List<SiteLink> page : this.pages) {
            for (SiteLink link : page) {
                result.append("[").append(link.getTitle()).append("]");
                if (link.getIsCurrent())
                    result.append("*");
                result.append(" ");
            }
            result.append("... ");
        }
        return result.toString();
    }

    public List<List<SiteLink>> getPages() {
        return this.pages;
    }
}
