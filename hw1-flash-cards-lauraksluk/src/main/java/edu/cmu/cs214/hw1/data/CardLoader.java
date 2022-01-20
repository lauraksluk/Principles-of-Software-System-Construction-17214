package edu.cmu.cs214.hw1.data;

import edu.cmu.cs214.hw1.cards.FlashCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads cards from a provided file.
 * The file contains card content, one in each line. Each line is split into back (answer)
 * and front (question) of the card by the first "--" in the line.
 */
public class CardLoader {

    public CardStore loadCardsFromFile(File cardsFile, boolean flag) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cardsFile));
        try {
            List<FlashCard> result = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null)
                if (line.contains("--"))
                    result.add(new FlashCard(
                            line.substring(line.indexOf("--") + 2).trim(),
                            line.substring(0, line.indexOf("--")).trim()
                    ));
            if (flag) {
                return new InMemoryCardStore(result).invertQuestions();
            }
            return new InMemoryCardStore(result);
        } finally {
            reader.close();
        }

    }

}
