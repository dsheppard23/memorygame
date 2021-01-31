import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.*;

public class Board
{
    // Array to hold board cards
    private FlippableCard cards[];

    // Resource loader
    private ClassLoader loader = getClass().getClassLoader();

    public Board(int size, ActionListener AL)
    {
        // Allocate and configure the game board: an array of cards
        // Leave one extra space for the "happy face", added at the end
        cards = new FlippableCard[size];

        // Fill the Cards array
        int imageIdx = 1;
        for (int i = 0; i < (size-1); i += 2) {

            // Load the front image from the resources folder
            String imgPath = "res/hub" + imageIdx + ".jpg";
            ImageIcon img = new ImageIcon(loader.getResource(imgPath));
            imageIdx++;  // get ready for the next pair of cards

            // Setup two cards at a time
            FlippableCard c1 = new FlippableCard(img);
            FlippableCard c2 = new FlippableCard(img);

            // Add them to the array
            cards[i] = c1;
            cards[i + 1] = c2;

            cards[i].setFrontImage(img);
            cards[i+1].setFrontImage(img);

            cards[i].addActionListener(AL);
            cards[i+1].addActionListener(AL);

            cards[i].setID(i);
            cards[i+1].setID(i);

        }
        // Add the "happy face" image
        String imgPath = "res/happy-face.jpg";
        ImageIcon img = new ImageIcon(loader.getResource(imgPath));
        FlippableCard c1 = new FlippableCard(img);
        cards[size-1] = c1;

        cards[size - 1].addActionListener(AL);

        //Set this ID to a number not in the range, therefore we can use it later to verify the happy face
        cards[size - 1].setID(55);

        //Randomize the card positions
        Collections.shuffle(Arrays.asList(cards));
    }

    public void fillBoardView(JPanel view)
    {
        for (FlippableCard c : cards) {
            view.add(c);
        }
    }

    public void resetBoard()
    {
        //this will hide the pictures and display the grey wall looking picture
        for(int i = 0; i < 25; i++)
        {
            cards[i].hideFront();
        }

        //Randomize the card positions
        Collections.shuffle(Arrays.asList(cards));
    }
}
