package edu.cmu.cs214.hw1.ordering;

import edu.cmu.cs214.hw1.cards.CardStatus;

import java.util.List;

/**
 * A <b>composite</b> class that wraps other {@link CardOrganizer} instances and a list of {@link CardStatus} cards.
 */
public class MultiOrganizer implements CardOrganizer {
    private final List<CardOrganizer> cardOrganizers;

    /**
     * Initializes a {@link MultiOrganizer} that wraps a number of {@link CardOrganizer} instances.
     *
     * @param cardOrganizers The {@link CardOrganizer} instances to compose into this instance.
     */
    public MultiOrganizer(List<CardOrganizer> cardOrganizers) {
        this.cardOrganizers = cardOrganizers;
    }

    /**
     * Applies each {@link CardOrganizer} instance to the provided collection of cards. This method makes no guarantees
     * about the order in which the underlying sorters are invoked; the final order may be dependent on this order when
     * conflicting priorities are involved.
     *
     * @param cards The {@link CardStatus} objects to order.
     * @return The final, filtered and ordered list of cards.
     */
    @Override
    public List<CardStatus> reorganize(List<CardStatus> cards) {
        for (CardOrganizer cardOrganizer : this.cardOrganizers) {
            cards = cardOrganizer.reorganize(cards);
        }
        return cards;
    }
}
