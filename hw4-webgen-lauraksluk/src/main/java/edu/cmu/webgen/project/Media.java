package edu.cmu.webgen.project;

import java.io.File;
import java.time.LocalDateTime;

public abstract class Media extends AbstractContent {
    private final File mediaPath;
    private final long mediaSize;

    Media(File mediaPath, LocalDateTime created, LocalDateTime lastUpdate, long size) {
        super(created, lastUpdate);
        this.mediaPath = mediaPath;
        this.mediaSize = size;
    }

    public File getMediaPath() {
        return this.mediaPath;
    }

    public long getMediaSize() {
        return this.mediaSize;
    }

}
