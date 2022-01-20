package edu.cmu.webgen.parser;

import edu.cmu.webgen.WebGen;
import edu.cmu.webgen.project.*;
import edu.cmu.webgen.project.BlockQuote;
import edu.cmu.webgen.project.BulletList;

import edu.cmu.webgen.project.Heading;
import edu.cmu.webgen.project.Link;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.commonmark.ext.front.matter.YamlFrontMatterBlock;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterNode;
import org.commonmark.node.*;

import org.commonmark.node.Image;
import org.commonmark.node.Paragraph;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class loads all files in a directory and reports findings to the {@link ProjectBuilder} class.
 * <p>
 * You will not need to understand details or make any modifications to this class, but you can.
 */
public class ProjectParser {

    private final Parser markdownParser = Parser.builder().extensions(
            Collections.singletonList(YamlFrontMatterExtension.create())).build();

    /**
     * loading a whole directory as a project
     *
     * @param dir target directory with the project's content
     * @return the loaded project
     * @throws IOException
     */
    public Project loadProject(@NotNull File dir) throws IOException, ProjectFormatException {
        if (!(dir.exists() && dir.isDirectory())) throw new IOException("Project directory not found: " + dir);
        BasicFileAttributes attr = Files.readAttributes(dir.toPath(), BasicFileAttributes.class);
        LocalDateTime folderCreated = new WebGen().getDateTime(attr.creationTime());
        LocalDateTime folderLastUpdate = new WebGen().getDateTime(attr.lastModifiedTime());
        ProjectBuilder builder = new ProjectBuilder(dir.getName(), folderCreated, folderLastUpdate);
        processProject(builder, dir);
        return builder.buildProject();
    }

