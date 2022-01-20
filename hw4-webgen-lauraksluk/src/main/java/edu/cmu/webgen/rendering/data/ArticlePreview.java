package edu.cmu.webgen.rendering.data;

public class ArticlePreview {

    private final String prefix;
    final private String title;
    final private String date;
    final private String preview;
    private final SiteURL address;
    private final String relPath;

    public ArticlePreview(String prefix, String title, String date, String preview, String relPath, SiteURL address) {
        this.prefix = prefix;
        this.title = title;
        this.date = date;
        this.preview = preview;
        this.address = address;
        this.relPath = relPath;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDate() {
        return this.date;
    }

    public String getPreview() {
        return this.preview;
    }

    public SiteURL getAddress() {
        return this.address;
    }

    public String getRelPath() {
        return this.relPath;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
