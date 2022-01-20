package edu.cmu.cs214.hw1.data;

import edu.cmu.cs214.hw1.cards.FlashCard;

import java.util.Collection;

public interface CardStore {

    /**
     * Retrieves the full set of cards in this store, in no prescribed order.
     *
     * @return all cards stored in this dataset.
     */
    Collection<FlashCard> getAllCards();

    /**
     * Adds a card to the dataset. If an identical card is present, returns false and does not add this card.
     *
     * @param card The {@link FlashCard} to add.
     * @return {@code false}, it this card already present in the data store, in which case it has not been added.
     */
    boolean addCard(FlashCard card);

    /**
     * Removes this card from the data store, if it is present.
     *
     * @param card The {@link FlashCard} to remove.
     * @return A boolean, indicating whether this card was present in the data store, in which case it has been removed.
     */
    boolean removeCard(FlashCard card);

}
