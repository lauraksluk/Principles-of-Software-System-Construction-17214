package edu.cmu.webgen.project;

import edu.cmu.webgen.rendering.TemplateEngine;
import edu.cmu.webgen.rendering.data.ContentFragment;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;

public class Image extends Media {

    public Image(File mediaPath, LocalDateTime created, LocalDateTime lastUpdate, long imageSize) {
        super(mediaPath, created, lastUpdate, imageSize);
    }

    public ContentFragment getContentFragment(TemplateEngine eng) throws IOException {
        //TODO not yet implemented
        StringWriter w = new StringWriter();
        eng.render("content-fragment-image",
                Map.of("address", getMediaPath(), "title", hasTitle() ? getTitle() : ""), w);
        return new ContentFragment(null, w.toString());
    }

}
