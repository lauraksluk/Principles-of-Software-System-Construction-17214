package edu.cmu.webgen.project;

import java.io.StringWriter;

public record Heading(TextFragment text, int level) implements FormattedTextContent {
    @Override
    public void toHtml(StringWriter w) {
        int l = this.level + 1;
        w.write("<h" + l + ">");
        this.text.toHtml(w);
        w.write("</h" + l + ">");
    }
}
