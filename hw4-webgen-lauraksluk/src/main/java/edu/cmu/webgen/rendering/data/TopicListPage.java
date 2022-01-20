package edu.cmu.webgen.rendering.data;

import java.util.Collections;
import java.util.List;

public class TopicListPage extends PaginatedPage {
    final private List<SiteLink> topics;

    public TopicListPage(SiteData siteData, String pageTitle, boolean hasPagination, Pagination pagination,
                         List<SiteLink> topics) {
        super(siteData, pageTitle, Collections.emptyList(), hasPagination, pagination);
        this.topics = topics;
    }

    public String getTemplate() {
        return "topic-list.html";
    }

    public List<SiteLink> getTopics() {
        return this.topics;
    }

}
