package edu.cmu.webgen.project;

import edu.cmu.webgen.rendering.TemplateEngine;
import edu.cmu.webgen.rendering.data.ContentFragment;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Represents some form of content in this project
 */
public abstract class AbstractContent {

    private final LocalDateTime lastUpdate;
    private final LocalDateTime created;

    public AbstractContent(LocalDateTime created, LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        this.created = created;
    }

    public abstract ContentFragment getContentFragment(TemplateEngine eng) throws IOException;

    /**
     * timestamp of the last update of the content this node represents
     *
     * @return the timestamp
     */
    public LocalDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * timestamp of the content this node represents was created
     *
     * @return the timestamp
     */
    public LocalDateTime getCreated() {
        return this.created;
    }

    public boolean hasTitle() {
        return getTitle() != null;
    }

    /**
     * title of this content, if any
     *
     * @return title or null if this content has no title
     */
    public @Nullable String getTitle() {
        return null;
    }

}