    /**
     * in the top-level directory only look for subdirectories and metadata files
     */
    private void processProject(@NotNull ProjectBuilder builder, @NotNull File dir) throws IOException, ProjectFormatException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    processDirectory(builder, file);
                else if (file.getName().endsWith(".yml"))
                    loadMetadataFile(builder, file);
            }
        }
    }

    /**
     * create an Entry per directory
     * <p>
     * in a directory, look for files and subdirectories
     */
    private void processDirectory(@NotNull ProjectBuilder builder, @NotNull File dir) throws IOException, ProjectFormatException {
        //skip directories starting with _
        if (dir.getName().startsWith("_")) return;

        BasicFileAttributes attr = Files.readAttributes(dir.toPath(), BasicFileAttributes.class);
        LocalDateTime folderCreated = new WebGen().getDateTime(attr.creationTime());
        LocalDateTime folderLastUpdate = new WebGen().getDateTime(attr.lastModifiedTime());
        builder.openDirectory(dir.getName(), folderCreated, folderLastUpdate);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory())
                    processDirectory(builder, file);
                else
                    processFile(builder, file);
            }
        }
        builder.finishDirectory();
    }

    /**
     * check for supported file types and load the files
     */
    private void processFile(@NotNull ProjectBuilder builder, @NotNull File file) throws IOException, ProjectFormatException {
        if (file.getName().toLowerCase().endsWith(".md"))
            loadMarkdown(builder, file);
        if (file.getName().toLowerCase().endsWith(".txt"))
            loadTextfile(builder, file);
        if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png"))
            loadImage(builder, file);
        if (file.getName().toLowerCase().endsWith(".mp4") || file.getName().toLowerCase().endsWith(".mpg"))
            loadVideo(builder, file);
        if (file.getName().toLowerCase().endsWith(".youtube"))
            loadYoutubeVideo(builder, file);
        if (file.getName().toLowerCase().endsWith(".yml"))
            loadMetadataFile(builder, file);
    }

    /**
     * load a yaml file as key-value pairs
     *
     * @param file yaml file to be parsed
     * @return key value pairs of metadata
     * @throws IOException
     */
    private Map<String, String> parseMetadataFile(File file) throws IOException {
        // abusing the yaml parser of the markdown library;
        // the parser is pretty incomplete and doesn't detect nested structures or different lists and literals,
        // but it is good enough for plain key value pairs and lists
        String fileContent = FileUtils.readFileToString(file, Charset.defaultCharset());
        String yaml = "---\n" + fileContent + "\n---\n";
        Node document = this.markdownParser.parse(yaml);
        if (document.getFirstChild() instanceof YamlFrontMatterBlock yamlBlock)
            return loadMetadataBlock(yamlBlock);
        return Collections.emptyMap();
    }

    private void loadMetadataFile(ProjectBuilder builder, File file) throws IOException {
        builder.foundMetadata(parseMetadataFile(file));
    }

    /**
     * find yaml metadata as top-level element in markdown document
     */
    private Map<String, String> loadMetadata(Node doc) {
        Map<String, String> result = new HashMap<>();
        Node node = doc.getFirstChild();
        while (node != null) {
            if (node instanceof YamlFrontMatterBlock yamlBlock)
                result.putAll(loadMetadataBlock(yamlBlock));
            node = node.getNext();
        }
        return result;
    }

    /**
     * helper function for loading metadata from the markdown parser output
     *
     * @param yamlBlock input
     * @return parsed metadata
     */
    private Map<String, String> loadMetadataBlock(YamlFrontMatterBlock yamlBlock) {
        Map<String, String> metadata = new HashMap<>();
        Node node = yamlBlock.getFirstChild();
        while (node instanceof YamlFrontMatterNode yamlNode) {
            String key = yamlNode.getKey();
            List<String> values = yamlNode.getValues();
            if (values.size() == 1)
                metadata.put(key, values.get(0));
            else for (int idx = 0; idx < values.size(); idx++)
                metadata.put(key + "[" + idx + "]", values.get(idx));
            node = node.getNext();
        }
        return metadata;
    }

    /**
     * load markdown files as formatted text, process yaml metadata within markdown
     */
    public void loadMarkdown(@NotNull ProjectBuilder builder, @NotNull File file) throws IOException, ProjectFormatException {
        assert file.exists();
        try (FileReader fr = new FileReader(file)) {
            Node document = this.markdownParser.parseReader(fr);
            Map<String, String> metadata = loadMetadata(document);
            List<FormattedTextContent> text = parseParagraphList(document.getFirstChild());
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime fileCreated = new WebGen().getDateTime(attr.creationTime());
            LocalDateTime fileLastUpdate = new WebGen().getDateTime(attr.lastModifiedTime());
            long fileSize = attr.size();
            builder.foundTextDocument(text, metadata, fileCreated, fileLastUpdate, fileSize);
        }
    }

    /**
     * load a text file, represented as formatted text without formatting and without metadata
     */
    public void loadTextfile(@NotNull ProjectBuilder builder, @NotNull File file) throws IOException, ProjectFormatException {
        assert file.exists();
        try (BufferedReader fr = new BufferedReader(new FileReader(file))) {
            List<FormattedTextContent> paragraphs = new ArrayList<>();
            String line = fr.readLine();
            StringBuilder paragraph = new StringBuilder();
            //first line is interpreted as title
            String firstLine = line;
            //text is broken into paragraphs at empty lines
            while (line != null) {
                if (line.equals("")) {
                    if (paragraph.length() > 0) {
                        paragraphs.add(new TextParagraph(
                                new PlainTextFragment(paragraph.toString())));
                    }
                    paragraph = new StringBuilder();
                } else {
                    paragraph.append(line).append('\n');
                }
            }
            if (paragraph.length() > 0) {
                paragraphs.add(new TextParagraph(
                        new PlainTextFragment(paragraph.toString())));
            }
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime fileCreated = new WebGen().getDateTime(attr.creationTime());
            LocalDateTime fileLastUpdate = new WebGen().getDateTime(attr.lastModifiedTime());
            long fileSize = attr.size();
            builder.foundTextDocument(paragraphs, Collections.emptyMap(), fileCreated, fileLastUpdate, fileSize);
        }
    }

    /**
     * convert markdown to FormattedTextDocument
     */
    private List<FormattedTextContent> parseParagraphList(Node node) {
        List<FormattedTextContent> result = new ArrayList<>();
        while (node != null) {
            parseParagraph(node).ifPresent(result::add);
            node = node.getNext();
        }
        return result;
    }

    /**
     * convert markdown to FormattedTextDocument
     */
    private @NotNull Optional<FormattedTextContent> parseParagraph(Node node) {
        if (node instanceof YamlFrontMatterBlock) return Optional.empty();
        if (node instanceof HtmlBlock)
            return Optional.empty();
        if (node instanceof org.commonmark.node.Heading)
            return Optional.of(new Heading(parseText(node.getFirstChild()), ((org.commonmark.node.Heading) node).getLevel()));
        if (node instanceof ThematicBreak)
            return Optional.of(new HorizontalRow());
        if (node instanceof Paragraph)
            return Optional.of(new TextParagraph(
                    parseText(node.getFirstChild())));
        if (node instanceof org.commonmark.node.BulletList)
            return Optional.of(new BulletList(parseParagraphList(node.getFirstChild())));
        if (node instanceof FencedCodeBlock)
            return Optional.of(new CodeBlock(
                    ((FencedCodeBlock) node).getLiteral(), ((FencedCodeBlock) node).getInfo()));
        if (node instanceof org.commonmark.node.BlockQuote)
            return Optional.of(new BlockQuote(parseParagraphList(node.getFirstChild())));
        if (node instanceof ListItem)
            return parseParagraph(node.getFirstChild());
        return Optional.empty();
    }

    /**
     * convert markdown to FormattedTextDocument
     */
    private TextFragment parseText(Node node) {
        List<TextFragment> result = new ArrayList<>();
        while (node != null) {
            if (node instanceof Text)
                result.add(new PlainTextFragment(((Text) node).getLiteral()));
            else if (node instanceof Emphasis)
                result.add(new EmphasisTextFragment(parseText(node.getFirstChild())));
            else if (node instanceof StrongEmphasis)
                result.add(new StrongEmphasisTextFragment(parseText(node.getFirstChild())));
            else if (node instanceof Image)
                result.add(new InlineImage(
                        ((Image) node).getDestination(), parseText(node.getFirstChild())));
            else if (node instanceof org.commonmark.node.Link)
                result.add(new Link(
                        ((org.commonmark.node.Link) node).getDestination(), parseText(node.getFirstChild())));
            node = node.getNext();
        }
        return TextFragmentSequence.create(result);
    }

    /**
     * identify and load metadata of image files
     */
    public void loadImage(@NotNull ProjectBuilder builder, File file) throws IOException, ProjectFormatException {
        assert file.exists();
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        LocalDateTime fileCreated = new WebGen().getDateTime(attr.creationTime());
        LocalDateTime fileLastUpdate = new WebGen().getDateTime(attr.lastModifiedTime());
        builder.foundImage(file, fileCreated, fileLastUpdate, attr.size());
    }

    /**
     * identify and load metadata of video files
     */
    public void loadVideo(@NotNull ProjectBuilder builder, File file) throws IOException, ProjectFormatException {
        assert file.exists();
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        LocalDateTime fileCreated = new WebGen().getDateTime(attr.creationTime());
        LocalDateTime fileLastUpdate = new WebGen().getDateTime(attr.lastModifiedTime());
        builder.foundVideo(file, fileCreated, fileLastUpdate, attr.size());
    }

    /**
     * youtube files are yaml files with a "id" pointing to the youtube id and optional metadata
     */
    private void loadYoutubeVideo(ProjectBuilder builder, File file) throws IOException, ProjectFormatException {
        assert file.exists();
        Map<String, String> m = parseMetadataFile(file);
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        LocalDateTime fileCreated = new WebGen().getDateTime(attr.creationTime());
        LocalDateTime fileLastUpdate = new WebGen().getDateTime(attr.lastModifiedTime());
        if (m.containsKey("id"))
            builder.foundYoutubeVideo(m.get("id"), m, fileCreated, fileLastUpdate, attr.size());
        else
            System.err.println("Youtube file does not contain id: " + file);
    }
}
