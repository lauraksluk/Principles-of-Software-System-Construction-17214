package edu.cmu.cs214.hw1.cards;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class CardStatus {
    private final FlashCard card;
    private final List<Boolean> successes;

    /**
     * Creates a new {@link CardStatus} instance.
     *
     * @param card The {@link FlashCard} card to track answer correctness for.
     */
    public CardStatus(FlashCard card) {
        this.card = card;
        this.successes = new ArrayList<>();
    }

    /**
     * Retrieves the {@link edu.cmu.cs214.hw1.cards.FlashCard} associated with this {@code CardStatus}.
     *
     * @return The associated {@link edu.cmu.cs214.hw1.cards.FlashCard}.
     */
    public FlashCard getCard() {
        return card;
    }

    /**
     * Retrieves the record of past successes at answering this card.
     *
     * @return A list of boolean's indicating the recorded outcome of previous attempts to answer this card.
     */
    public List<Boolean> getResults() {
        return this.successes;
    }

    /**
     * Updates the internal success tracker with a new answering outcome.
     *
     * @param success {@code true} if this card was answered correctly.
     */
    public void recordResult(boolean success) {
        this.successes.add(success);
    }

    /**
     * Resets the record of past answering outcomes.
     */
    public void clearResults() {
        this.successes.clear();
    }

    @Override
    public String toString() {
        return MessageFormat.format("CardStatus[card={0}, status={1}]", card, successes);
    }
}
