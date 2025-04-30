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
    JLabel labNoOfMoves;
    JPanel buttonPanel;
    ImageIcon woodBack = new ImageIcon("assets/woodBack.png");
    int indexOfCurrentEmptyTile = -1; // stores the index of the currently empty tile
    int noOfMoves = 0;
    JButton tiles[] = new JButton[9];
    JButton autoSolve;

    EightPuzzle(ArrayList<Integer> numbers) {
        frame = new JFrame("Eight Puzzle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        initComponents();
        startGame(numbers);
        frame.setVisible(true);
    }

    private void initComponents() {
        // Set up the menu bar and menu items
        MenuBarController menuHelper = new MenuBarController(frame);
        frame.setJMenuBar(menuHelper.createMenuBar());
        
        labNoOfMoves = new JLabel("Moves: " + noOfMoves);
        labNoOfMoves.setFont(new Font("Arial", Font.BOLD, 24));
        labNoOfMoves.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(labNoOfMoves, BorderLayout.NORTH);

        buttonPanel = new JPanel(new GridLayout(3, 3)); // 3 rows and 3 columns for 9 buttons
        frame.add(buttonPanel, BorderLayout.CENTER);

        autoSolve = new JButton("Auto solve using AI");
        autoSolve.setBackground(new Color(33, 3, 4));
        autoSolve.setForeground(Color.white);
        autoSolve.addActionListener(this);
        frame.add(autoSolve, BorderLayout.SOUTH);
    }

    public void startGame(ArrayList<Integer> numbers) {

        for (int i = 0; i < 9; i++) {
            tiles[i] = new JButton(); // Create an empty button
            tiles[i].setIcon(woodBack);
            tiles[i].setText(String.valueOf(numbers.get(i)));
            tiles[i].setHorizontalTextPosition(SwingConstants.CENTER);
            tiles[i].setVerticalTextPosition(SwingConstants.CENTER); // Center text on icon
            // Change text to null if the number is 0 because 0 represents the empty button
            // and set it as invisible
            if (numbers.get(i) == 0) {
                tiles[i].setVisible(false);
                indexOfCurrentEmptyTile = i;
            }
            tiles[i].setFont(new Font("Serif", Font.BOLD, 44));
            tiles[i].setBackground(Color.WHITE);
            tiles[i].setForeground(Color.BLACK);
            tiles[i].setBorder(new LineBorder(Color.BLACK, 1));

            buttonPanel.add(tiles[i]); // Add buttons to the panel
        }

        addTilesActionListeners();
    }

    public void addTilesActionListeners() {
        for (int i = 0; i < 9; i++) {
            final int indexOfClickedButton = i;
            tiles[i].addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            if (isAdjacent(indexOfClickedButton, indexOfCurrentEmptyTile)) {
                                swapButtons(tiles[indexOfClickedButton], tiles[indexOfCurrentEmptyTile],
                                        indexOfClickedButton);
                                playSound("assets/wood.wav");
                            }

                        }
                    });
        }
    }

    private boolean isAdjacent(int index1, int index2) {
        // If two tiles are adjacent then their Manhattan dist = 1
        int row1 = index1 / 3;
        int col1 = index1 % 3;
        int row2 = index2 / 3;
        int col2 = index2 % 3;

        int rowDiff = Math.abs(row1 - row2);
        int colDiff = Math.abs(col1 - col2);

        return (rowDiff + colDiff == 1);
    }

    public void swapButtons(JButton clickedButton, JButton emptyTile, int indexOfNextEmptyTile) {
        emptyTile.setText(clickedButton.getText());
        emptyTile.setVisible(true);
        clickedButton.setVisible(false);
        indexOfCurrentEmptyTile = indexOfNextEmptyTile;
        labNoOfMoves.setText("Moves: " + ++noOfMoves);
    }

    public void playSound(String soundFileName) {
        try {
            // Load the sound file
            File soundFile = new File(soundFileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // Get a clip resource
            Clip clip = AudioSystem.getClip();

            // Open the audio stream and start playing it
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void autoSolve() {
        labNoOfMoves.setText("Solving...");

        ArrayList<Integer> finalState = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (tiles[i].isVisible()) {
                finalState.add(Integer.parseInt(tiles[i].getText()));
            } else {
                finalState.add(0);
            }
        }

        new AutoSolve(finalState);
        frame.dispose();
    }

    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "Auto solve using AI":
                autoSolve();
                break;
        }
    }
}
