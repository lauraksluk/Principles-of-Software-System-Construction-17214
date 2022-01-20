package edu.cmu.webgen.rendering.data;

import java.util.Collections;
import java.util.List;

public class ArticleListPage extends PaginatedPage {
    final private List<ArticlePreview> articles;


    public ArticleListPage(SiteData siteData, String pageTitle, boolean hasPagination, Pagination pagination,
                           List<ArticlePreview> articles) {
        super(siteData, pageTitle, Collections.emptyList(), hasPagination, pagination);
        this.articles = articles;
    }

    public String getTemplate() {
        return "article-list.html";
    }

    public List<ArticlePreview> getArticles() {
        return articles;
    }


}
