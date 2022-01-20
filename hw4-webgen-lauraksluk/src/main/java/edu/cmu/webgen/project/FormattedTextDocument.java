package edu.cmu.webgen.project;

import edu.cmu.webgen.WebGen;
import edu.cmu.webgen.rendering.TemplateEngine;
import edu.cmu.webgen.rendering.data.ContentFragment;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents formatted text documents, in which text is structured in to paragraphs
 * with formatted text.
 */
public class FormattedTextDocument extends AbstractContent {
    private final List<FormattedTextContent> paragraphs;
    private final Metadata metadata;
    private final long textSize;

    public FormattedTextDocument(List<FormattedTextContent> paragraphs, Metadata metadata, LocalDateTime created,
                          LocalDateTime lastUpdate, long textSize) {
        super(created, lastUpdate);
        this.paragraphs = paragraphs;
        this.metadata = metadata;
        this.textSize = textSize;
    }

    public void toHtml(StringWriter w) {
        for (FormattedTextContent p : this.paragraphs) {
            p.toHtml(w);
        }
    }

    public ContentFragment getContentFragment(TemplateEngine eng) {
        StringWriter w = new StringWriter();
        toHtml(w);
        return new ContentFragment(null, w.toString());
    }

    /**
     * Creates preview text of a maximum length without any formatting.
     *
     * @param w         writer into which the text is written
     * @param maxLength maximum number of characters to write
     * @return length of remaining length budget
     * @
     */
    public int toPreview(StringWriter w, int maxLength) {
        for (FormattedTextContent p : this.paragraphs) {
            if (maxLength > 0)
                maxLength = new WebGen().previewText(p, w, maxLength);
        }
        return maxLength;
    }

    /**
     * Returns all paragraphs in this document
     *
     * @return paragraphs
     */
    public List<FormattedTextContent> getParagraphs() {
        return this.paragraphs;
    }


    public String getTitle() {
        //metadata title takes priorty
        if (this.metadata.has("title"))
            return this.metadata.get("title");
        // if there are captions, take the first one
        for (FormattedTextContent p : this.paragraphs) {
            if (p instanceof Heading h)
                if (h.level() <= 1)
                    return h.text().toPlainText();
        }
        //if the first paragraph is text, let's take the first line
        if (this.paragraphs.size() >= 1) {
            if (this.paragraphs.get(0) instanceof TextParagraph text) {
                String s = text.text().toPlainText();
                if (s.contains("\n")) s = s.substring(0, s.indexOf("\n"));
                if (!"".equals(s.trim()))
                    return s;
            }
        }
        return null;
    }

    public long getTextSize() {
        return this.textSize;
    }

    //    public static record Image(String source, String paragraphs) implements Paragraph {}

}
