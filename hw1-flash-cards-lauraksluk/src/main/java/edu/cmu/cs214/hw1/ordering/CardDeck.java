package edu.cmu.cs214.hw1.ordering;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.cards.FlashCard;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A card deck represents a set of cards in a specific order with associated state and a mechanism to filter and reorder
 * them.
 */
public class CardDeck {

    private final CardOrganizer cardOrganizer;
    private List<CardStatus> cards;

    /**
     * Initializes a new {@link CardDeck} instance.
     *
     * @param cards         The {@link FlashCard} cards to store in this deck, to be paired with a status into
     *                      a {@link CardStatus}.
     * @param cardOrganizer The (potentially composite) {@link CardOrganizer} instance to sort and filter the cards with
     *                      based on the correctness of responses.
     */
    public CardDeck(Collection<FlashCard> cards, CardOrganizer cardOrganizer) {
        assert cardOrganizer != null;
        this.cardOrganizer = cardOrganizer;
        this.cards = cards.stream().map(CardStatus::new).collect(Collectors.toList());
    }

    /**
     * Retrieves the remaining stored cards.
     *
     * @return The {@link CardStatus} cards in this deck.
     */
    public List<CardStatus> getCards() {
        return this.cards;
    }

    /**
     * Retrieves the associated card organizer.
     *
     * @return The {@link CardOrganizer} used to sort this deck.
     */
    public CardOrganizer getCardSorter() {
        return this.cardOrganizer;
    }

    /**
     * A helper method, that calls {@link CardOrganizer#reorganize(List)} with the global cards field and overwrites
     * it with the result.
     *
     * @return The final, filtered and ordered list of cards.
     */
    public List<CardStatus> reorganize() {
        this.cards = cardOrganizer.reorganize(this.cards);
        return this.cards;
    }

    /**
     * Checks whether any more cards need to be tested.
     *
     * @return {@code true} if all cards have been filtered.
     */
    public boolean isComplete() {
        return this.cards.isEmpty();
    }

    /**
     * Gets the size of the remaining cards in this deck.
     *
     * @return The number of cards in this deck.
     */
    public int countCards() {
        return cards.size();
    }
}
