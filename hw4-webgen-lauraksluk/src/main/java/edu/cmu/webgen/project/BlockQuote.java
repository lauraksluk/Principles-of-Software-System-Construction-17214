package edu.cmu.webgen.project;

import java.io.StringWriter;
import java.util.List;

public record BlockQuote(List<FormattedTextContent> paragraphs) implements FormattedTextContent {
    @Override
    public void toHtml(StringWriter w) {
        w.write("<blockquote>");
        for (FormattedTextContent p : this.paragraphs) {
            p.toHtml(w);
        }
        w.write("</blockquote>");
    }
}
