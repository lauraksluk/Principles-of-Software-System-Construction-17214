package edu.cmu.webgen;

import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Parsing and abstracting of command-line options
 */
public class WebGenArgs {

    final CommandLine cmd;
    private final Options options;
    private final OptionGroup sortingOptions;

    public WebGenArgs(String[] arguments) throws ParseException {
        this.options = new Options();
        this.options.addOption("d", "directory", true, "root directory for the project");
        this.options.addOption("o", "output", true, "target directory for the generated html files");
        this.options.addOption("dr", "dry-run", false, "process without rendering");
        this.options.addOption("l", "list-articles", false, "print all articles in this project");
        this.options.addOption("e", "list-events", false, "print all events in this project");
        this.sortingOptions = new OptionGroup();
        this.sortingOptions.addOption(Option.builder().longOpt("sort-pinned-first").desc("Show pinned articles first, then sort by publish date (default)").build());
        this.sortingOptions.addOption(Option.builder().longOpt("sort-published").desc("Sort by publication date, newest first").build());
        this.sortingOptions.addOption(Option.builder().longOpt("sort-published-first").desc("Sort by publication date, oldest first").build());
        this.sortingOptions.addOption(Option.builder().longOpt("sort-edited").desc("Sort by date of last edit of the source file").build());
        this.options.addOptionGroup(this.sortingOptions);
        this.options.addOption("a", "all", false, "print also inner articles or events (only in combination with --list-*)");
        this.options.addOption("c", "clean", false, "clean target directory before generating new page");
        this.options.addOption("s", "size", false, "print the size of the entire project");
        this.options.addOption("t", "topics", false, "print topics of articles or events");
        this.options.addOption("h", "help", false, "print this help message");
        DefaultParser parser = new DefaultParser();
        this.cmd = parser.parse(this.options, arguments);
    }

    void printHelp() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("webgen", this.options);
    }

    public boolean isListArticles() {
        return this.cmd.hasOption("list-articles");
    }

    public boolean isListEvents() {
        return this.cmd.hasOption("list-events");
    }

    public boolean isListAll() {
        return this.cmd.hasOption("all");
    }

    public boolean isListTopics() {
        return this.cmd.hasOption("topics");
    }

    public ArticleSorting getArticleSorting() {
        if (this.cmd.hasOption("sort-published")) return ArticleSorting.PUBLISHED_LAST;
        if (this.cmd.hasOption("sort-published-first")) return ArticleSorting.PUBLISHED_FIRST;
        if (this.cmd.hasOption("sort-edited")) return ArticleSorting.EDITED;
        return ArticleSorting.PINNED;
    }

    @NotNull
    public File getTargetDirectory() {
        return this.cmd.hasOption("o") ? new File(this.cmd.getOptionValue("o")) : new File("_static");
    }

    public File getProjectSourceDirectory() {
        return this.cmd.hasOption("d") ? new File(this.cmd.getOptionValue("d")) : new File("input");
    }

    public boolean projectSourceDirectoryExists() {
        return getProjectSourceDirectory().exists();
    }

    public boolean isHelp() {
        return this.cmd.hasOption("h");
    }

    public boolean isRender() {
        return !this.cmd.hasOption("dry-run");
    }

    public boolean cleanTargetDirectory() {
        return this.cmd.hasOption("clean");
    }

    public boolean printSize() {
        return this.cmd.hasOption("size");
    }

    public enum ArticleSorting {PINNED, PUBLISHED_LAST, PUBLISHED_FIRST, EDITED}
}
