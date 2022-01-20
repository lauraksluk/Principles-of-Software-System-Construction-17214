package edu.cmu.cs214.hw1.ordering.prioritization;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.ordering.CardOrganizer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MostMistakesFirstSorter implements CardOrganizer {

    /**
     * Orders the cards by the number of incorrect answers provided for them.
     *
     * @param cards The {@link CardStatus} objects to order.
     * @return The ordered cards.
     */
    @Override
    public List<CardStatus> reorganize(List<CardStatus> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(this::numberOfFailures).reversed())
                .collect(Collectors.toList());
    }

    private int numberOfFailures(CardStatus cardStatus) {
        return (int) cardStatus.getResults().stream().filter(s -> !s).count();
    }
}
