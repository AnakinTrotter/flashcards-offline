import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class CardSet {
    private LinkedList<Flashcard> flashcards;

    public CardSet() {
        flashcards = new LinkedList<>();
    }

    public CardSet(String path) {
        flashcards = generateCards(path);
    }

    public LinkedList<Flashcard> generateCards(String path) {
        LinkedList<Flashcard> result = new LinkedList<>();
        String text = "";
        try {
            text = Files.readString(Paths.get(path));
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        StringTokenizer st = new StringTokenizer(text, "\n\r,");
        while(st.hasMoreTokens()) {
            Flashcard fc = new Flashcard(st.nextToken(), st.nextToken());
            System.out.println(fc);
            flashcards.add(fc);
        }
        System.out.println(st.toString());
        System.out.println(flashcards);
        return null;
    }
}
