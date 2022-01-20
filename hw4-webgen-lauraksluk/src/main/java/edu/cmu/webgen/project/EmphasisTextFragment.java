package edu.cmu.webgen.project;

public class EmphasisTextFragment extends DecoratedTextFragment {
    public EmphasisTextFragment(TextFragment text) {
        super(text, "<em>", "</em>");
    }
}
