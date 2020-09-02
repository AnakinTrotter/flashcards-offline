import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * @author Anakin Trotter
 * @version 1.0
 * @since 9/1/2020
 */

public class CardSet {
    private ArrayList<Flashcard> persistent;
    private ArrayList<Flashcard> flashcards;
    private int length;

    /**
     * Creates a persistent and regular list so that there will
     * always be a copy of the original list if the regular list
     * gets shuffled.
     * @param path  the path of the file to read
     */
    public CardSet(String path) {
        flashcards = generateCards(path);
        persistent = generateCards(path);
        length = persistent.size();
    }

    /**
     * Overloaded constructor that does not generate flashcards yet
     */
    public CardSet() {
        flashcards = new ArrayList<>();
        persistent = new ArrayList<>();
        length = 0;
    }

    /**
     * Shuffles the flashcards ArrayList using Collections.shuffle
     */
    public void shuffle() {
        Collections.shuffle(flashcards);
    }

    /**
     * Getter method for flashcards
     * @return  the flashcards ArrayList
     */
    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    /**
     * Getter method for persistent
     * @return  the original flashcard order as the persistent ArrayList
     */
    public ArrayList<Flashcard> getPersistentFlashcards() {
        return persistent;
    }

    /**
     * Getter method for length
     * @return  the number of flashcards in the set
     */
    public int getLength() {
        return length;
    }

    /**
     * Reads in a text file to populate a list of Flashcard objects
     * @param path  the path of the file to read
     * @return  a populated ArrayList of Flashcard objects
     */
    public ArrayList<Flashcard> generateCards(String path) {
        ArrayList<Flashcard> result = new ArrayList<>();
        String text = "";
        // attempt to read the file, aborts if an error occurs
        try {
            text = readFile(Paths.get(path).toString(), Charset.defaultCharset());
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // splits the string at each delimiter
        StringTokenizer st;
        if(text.contains("}")) {
            st = new StringTokenizer(text, "}");
        } else {
            st = new StringTokenizer(text, "\n\r|");
        }
        while(st.hasMoreTokens()) {
            // front of the card
            String t1 = st.nextToken();
            // back of the card
            String t2 = st.nextToken();
            // creating a new flashcard object with t1 and t2 as the front and back
            // then adding it to the list that will be returned
            result.add(new Flashcard(t1, t2));
        }
        return result;
    }

    /**
     * Overrides the default toString method
     * @return  the list of flashcards as a String
     */
    public String toString() {
        return flashcards.toString();
    }

    /**
     * Creates one long String from a file input source
     * @param path  the path of the file to read
     * @param encoding  the encoding to use (default depends on OS)
     * @return  a String containing the entire contents of the file
     * @throws IOException  if the file cannot be read
     */
    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
