import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author Anakin Trotter
 * @version 1.0
 * @since 9/1/2020
 */

public class GUI extends JFrame {
    private CardSet flashcards;
    private FlashcardPanel flashcard;
    private JMenuItem shuffleItem;

    /**
     * Creates a JFrame then fills it with JPanels and a JMenubar
     * @param width width of the JFrame
     * @param height    height of the JFrame
     * @param title title of the JFrame to be displayed on top of the window
     * @param flashcards    the CardSet to use and display
     */
    public GUI(int width, int height, String title, CardSet flashcards) {
        super(title);
        // makes the GUI look more clean (like the regular Windows file explorer)
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.flashcards = flashcards;
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centers the JFrame

        flashcard = new FlashcardPanel();
        add(flashcard);

        JMenuBar menuBar = new JMenuBar();
        JMenuItem openItem;
        JMenuItem keyHelpItem;
        JMenuItem makeHelpItem;

        JMenu fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new OpenListener());
        fileMenu.add(openItem);
        menuBar.add(fileMenu);

        JMenu optionMenu = new JMenu("Edit");
        shuffleItem = new JMenuItem("Shuffle");
        shuffleItem.addActionListener(new ShuffleListener());
        optionMenu.add(shuffleItem);
        menuBar.add(optionMenu);

        JMenu helpMenu = new JMenu("Help");
        keyHelpItem = new JMenuItem("Key Bindings");
        makeHelpItem = new JMenuItem("Creating a set");
        HelpListener hl = new HelpListener();
        makeHelpItem.addActionListener(hl);
        keyHelpItem.addActionListener(hl);
        helpMenu.add(keyHelpItem);
        helpMenu.add(makeHelpItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);


