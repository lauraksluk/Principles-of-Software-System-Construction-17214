package edu.cmu.webgen.rendering.data;

public class SiteURL {
    private final String path;

    public SiteURL(String path) {
        assert path.startsWith("/");
        assert path.endsWith("/index.html");
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
