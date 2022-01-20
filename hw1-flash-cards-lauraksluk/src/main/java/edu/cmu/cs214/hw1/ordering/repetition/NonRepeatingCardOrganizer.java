package edu.cmu.cs214.hw1.ordering.repetition;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.cards.FlashCard;

/**
 * Returns every {@link FlashCard} exactly once, regardless of the correctness of the response.
 */
public class NonRepeatingCardOrganizer implements CardRepeater {

    /**
     * Checks if the provided card has not yet been answered, correctly or otherwise.
     *
     * @param card The {@link CardStatus} object to check.
     * @return {@code true} if this card has received one or more answers.
     */
    @Override
    public boolean isComplete(CardStatus card) {
        // Completion is marked by having been answered at least once, regardless of the correctness of that response.
        return !card.getResults().isEmpty();
    }
}
