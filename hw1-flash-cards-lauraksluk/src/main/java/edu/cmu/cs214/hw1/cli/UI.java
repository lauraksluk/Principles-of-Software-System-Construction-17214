package edu.cmu.cs214.hw1.cli;

import edu.cmu.cs214.hw1.cards.CardStatus;
import edu.cmu.cs214.hw1.cards.FlashCard;
import edu.cmu.cs214.hw1.ordering.CardDeck;

import java.util.Scanner;

public class UI {


    /**
     * Prompts the user with {@link FlashCard} cards until the {@link CardDeck} is exhausted.
     *
     * @param cardProducer The {@link CardDeck} to use for organizing cards.
     */
    public void studyCards(CardDeck cardProducer) {
        try (Scanner sc = new Scanner(System.in)) {
            while (!cardProducer.isComplete()) {
                cueAllCards(cardProducer, sc);
                cardProducer.reorganize();
                System.out.println("Pass completed; " + cardProducer.countCards() + " cards left.");
            }
            System.out.println("Great job, you have studied all the cards!");
        }
    }

    private void cueAllCards(CardDeck cardProducer, Scanner sc) {
        for (CardStatus cardStatus : cardProducer.getCards()) {
            FlashCard card = cardStatus.getCard();
            boolean success = cueCard(card, sc);
            cardStatus.recordResult(success);
        }
    }

    private boolean cueCard(FlashCard card, Scanner sc) {
        System.out.println("Next cue: " + card.question());
        var response = sc.nextLine().trim();
        var success = card.checkSuccess(response);
        if (success) {
            System.out.println("That's correct!");
        } else {
            System.out.println("That is incorrect; the correct response was: "
                    + card.answer());
        }
        return success;
    }


}
