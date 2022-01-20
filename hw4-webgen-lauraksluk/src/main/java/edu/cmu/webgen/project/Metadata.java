package edu.cmu.webgen.project;

import edu.cmu.webgen.WebGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * represents key value pairs from yaml files
 * <p>
 * nested content is represented as "/"-separated key, lists are represented with [0], [1], ... in key names
 */
public class Metadata {
    public final Map<String, String> metadata;

    public Metadata() {
        this.metadata = Collections.emptyMap();
    }

    public Metadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean has(String key) {
        return this.metadata.containsKey(key);
    }

    public @NotNull String get(String key) {
        return this.metadata.getOrDefault(key, "");
    }

    public @NotNull Set<String> getKeys() {
        return this.metadata.keySet();
    }

    public boolean isDate(String key) {
        if (!has(key)) return false;
        try {
            WebGen.parseDate(get(key));
            return true;
        } catch (ParseException e) {
            System.err.println("Warning: Cannot parse date in metadata: %s -- %s".formatted(get(key), e.getMessage()));
            return false;
        }
    }

    public @Nullable LocalDateTime getDate(String key) {
        try {
            return WebGen.parseDate(get(key));
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        this.metadata.forEach((k, v) -> b.append(k).append("=").append(v).append("\n"));
        return b.toString();
    }

    public Metadata concat(Metadata that) {
        if (this.metadata.isEmpty()) return that;
        if (that.metadata.isEmpty()) return this;

        Set<String> keyoverlap = new HashSet<>(this.metadata.keySet());
        keyoverlap.removeAll(that.metadata.keySet());
        if (!keyoverlap.isEmpty())
            System.err.println("Warning: Metadata defined repeatedly in directory (%s)".formatted(keyoverlap.toString()));

        Map<String, String> m = new HashMap<>();
        m.putAll(this.metadata);
        m.putAll(that.metadata);
        return new Metadata(m);
    }
}
