package edu.cmu.cs214.hw1.ordering.prioritization;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.ordering.CardOrganizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardShuffler implements CardOrganizer {

    /**
     * Randomly shuffles the provided cards.
     *
     * @param cards The {@link CardStatus} objects to order.
     * @return The shuffled cards.
     */
    @Override
    public List<CardStatus> reorganize(List<CardStatus> cards) {
        List<CardStatus> cardsList = new ArrayList<>(cards);
        Collections.shuffle(cardsList);
        return cardsList;
    }
}
