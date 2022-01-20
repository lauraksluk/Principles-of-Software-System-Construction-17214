package edu.cmu.cs214.hw1.ordering;

import edu.cmu.cs214.hw1.cards.CardStatus;

import java.util.List;

/**
 *
 */
public interface CardOrganizer {

    /**
     * Orders, and potentially filters, the provided cards.
     *
     * @param cards The {@link CardStatus} objects to order.
     * @return The provided cards, sorted and/or filtered based on the implementing class.
     */
    List<CardStatus> reorganize(List<CardStatus> cards);

}
