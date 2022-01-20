package edu.cmu.cs214.hw1.ordering.repetition;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.cards.FlashCard;

/**
 * Ensures that every {@link FlashCard} is answered correctly a given number of times.
 */
public class RepeatingCardOrganizer implements CardRepeater {

    private final int repetitions;

    /**
     * Creates a RepeatingCardSorter instance.
     *
     * @param repetitions The number of repetitions to require of each card. Must be positive.
     */
    public RepeatingCardOrganizer(int repetitions) {
        assert repetitions >= 1;
        this.repetitions = repetitions;
    }

    /**
     * Checks if the provided card has been answered correctly the required number of times.
     *
     * @param card The {@link CardStatus} object to check.
     * @return {@code true} if this card has been answered correctly at least {@code this.repetitions} times.
     */
    @Override
    public boolean isComplete(CardStatus card) {
        return card.getResults().stream().filter(isSuccess -> isSuccess).count() >= this.repetitions;
    }
}