        setVisible(true);
    }

    /**
     * Opens a popup dialog menu to give the user information about something
     * @param infoMessage   the text that should go in the message box
     * @param titleBar  the text that should display in the title bar of the message box
     */
    private void showInfoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Listens for clicks to the Help Menu and then displays the correct pop up message
     */
    private class HelpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()) {
                case "Key Bindings":
                    showInfoBox("Left arrow key: go back one card\nRight arrow key: go forward one card\n" +
                            "Up/down arrow key or space: flip card over\ns: shuffle cards", "Keyboard Commands");
                    break;
                case "Creating a set":
                    showInfoBox("1) Create a new text document (notepad is fine)\n" +
                            "2) Type your terms and definitions separated by '|'. For example:\n" +
                            "\n        hi| used as a friendly greeting or to attract attention.\n" +
                            "\n        bye| one or more holes remaining unplayed after a match has been decided.\n" +
                            "\n3) Save your text file then open it through the file menu.",
                            "How to create your own flashcards");
                    break;
            }
        }
    }

    /**
     * Listens for clicks to the Open Menu and then prompts the user to select a file to open
     */
    private class OpenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jfc = new JFileChooser();
            int response = jfc.showOpenDialog(flashcard);
            if(response == JFileChooser.APPROVE_OPTION) {
                flashcards = new CardSet(jfc.getSelectedFile().getAbsolutePath());
                flashcard.updateCards();
            }
        }
    }

    /**
     * Listens for clicks to the shuffle button to toggle between shuffled and un-shuffled flashcards
     */
    private class ShuffleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(flashcard.shuffled) {
                flashcard.shuffleCards();
            } else {
                flashcard.shuffleCards();
            }
        }
    }

    /**
     * The JPanel that contains basically everything.
     * The class is protected so that the class it is embedded in can access its fields.
     */
    protected class FlashcardPanel extends JPanel {
        private JTextPane text;
        private int currentCard;
        private int currentCardPersist;
        private Font font;
        private boolean onFront;
        private boolean shuffled;

        public FlashcardPanel() {
            onFront = true;
            shuffled = false;
            currentCard = 1;
            font = new Font("Arial", Font.PLAIN, 32);
            setBorder(BorderFactory.createEmptyBorder(200,100,100,100));
            text = new JTextPane();
            configureKeyBinding();

            SimpleAttributeSet as  = new SimpleAttributeSet();
            StyledDocument doc = text.getStyledDocument();
            StyleConstants.setAlignment(as, StyleConstants.ALIGN_CENTER);
            StyleConstants.setFontSize(as, 32);
            StyleConstants.setBold(as, true);
            doc.setParagraphAttributes(0, doc.getLength(), as, false);
            text.setBackground(Color.lightGray);

            text.setEditable(false);
            text.setFocusable(false);
            text.setPreferredSize(new Dimension(500, 300));
            add(text);
            setVisible(true);
            setFocusable(true);

            try {
                doc.insertString(doc.getLength(), "⬉ Click 'File' to get started!", as);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        /**
         * Displays from the regular list if shuffled, and the persistent list if not shuffled.
         * Displays either the text on the front of back of the flashcard depending on what side
         * the user is looking at.
         */
        public void updateCards() {
            if(onFront) {
                if(shuffled)
                    text.setText(flashcards.getFlashcards().get(currentCard - 1).getFront());
                else
                    text.setText(flashcards.getPersistentFlashcards().get(currentCard - 1).getFront());
            } else {
                if(shuffled)
                    text.setText(flashcards.getFlashcards().get(currentCard - 1).getBack());
                else
                    text.setText(flashcards.getPersistentFlashcards().get(currentCard - 1).getBack());
            }
            repaint();
        }

        /**
         * Toggles the shuffle option and saves the user's position in the CardSet
         * so that they can pick up where they left off if they un-shuffle.
         */
        public void shuffleCards() {
            if(flashcards.getLength() == 0) return;
            if (shuffled) {
                shuffled = false;
                currentCard = currentCardPersist;
                shuffleItem.setText("Shuffle");
            } else {
                flashcards.shuffle();
                shuffled = true;
                currentCardPersist = currentCard;
                currentCard = 1;
                shuffleItem.setText("Un-shuffle");
            }
            updateCards();
        }

        /**
         * Draws the flashcard shaped background and the progress counter on the top left
         * @param g the Graphics object as provided by Java
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(font);
            g.drawString(currentCard + "/" + flashcards.getLength(), 0, 32);
            g.setColor(Color.lightGray);
            g.fillRoundRect(80, 55, 800, 550, 20, 20);
        }

        /**
         * Sets key bindings using the InputMap and ActionMap rather than KeyListener
         * so that the user's key inputs don't fail to register because of focus issues.
         */
        private void configureKeyBinding() {
            ActionMap actionMap = getActionMap();
            int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
            InputMap inputMap = getInputMap(condition );

            String vkLeft = "VK_LEFT";
            String vkRight = "VK_RIGHT";
            String vkSpace = "VK_SPACE";
            String vkS = "VK_S";
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), vkLeft);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), vkRight);

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), vkSpace);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), vkSpace);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), vkSpace);
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), vkS);

            actionMap.put(vkLeft, new KeyAction(vkLeft));
            actionMap.put(vkRight, new KeyAction(vkRight));
            actionMap.put(vkSpace, new KeyAction(vkSpace));
            actionMap.put(vkS, new KeyAction(vkS));
        }

        /**
         * Listens for keyboard inputs from the user then executes through a switch
         * the operation associated with that key binding
         */
        private class KeyAction extends AbstractAction {
            public KeyAction(String actionCommand) {
                putValue(ACTION_COMMAND_KEY, actionCommand);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if(flashcards.getLength() == 0) return;
                switch(e.getActionCommand()) {
                    case "VK_LEFT":
                        if (currentCard > 1) {
                            currentCard--;
                            updateCards();
                        }
                        break;
                    case "VK_RIGHT":
                        if (currentCard < flashcards.getLength()) {
                            currentCard++;
                            updateCards();
                        }
                        break;
                    case "VK_SPACE":
                        onFront = !onFront;
                        updateCards();
                        break;
                    case "VK_S":
                        shuffleCards();
                        break;
                }
            }

        }
    }

    /**
     * Creates a new GUI and passes it an empty CardSet
     * @param args  arguments as provided by Java
     */
    public static void main(String[] args) {
        GUI gui = new GUI(970, 720, "Flashcard Maker", new CardSet());
    }

}
