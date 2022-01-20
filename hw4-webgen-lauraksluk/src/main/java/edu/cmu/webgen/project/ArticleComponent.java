package edu.cmu.webgen.project;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface ArticleComponent {

    String getId();

    LocalDateTime getLastUpdate();

    LocalDateTime getCreated();

    LocalDateTime getPublishedDate();

    String getTitle();

    void addContent(AbstractContent newContent);

    List<AbstractContent> getContent();

    void addMetadata(Metadata m);

    Metadata getMetadata();

    Set<Topic> getTopics(HashMap<Object, Set<Topic>> topics);

    List<AbstractContent> getAllContent();

    List<ArticleComponent> getChildren();

    public List<Object> getAllArticles();

}
