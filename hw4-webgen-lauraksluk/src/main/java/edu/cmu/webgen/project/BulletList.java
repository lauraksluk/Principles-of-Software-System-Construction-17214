package edu.cmu.webgen.project;

import java.io.StringWriter;
import java.util.List;

public record BulletList(List<FormattedTextContent> items) implements FormattedTextContent {
    @Override
    public void toHtml(StringWriter w) {
        w.write("<p><ul>");
        for (FormattedTextContent t : this.items) {
            w.write("<li>");
            t.toHtml(w);
            w.write("</li>");
        }
        w.write("</ul></p>");
    }
}
