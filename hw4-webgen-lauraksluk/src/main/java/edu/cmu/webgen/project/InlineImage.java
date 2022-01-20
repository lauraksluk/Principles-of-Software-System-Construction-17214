package edu.cmu.webgen.project;

import java.io.StringWriter;

public record InlineImage(String source, TextFragment text) implements TextFragment {
    @Override
    public void toHtml(StringWriter w) {
        w.write("<img src=\"" + this.source + "\" alt=\"");
        this.text.toHtml(w);
        w.write("\" />");
    }

    @Override
    public String toPlainText() {
        return "";
    }
}
