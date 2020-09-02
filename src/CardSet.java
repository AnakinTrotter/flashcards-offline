import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class CardSet {
    private ArrayList<Flashcard> persistent;
    private ArrayList<Flashcard> flashcards;
    private int length;

    public CardSet() {
        flashcards = new ArrayList<>();
        persistent = new ArrayList<>();
        // starred = new ArrayList<>();
        length = 0;
    }

    public CardSet(String path) {
        flashcards = generateCards(path);
        persistent = generateCards(path);
        // starred = new ArrayList<>();
        length = persistent.size();
    }

    public void shuffle() {
        Collections.shuffle(flashcards);
    }

    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    public ArrayList<Flashcard> getPersistentFlashcards() {
        return persistent;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<Flashcard> generateCards(String path) {
        ArrayList<Flashcard> result = new ArrayList<>();
        String text = "";
        try {
            text = readFile(Paths.get(path).toString(), Charset.defaultCharset());
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        StringTokenizer st = new StringTokenizer(text, "\n\r,");
        while(st.hasMoreTokens()) {
            String t1 = st.nextToken();
            String t2 = st.nextToken();
            result.add(new Flashcard(t1, t2));
        }
        return result;
    }

    public String toString() {
        return flashcards.toString();
    }

    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
