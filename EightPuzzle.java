import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.util.*;
import javax.swing.border.LineBorder;

public class EightPuzzle implements ActionListener {
    JFrame frame;
    JMenuBar menuBar;
    JMenu options, help;
    JMenuItem newGame, exit, contact, rules, about;
    JLabel labNoOfMoves;
    JPanel buttonPanel;
    ImageIcon woodBack = new ImageIcon("assets/woodBack.png");
    int indexOfCurrentEmptyTile = -1;
    int noOfMoves = 0;
    JButton[] tiles = new JButton[9];

    public static void main(String[] args) {
        new EightPuzzle();
    }

    EightPuzzle() {
        frame = new JFrame("Sliding Puzzle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        initComponents();
        frame.setVisible(true);
    }

    private void initComponents() {
        menuBar = new JMenuBar();
        options = new JMenu("Options");
        help = new JMenu("Help");
        newGame = new JMenuItem("New Game");
        exit = new JMenuItem("Exit");
        contact = new JMenuItem("Contact");
        rules = new JMenuItem("Rules");
        about = new JMenuItem("About");

        newGame.addActionListener(this);
        exit.addActionListener(this);
        contact.addActionListener(this);
        rules.addActionListener(this);
        about.addActionListener(this);

        options.add(newGame);
        options.add(exit);
        help.add(contact);
        help.add(rules);
        help.add(about);

        menuBar.add(options);
        menuBar.add(help);

        frame.setJMenuBar(menuBar);

        labNoOfMoves = new JLabel("Moves: " + noOfMoves);
        labNoOfMoves.setFont(new Font("Arial", Font.BOLD, 24));
        labNoOfMoves.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(labNoOfMoves, BorderLayout.NORTH);

        buttonPanel = new JPanel(new GridLayout(3, 3));
        frame.add(buttonPanel, BorderLayout.CENTER);
        startGame();
    }

    public static ArrayList<Integer> createSolvablePuzzle() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int inversions = 0;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                int numI = numbers.get(i);
                int numJ = numbers.get(j);
                if (numI != 0 && numJ != 0 && numI > numJ) {
                    inversions++;
                }
            }
        }

        if (inversions % 2 == 1) {
            for (int i = 0; i < numbers.size(); i++) {
                for (int j = i + 1; j < numbers.size(); j++) {
                    if (numbers.get(i) != 0 && numbers.get(j) != 0) {
                        Collections.swap(numbers, i, j);
                        i = numbers.size();
                        break;
                    }
                }
            }
        }

        return numbers;
    }

    public void startGame() {
        ArrayList<Integer> numbers = createSolvablePuzzle();

        for (int i = 0; i < 9; i++) {
            tiles[i] = new JButton();
            tiles[i].setIcon(woodBack);
            int tileValue = numbers.get(i);
            if (tileValue == 0) {
                tiles[i].setText("");
                tiles[i].setVisible(false);
                indexOfCurrentEmptyTile = i;
            } else {
                tiles[i].setText(String.valueOf(tileValue));
            }
            tiles[i].setHorizontalTextPosition(SwingConstants.CENTER);
            tiles[i].setVerticalTextPosition(SwingConstants.CENTER);
            tiles[i].setFont(new Font("Serif", Font.BOLD, 44));
            tiles[i].setBackground(Color.WHITE);
            tiles[i].setBorder(new LineBorder(Color.BLACK, 1));
            buttonPanel.add(tiles[i]);
        }

        addTilesActionListeners();
    }

    public void addTilesActionListeners() {
        for (int i = 0; i < 9; i++) {
            final int indexOfClickedButton = i;
            tiles[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (indexOfClickedButton == indexOfCurrentEmptyTile - 3
                            || indexOfClickedButton == indexOfCurrentEmptyTile + 3)
                        swapButtons(tiles[indexOfClickedButton], tiles[indexOfCurrentEmptyTile], indexOfClickedButton);

                    else if (indexOfClickedButton == indexOfCurrentEmptyTile - 1
                            && (indexOfCurrentEmptyTile % 3 != 0))
                        swapButtons(tiles[indexOfClickedButton], tiles[indexOfCurrentEmptyTile], indexOfClickedButton);

                    else if (indexOfClickedButton == indexOfCurrentEmptyTile + 1
                            && (indexOfClickedButton % 3 != 0))
                        swapButtons(tiles[indexOfClickedButton], tiles[indexOfCurrentEmptyTile], indexOfClickedButton);

                    playSound("assets/wood.wav");
                }
            });
        }
    }

    public void swapButtons(JButton clickedButton, JButton emptyTile, int indexOfNextEmptyTile) {
        emptyTile.setText(clickedButton.getText());
        emptyTile.setVisible(true);
        clickedButton.setText("");
        clickedButton.setVisible(false);
        indexOfCurrentEmptyTile = indexOfNextEmptyTile;
        labNoOfMoves.setText("Moves: " + ++noOfMoves);
    }

    public void playSound(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "New Game":
                frame.dispose();
                new EightPuzzle();
                break;

            case "Exit":
                System.exit(0);
                break;

            case "Contact":
                try {
                    Desktop.getDesktop().browse(new URL("https://twitter.com/SoumyadeepB2001").toURI());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Browser not found");
                }
                break;

            case "Rules":
                JOptionPane.showMessageDialog(null,
                        "Starting at the top left corner, move the tiles in ascending order in the grid. \nThe tile in the lower right corner should remain \"empty\". \nTo move a tile you can click on it.");
                break;

            case "About":
                JOptionPane.showMessageDialog(null,
                        "Match The Tiles Game\nVersion: 1.0.1\nProgram written by Soumyadeep Banerjee, MCA");
                break;
        }
    }
}
