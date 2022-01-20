package edu.cmu.webgen.project;

import edu.cmu.webgen.WebGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

public class ArticleLeaf implements ArticleComponent, Comparable<ArticleLeaf> {
    protected @Nullable String id = null;
    protected @NotNull final String directoryName;
    private final LocalDateTime lastUpdate;
    private final LocalDateTime created;
    final List<AbstractContent> content;
    protected @NotNull Metadata metadata = new Metadata();

    public ArticleLeaf(List<AbstractContent> content, @NotNull String directoryName, @NotNull LocalDateTime created,
                       @NotNull LocalDateTime lastUpdate) {
        this.content = content;
        this.directoryName = directoryName;
        this.created = created;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public int compareTo(@NotNull ArticleLeaf that) {
        return this.getTitle().compareTo(that.getTitle());
    }

    /**
     * return an unique ID of letters, digits and underscores only, based on the title
     *
     * @return the id
     *//**
     * return an unique ID of letters, digits and underscores only, based on the title
     *
     * @return the id
     */
    @Override
    public String getId() {
        if (this.id == null)
            this.id = WebGen.genId(getTitle());
        return this.id;
    }

    /**
     * get the most recent update of this folder or any content inside
     *
     * @return timestamp of last update
     */
    @Override
    public @NotNull LocalDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * get the oldest creation date of this folder or any content inside
     *
     * @return timestamp of last creation date
     */
    @Override
    public @NotNull LocalDateTime getCreated() {
        return this.created;
    }

    @Override
    public LocalDateTime getPublishedDate() {
        if (this.metadata.has("date")) {
            try {
                return WebGen.parseDate(this.metadata.get("date"));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        }
        return getLastUpdate();
    }

    @Override
    public String getTitle() {
        if (this.metadata.has("title"))
            return this.metadata.get("title");
        for (AbstractContent n : this.content)
            if (n.hasTitle())
                return Objects.requireNonNull(n.getTitle());
        return this.directoryName;
    }

    @Override
    public void addContent(AbstractContent newContent) {
        this.content.add(newContent);
    }

    @Override
    public List<AbstractContent> getContent() {
        return this.content;
    }

    @Override
    public void addMetadata(Metadata m) {
        this.metadata = this.metadata.concat(m);
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

    public @NotNull Set<Topic> getTopics(HashMap<Object, Set<Topic>> topics) {
        Set<Topic> result = new HashSet<>();
        for (AbstractContent c : content) {
            result.addAll(topics.getOrDefault(c, new HashSet<>()));
        }
        return result;
    }

    public List<AbstractContent> getAllContent() {
        return getContent();
    }

    public List<ArticleComponent> getChildren() {
        return new ArrayList<>();
    }

    public List<Object> getAllArticles() {
        List<Object> result = new ArrayList<>();
        result.add(this);
        return result;
    }

}
