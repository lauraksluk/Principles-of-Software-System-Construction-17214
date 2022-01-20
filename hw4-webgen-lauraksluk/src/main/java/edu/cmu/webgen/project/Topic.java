package edu.cmu.webgen.project;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Topic implements Comparable<Topic> {
    public final String name;
    public final String id;

    public Topic(String name) {
        this.name = name;
        this.id = name.toLowerCase().replaceAll("[^a-z0-9]", "-");
    }

    public static Set<Topic> from(Metadata metadata) {
        Set<Topic> result = new HashSet<>();
        for (String topicKey : List.of("topic", "topics")) {
            if (metadata.has(topicKey)) {
                for (String t : metadata.get(topicKey).split(",")) {
                    result.add(new Topic(t.trim()));
                }
            }
        }
        return result;
    }

    public String name() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Topic) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Topic[" + "name=" + this.name + ", id=" + this.id + ']';
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(@NotNull Topic that) {
        return this.name.compareTo(that.name);
    }
}
