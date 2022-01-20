package edu.cmu.cs214.hw1.ordering.repetition;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.ordering.CardOrganizer;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public interface CardRepeater extends CardOrganizer {

    /**
     * Drops all cards that require no more repetition.
     *
     * @param cards The {@link CardStatus} objects to order.
     * @return The filtered cards, in no particular order.
     */
    default List<CardStatus> reorganize(List<CardStatus> cards) {
        return cards.stream().filter(card -> !isComplete(card)).collect(Collectors.toList());
    }

    /**
     * Checks whether the provided card no longer needs to be repeated. If {@code true}, it will be filtered from the
     * {@link CardRepeater#reorganize(List)} process above.
     *
     * @param card The {@link CardStatus} object to check.
     * @return Whether the user has completed studying this card.
     */
    boolean isComplete(CardStatus card);

}
