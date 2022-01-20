package edu.cmu.webgen.rendering.data;

import java.util.List;

public abstract class PaginatedPage extends Page {
    final private Pagination pagination;
    final private boolean hasPagination;

    public PaginatedPage(SiteData siteData, String pageTitle, List<SiteLink> breadcrumbs, boolean hasPagination,
                            Pagination pagination) {
        super(siteData, pageTitle, breadcrumbs);
        this.pagination = pagination;
        this.hasPagination = hasPagination;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public boolean hasPagination() {
        return this.hasPagination;
    }
}
