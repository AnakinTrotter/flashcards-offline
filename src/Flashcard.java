/**
 * @author Anakin Trotter
 * @version 1.0
 * @since 9/1/2020
 */

public class Flashcard {
    // Object to serve as the list item in CardSet
    private String front = "";
    private String back = "";

    /**
     * Assigns the Strings for the front and back of the flashcard
     * @param front the text on the front of the flashcard
     * @param back  the text on the back of the flashcard
     */
    public Flashcard(String front, String back) {
        this.front = front;
        this.back = back;
    }

    /**
     * Getter method for front
     * @return  the text on the front of the flashcard
     */
    public String getFront() {
        return front;
    }

    /**
     * Setter method for front
     * @param front the text to put on the front of the flashcard
     */
    public void setFront(String front) {
        this.front = front;
    }

    /**
     * Getter method for back
     * @return  the text on the back of the flashcard
     */
    public String getBack() {
        return back;
    }

    /**
     * Setter method for back
     * @param back  the text to put on the back of the flashcard
     */
    public void setBack(String back) {
        this.back = back;
    }

    /**
     * Overrides the default toString method
     * @return  the text on the front and back of the flashcard as one String
     */
    public String toString() {
        return "Front: " + front + "\tBack: " + back;
    }
}
