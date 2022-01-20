package edu.cmu.webgen.project;

public class StrongEmphasisTextFragment extends DecoratedTextFragment {
    public StrongEmphasisTextFragment(TextFragment text) {
        super(text, "<strong>", "</strong>");
    }
}
