package edu.cmu.cs214.hw1;

import edu.cmu.cs214.hw1.cli.UI;
import edu.cmu.cs214.hw1.data.CardLoader;
import edu.cmu.cs214.hw1.data.CardStore;
import edu.cmu.cs214.hw1.ordering.CardDeck;
import edu.cmu.cs214.hw1.ordering.CardOrganizer;
import edu.cmu.cs214.hw1.ordering.MultiOrganizer;
import edu.cmu.cs214.hw1.ordering.prioritization.CardShuffler;
import edu.cmu.cs214.hw1.ordering.prioritization.MostMistakesFirstSorter;
import edu.cmu.cs214.hw1.ordering.prioritization.RecentMistakesFirstSorter;
import edu.cmu.cs214.hw1.ordering.repetition.NonRepeatingCardOrganizer;
import edu.cmu.cs214.hw1.ordering.repetition.RepeatingCardOrganizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

/**
 * Main class to start the flashcard program. Includes options for
 * command line inputs.
 */
public final class Main {

    private Main() {
        // Disable instantiating this class.
        throw new UnsupportedOperationException();
    }

    /**
     * Private method to create all options for program.
     * @return Options type with all opts.
     */
    private static Options createOptions() {
        Option help = new Option("h", "help", false, "Show this help");
        Option version = new Option("v","version", false,"Show version number");
        Option order = Option.builder("o").longOpt("order")
                .hasArg()
                .desc("The type of ordering to use, default = random\n" +
                        "choices = [random, worst-first, last-mistakes-first]").build();
        Option rep = Option.builder("r").longOpt("repetitions")
                .hasArg()
                .desc("The number of times to each card should be answered " +
                        "successfully. If not given, every card presented once.").build();
        Option learn = Option.builder("l").longOpt("learn-titles")
                .desc("If set, prompts with the card's definition and asks the user " +
                        "to provide the corresponding title. Default = false").build();
        Options options = new Options();
        options.addOption(help);
        options.addOption(version);
        options.addOption(order);
        options.addOption(rep);
        options.addOption(learn);
        return options;
    }

    /**
     * Main method to start program.
     * @param args command line arguments
     * @throws IOException for invalid command line arguments
     */
    public static void main(String[] args) throws IOException {
        boolean flagCards = true;
        boolean flagTitles = false;

        CardStore cards;
        CardOrganizer ordP = new CardShuffler();
        CardOrganizer ordR = new NonRepeatingCardOrganizer();

        Options allOpts = createOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(allOpts, args);

            if (line.hasOption("order")) {
                String ordering = line.getOptionValue("order");
                if (ordering.equalsIgnoreCase("random")) {
                    ordP = new CardShuffler();
                }
                if (ordering.equalsIgnoreCase("worst-first")) {
                    ordP = new MostMistakesFirstSorter();
                }
                if (ordering.equalsIgnoreCase("last-mistakes-first")) {
                    ordP = new RecentMistakesFirstSorter();
                }
            }
            if (line.hasOption("learn-titles")) {
                flagTitles = true;
            }
            if (line.hasOption("repetitions")) {
                int reps = Integer.parseInt(line.getOptionValue("repetitions"));
                if (reps < 1) {
                    throw new IllegalArgumentException("repetitions must be positive");
                }
                ordR = new RepeatingCardOrganizer(reps);
            }
            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("flashcards", allOpts);
                flagCards = false;
            }
            if (line.hasOption("version")) {
                System.out.println("FlashCards \nVersion 1.0");
                flagCards = false;
            }

        } catch (org.apache.commons.cli.ParseException e) {
            flagCards = false;
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        }

        if (flagCards) {
            List<CardOrganizer> orgs = new ArrayList<>();
            orgs.add(ordP);
            orgs.add(ordR);
            CardOrganizer ord = new MultiOrganizer(orgs);
            if (!flagTitles) {
                cards = new CardLoader().loadCardsFromFile(new File(args[0]), true);
            } else {
                cards = new CardLoader().loadCardsFromFile(new File(args[0]), false);
            }
            CardDeck cardDeck = new CardDeck(cards.getAllCards(), ord);
            new UI().studyCards(cardDeck);
        }
    }

}
