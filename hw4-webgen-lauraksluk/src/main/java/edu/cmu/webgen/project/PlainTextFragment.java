package edu.cmu.webgen.project;

import com.github.jknack.handlebars.internal.text.StringEscapeUtils;

import java.io.StringWriter;

public record PlainTextFragment(String text) implements TextFragment {
    @Override
    public void toHtml(StringWriter w) {
        w.write(StringEscapeUtils.escapeHtml4(this.text));
    }

    @Override
    public String toPlainText() {
        return this.text;
    }
}
