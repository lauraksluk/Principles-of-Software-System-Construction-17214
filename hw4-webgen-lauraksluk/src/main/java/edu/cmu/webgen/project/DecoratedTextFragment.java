package edu.cmu.webgen.project;

import java.io.StringWriter;

public abstract class DecoratedTextFragment implements TextFragment {
    private final TextFragment text;
    private final String htmlOpen;
    private final String htmlClose;

    public DecoratedTextFragment(TextFragment text, String htmlOpen, String htmlClose) {
        this.text = text;
        this.htmlOpen = htmlOpen;
        this.htmlClose = htmlClose;
    }

    @Override
    public void toHtml(StringWriter w) {
        w.write(this.htmlOpen);
        this.text.toHtml(w);
        w.write(this.htmlClose);
    }

    @Override
    public String toPlainText() {
        return this.text.toPlainText();
    }

    public String getHtmlClose() {
        return this.htmlClose;
    }

    public String getHtmlOpen() {
        return this.htmlOpen;
    }

    public TextFragment getText() {
        return this.text;
    }
}
