package edu.cmu.webgen.project;

import java.io.StringWriter;

public class HorizontalRow implements FormattedTextContent {
    @Override
    public void toHtml(StringWriter w) {
        w.write("<hr />");
    }

    public int toPreview(StringWriter w, int maxLength) {
        return maxLength;
    }
}
