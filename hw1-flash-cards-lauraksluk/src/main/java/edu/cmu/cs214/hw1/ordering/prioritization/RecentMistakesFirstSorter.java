package edu.cmu.cs214.hw1.ordering.prioritization;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.ordering.CardOrganizer;

import java.util.ArrayList;
import java.util.List;

public class RecentMistakesFirstSorter implements CardOrganizer {
    /**
     * Orders the provided cards such that the ones that were answered incorrectly
     * in the last round appear first. Reordering is stable.
     *
     * @param cards The {@link CardStatus} objects to order.
     * @return The provided cards, sorted.
     */
    @Override
    public List<CardStatus> reorganize(List<CardStatus> cards) {
        List<CardStatus> incorrect = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            List<Boolean> success = cards.get(i).getResults();
            if (success.size() > 0) {
                Boolean correct = success.get(success.size() - 1);
                if (!correct) {
                    incorrect.add(cards.get(i));
                }
            }
        }
        for (int k = incorrect.size() - 1; k >= 0; k--) {
            cards.remove(incorrect.get(k));
            cards.add(0, incorrect.get(k));
        }

        return cards;
    }

}
