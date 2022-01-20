package edu.cmu.webgen.project;

import java.io.StringWriter;
import java.util.List;

public class TextFragmentSequence implements TextFragment {

    private final List<TextFragment> fragments;

    private TextFragmentSequence(List<TextFragment> fragments) {
        this.fragments = fragments;
    }

    public static TextFragment create(List<TextFragment> fragments) {
        if (fragments.size() == 1) return fragments.get(0);
        return new TextFragmentSequence(fragments);
    }

    @Override
    public void toHtml(StringWriter w) {
        for (TextFragment t : getFragments()) {
            t.toHtml(w);
        }
    }

    @Override
    public String toPlainText() {
        StringBuilder b = new StringBuilder();
        for (TextFragment t : getFragments()) {
            b.append(t.toPlainText());
        }
        return b.toString();
    }

    public List<TextFragment> getFragments() {
        return this.fragments;
    }


}
