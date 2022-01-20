package edu.cmu.webgen.project;

public class Link extends DecoratedTextFragment {
    private final String targetURI;

    public Link(String targetURI, TextFragment text) {
        super(text, "<a href=\"" + targetURI + "\">", "</a>");
        this.targetURI = targetURI;
    }
}
