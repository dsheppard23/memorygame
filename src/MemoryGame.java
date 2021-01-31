import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

public class MemoryGame extends JFrame implements ActionListener
{
    // Core game play objects
    private Board gameBoard;
    private FlippableCard prevCard1, prevCard2;

    // Labels to display game info
    private JLabel errorLabel, timerLabel;

    // layout objects: Views of the board and the label area
    private JPanel boardView, labelView;

    // Record keeping counts and times
    private int clickCount = 0, gameTime = 0, errorCount = 0;
    private int pairsFound = 0;

    // Game timer: will be configured to trigger an event every second
    private Timer gameTimer;

    public MemoryGame()
    {
        // Call the base class constructor
        super("Hubble Memory Game");

        // Allocate the interface elements
        JButton restart = new JButton("Play");
        JButton quit = new JButton("Quit");
        timerLabel = new JLabel("Matches: 0");
        errorLabel = new JLabel("Errors: 0");

        /*
         * To-Do: Setup the interface objects (timer, error counter, buttons)
         * and any event handling they need to perform
         */
        gameTimer = new Timer();
        prevCard1 = null;
        prevCard2 = null;


        //quit Action listener
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //restart Action listener
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //call restartGame function and revalidate
                restart.setText("Restart");
                timerLabel.setText("Matches: 0");    //a timerLabel is made for us, but we will use it for guesses. Later it will be used for the time
                errorLabel.setText("Errors: 0");
                restartGame();
                revalidate();

                //call time method
                time();
            }
        });

        // Allocate two major panels to hold interface
        labelView = new JPanel();  // used to hold labels
        boardView = new JPanel();  // used to hold game board

        // get the content pane, onto which everything is eventually added
        Container c = getContentPane();

        // Setup the game board with cards
        gameBoard = new Board(25, this);

        // Add the game board to the board layout area
        boardView.setLayout(new GridLayout(5, 5, 2, 0));
        gameBoard.fillBoardView(boardView);

        // Add required interface elements to the "label" JPanel
        labelView.setLayout(new GridLayout(1, 4, 2, 2));
        labelView.add(quit);
        labelView.add(restart);
        labelView.add(timerLabel);
        labelView.add(errorLabel);

        // Both panels should now be individually layed out
        // Add both panels to the container
        c.add(labelView, BorderLayout.NORTH);
        c.add(boardView, BorderLayout.SOUTH);

        setSize(745, 500);
        setVisible(true);
    }

    /* Handle anything that gets clicked and that uses MemoryGame as an
     * ActionListener */
    public void actionPerformed(ActionEvent e)
    {
        // Get the currently clicked card from a click event
        FlippableCard currCard = (FlippableCard)e.getSource();

        //checks if the two cards are null and if they are the same pic
        //if both are false the previous cards will be hidden again
        if ((prevCard1 != null && prevCard2 != null) && (prevCard1.id() != prevCard2.id()))
        {
            prevCard1.hideFront();
            prevCard2.hideFront();
            prevCard1 = null;
            prevCard2 = null;
        }
        else if(prevCard1 != null && prevCard1.id() == 55)  //if happy face is found first do nothing
        {
            prevCard1.hideFront();
            prevCard1 = null;
        }

        //increment click count
        clickCount++;


        //if statements to set the cards to proper prevCards and show them
        if(prevCard1 == null)
        {
            prevCard1 = currCard;
            prevCard1.showFront();
        }
        else if(prevCard2 == null)
        {
            prevCard2 = currCard;
            prevCard2.showFront();
        }

        //if prevCard1 and 2 are not null
        if (prevCard1 != null && prevCard2 != null)
        {

            if(prevCard1.id() == prevCard2.id())  //checks the ID to see if they are the same picture, increments pairsFound
            {
                pairsFound++;
                timerLabel.setText("Matches: " + pairsFound);
                prevCard1 = null;
                prevCard2 = null;

            }
            else
            {
                if(prevCard2.id() == 55) //if the happy face is found second
                {
                    //do nothing as error count will NOT be incremented
                }
                else {
                    errorCount++;
                    errorLabel.setText("Errors: " + errorCount);
                }
            }

            if(pairsFound == 12)    //winner statements
            {
                int finishTime = gameTime;
                gameTimer.cancel();
                repaint();
                timerLabel.setText("Winner! It took you " + finishTime + " seconds.");
            }

        }



    }

    private void restartGame()
    {
        pairsFound = 0;
        gameTime = 0;
        clickCount = 0;
        errorCount = 0;
        timerLabel.setText("Matches: 0");
        errorLabel.setText("Errors: 0");

        // Clear the boardView and have the gameBoard generate a new layout
        boardView.removeAll();
        gameBoard.resetBoard();
        gameBoard.fillBoardView(boardView);
    }

    private void time()
    {
        //Timer
        //start the timer
        gameTimer.cancel();
        gameTimer = new Timer();

        TimerTask time = new TimerTask() {
            @Override
            public void run() {
                gameTime++;
            }
        };

        gameTimer.scheduleAtFixedRate(time, 1000, 1000);
    }

    public static void main(String args[])
    {
        MemoryGame M = new MemoryGame();
        M.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });
    }


}
