package edu.cmu.webgen.project;

import java.io.StringWriter;

public record CodeBlock(String source, String language) implements FormattedTextContent {
    @Override
    public void toHtml(StringWriter w) {
        w.write("<pre>");
        w.write(this.source);
        w.write("</pre>");
    }

    public int toPreview(StringWriter w, int maxLength) {
        return maxLength;
    }
}
