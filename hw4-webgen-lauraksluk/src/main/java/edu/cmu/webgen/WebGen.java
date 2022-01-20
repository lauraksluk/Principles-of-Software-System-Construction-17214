package edu.cmu.webgen;

import com.github.jknack.handlebars.internal.text.StringEscapeUtils;
import com.joestelmach.natty.Parser;
import edu.cmu.webgen.parser.ProjectParser;
import edu.cmu.webgen.project.*;

import java.io.StringWriter;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class WebGen {

    private static final Map<String, Integer> idCounter = new HashMap<>();
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

    public static void main(String[] args) {
        try {
            WebGenArgs options = new WebGenArgs(args);
            if (!options.projectSourceDirectoryExists() || options.isHelp()) {
                options.printHelp();
                return;
            }
            Project project = new ProjectParser().loadProject(options.getProjectSourceDirectory());
            new CLI(project).run(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * using external library to flexibly parse dates
     * <p>
     * requires a bit of hacking with different time formats
     *
     * @param inputDate input date string in human readable time/date format
     * @return parsed date as LocalDateTime
     * @throws ParseException if input date string cannot be parsed
     */
    public static LocalDateTime parseDate(String inputDate) throws ParseException {
        List<Date> dates = new Parser().parse(inputDate,
                Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())).get(0).getDates();
        if (dates.isEmpty())
            throw new ParseException("Cannot parse date %s".formatted(inputDate), 0);
        return LocalDateTime.ofInstant(dates.get(0).toInstant(), ZoneId.systemDefault());
    }

    /**
     * print a date in a readable format
     *
     * @param date the date
     * @return string representing the date
     */
    public static String readableFormat(LocalDateTime date) {
        return formatter.format(date.atZone(ZoneId.systemDefault()));
    }

    public static String genId(String title) {
        String id = title.toLowerCase().replaceAll("[^a-z0-9]", "_");
        if (idCounter.containsKey(id)) {
            int idIdx = idCounter.get(id) + 1;
            idCounter.put(id, idIdx);
            id = id + idIdx;
        } else
            idCounter.put(id, 1);
        return id;
    }

    /**
     * helper function to paginate content
     *
     * @param content  iterator of content
     * @param pageSize number of elements per page
     * @param <R>      type of the content
     * @return list of lists of content, where each inner list has pageSize entries (possibly except the last)
     */
    public static <R> List<List<R>> paginateContent(Iterator<R> content, int pageSize) {
        List<List<R>> result = new ArrayList<>();
        List<R> pageContent = new ArrayList<>(pageSize);
        while (content.hasNext()) {
            if (pageContent.size() >= pageSize) {
                result.add(pageContent);
                pageContent = new ArrayList<>(pageSize);
            }
            pageContent.add(content.next());
        }
        result.add(pageContent);
        return result;
    }

    /**
     * helper functions to convert FileTime into LocalDateTime
     *
     * @param fileTime input time
     * @return LocalDateTime object for the same file
     */
    public LocalDateTime getDateTime(FileTime fileTime) {
        return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
    }

    public int previewText(FormattedTextContent content, StringWriter w, int maxLength) {
        if (content instanceof TextFragmentSequence node) {
            for (TextFragment t : node.getFragments()) {
                if (maxLength > 0) {
                    maxLength = previewText(t, w, maxLength);
                }
            }
            return maxLength;
        }
        if (content instanceof Heading node) {
            int l = node.level() + 1;
            w.write("<p><strong class=\"previewh" + l + "\">");
            maxLength = previewText(node.text(), w, maxLength);
            w.write("</strong></p>");
            return maxLength;
        }
        if (content instanceof TextParagraph node) {
            w.write("<p>");
            maxLength = previewText(node.text(), w, maxLength);
            w.write("</p>");
            return maxLength;
        }
        if (content instanceof BulletList node) {
            w.write("<p><ul>");
            for (FormattedTextContent t : node.items()) {
                if (maxLength > 0) {
                    w.write("<li>");
                    maxLength = previewText(t, w, maxLength);
                    w.write("</li>");
                }
            }
            w.write("</ul></p>");
            return maxLength;
        }
        if (content instanceof BlockQuote node) {
            w.write("<blockquote>");
            for (FormattedTextContent p : node.paragraphs()) {
                maxLength = previewText(p, w, maxLength);
            }
            w.write("</blockquote>");
            return maxLength;
        }
        if (content instanceof PlainTextFragment node) {
            if (node.text().length() > maxLength) {
                w.write(StringEscapeUtils.escapeHtml4(node.text().substring(0, maxLength)));
                w.write("...");
                return 0;
            }
            w.write(StringEscapeUtils.escapeHtml4(node.text()));
            return maxLength - node.text().length();
        }
        if (content instanceof InlineImage node) {
            return maxLength;
        }
        if (content instanceof DecoratedTextFragment node) {
            w.write(node.getHtmlOpen());
            maxLength = previewText(node.getText(), w, maxLength);
            w.write(node.getHtmlClose());
            return maxLength;
        }

        throw new UnsupportedOperationException("Preview for content %s of type %s not supported".formatted(
                content, content.getClass().getSimpleName()));
    }

    public List<Object> findArticlesByTopic(Project project, Topic topic) {
        List<Object> result = new ArrayList<>();
        for (ArticleComponent a : project.getArticles()) {
            if (project.getTopics(a).contains(topic))
                result.add(a);
        }
        return result;
    }
}
