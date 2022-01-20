package edu.cmu.cs214.hw1.data;

import edu.cmu.cs214.hw1.cards.FlashCard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements a card store in working memory, based on a {@link HashSet}. The data is initialized with a pre-populated
 * set of default cards.
 */
public final class InMemoryCardStore implements CardStore {


    private final Set<FlashCard> cards;

    /**
     * Initializes a new {@link InMemoryCardStore} instance, which contains the provided cards
     *
     * @param initialCards Provided cards
     */
    public InMemoryCardStore(Collection<FlashCard> initialCards) {
        cards = new HashSet<>(initialCards);
    }

    /**
     * Initializes a new empty {@link InMemoryCardStore} instance.
     */
    public InMemoryCardStore() {
        cards = new HashSet<>();
    }

    /**
     * Creates and returns a new {@link InMemoryCardStore} where question and answer are swapped on all cards
     *
     * @return The new, reversed {@link InMemoryCardStore}.
     */
    public InMemoryCardStore invertQuestions() {
        var newCards = cards.stream().map(c -> new FlashCard(c.answer(), c.question())).collect(Collectors.toSet());
        return new InMemoryCardStore(newCards);
    }

    @Override
    public Collection<FlashCard> getAllCards() {
        return this.cards;
    }

    @Override
    public boolean addCard(FlashCard card) {
        return this.cards.add(card);
    }

    @Override
    public boolean removeCard(FlashCard card) {
        return this.cards.remove(card);
    }

}
