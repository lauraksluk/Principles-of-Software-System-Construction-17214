package edu.cmu.webgen.project;

import edu.cmu.webgen.rendering.TemplateEngine;
import edu.cmu.webgen.rendering.data.ContentFragment;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;

public class YoutubeVideo extends AbstractContent {
    private final String youtubeId;

    public YoutubeVideo(String youtubeId, Metadata metadata, LocalDateTime created, LocalDateTime lastUpdate) {
        super(created, lastUpdate);
        this.youtubeId = youtubeId;
    }

    public String getYoutubeId() {
        return this.youtubeId;
    }

    public ContentFragment getContentFragment(TemplateEngine eng) throws IOException {
        StringWriter w = new StringWriter();
        eng.render("content-fragment-youtube", Map.of("id", getYoutubeId()), w);
        return new ContentFragment(null, w.toString());
    }

}
