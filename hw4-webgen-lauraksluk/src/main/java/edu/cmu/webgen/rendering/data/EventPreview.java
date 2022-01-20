package edu.cmu.webgen.rendering.data;

public class EventPreview {
    final private String title;
    final private String startDate;
    final private String endDate;
    final private String preview;
    private final String relPath;
    private final SiteURL address;

    public EventPreview(String title, String startDate, String endDate, String preview, String relPath, SiteURL address) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.preview = preview;
        this.relPath = relPath;
        this.address = address;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPreview() {
        return this.preview;
    }

    public SiteURL getAddress() {
        return this.address;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getRelPath() {
        return this.relPath;
    }
}
