package edu.cmu.webgen.project;

import java.io.StringWriter;

public record TextParagraph(TextFragment text) implements FormattedTextContent {
    @Override
    public void toHtml(StringWriter w) {
        w.write("<p>");
        this.text.toHtml(w);
        w.write("</p>");
    }
}
