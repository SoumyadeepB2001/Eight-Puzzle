import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AutoSolve {
    JFrame frame;
    JLabel labNoOfMoves;
    JPanel labelPanel;
    ImageIcon woodBack = new ImageIcon("assets/woodBack.png");
    JLabel tiles[] = new JLabel[9];
    ArrayList<Integer> inputBoard;
    Timer timer;

    public AutoSolve(ArrayList<Integer> numbers) {
        frame = new JFrame("Eight Puzzle - Solved View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        initComponents();
        inputBoard = numbers;
        showCurrentState(inputBoard);
        frame.setVisible(true);
        showSolving(solvePuzzle());
    }

    private void initComponents() {
        // Set up the menu bar and menu items
        MenuBarController menuHelper = new MenuBarController(this);
        frame.setJMenuBar(menuHelper.createMenuBar());

        labNoOfMoves = new JLabel("Solving...");
        labNoOfMoves.setFont(new Font("Arial", Font.BOLD, 24));
        labNoOfMoves.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(labNoOfMoves, BorderLayout.NORTH);

        labelPanel = new JPanel(new GridLayout(3, 3));
        frame.add(labelPanel, BorderLayout.CENTER);
    }

    private ArrayList<ArrayList<Integer>> solvePuzzle() {
        int[] startBoard = new int[9];
        for (int i = 0; i < 9; i++) {
            startBoard[i] = inputBoard.get(i);
        }

        BoardState start = new BoardState(startBoard, null, 0);

        PriorityQueue<BoardState> openList = new PriorityQueue<>(); // to get the smallest value
        HashSet<String> closedSet = new HashSet<>();

        openList.add(start);

        while (!openList.isEmpty()) {
            BoardState current = openList.poll();

            if (current.isGoal()) {
                // Found goal, reconstruct path
                ArrayList<ArrayList<Integer>> path = new ArrayList<>();
                while (current != null) {
                    ArrayList<Integer> state = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        state.add(current.board[i]);
                    }
                    path.add(state);
                    current = current.parent;
                }
                Collections.reverse(path); // To get path from start to goal
                return path;
            }

            closedSet.add(Arrays.toString(current.board));
            ArrayList<BoardState> neighbors = current.generateNeighbors();

            for (int i = 0; i < neighbors.size(); i++) {
                BoardState neighbor = neighbors.get(i);
                String hash = Arrays.toString(neighbor.board);
                if (!closedSet.contains(hash)) {
                    openList.add(neighbor);
                }
            }
        }
        return new ArrayList<>();
    }

    private void showCurrentState(ArrayList<Integer> numbers) {
        for (int i = 0; i < 9; i++) {
            tiles[i] = new JLabel();
            tiles[i].setOpaque(true);
            tiles[i].setIcon(woodBack);
            tiles[i].setHorizontalAlignment(SwingConstants.CENTER);
            tiles[i].setVerticalAlignment(SwingConstants.CENTER);
            tiles[i].setHorizontalTextPosition(SwingConstants.CENTER);
            tiles[i].setVerticalTextPosition(SwingConstants.CENTER);
            tiles[i].setFont(new Font("Serif", Font.BOLD, 44));
            tiles[i].setBorder(new LineBorder(Color.BLACK, 1));
            tiles[i].setForeground(Color.BLACK); // Text color

            if (numbers.get(i) != 0) {
                tiles[i].setText(String.valueOf(numbers.get(i)));
            } else {
                tiles[i].setVisible(false); // Hide empty tile
            }

            labelPanel.add(tiles[i]);
        }
    }

    private void showSolving(ArrayList<ArrayList<Integer>> states) {
        final int[] index = {0};
    
        timer = new Timer(800, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (index[0] >= states.size()) {
                    timer.stop(); // Stop the timer directly
                    labNoOfMoves.setText("Solved in " + (states.size() - 1) + " moves");
                    return;
                }
    
                ArrayList<Integer> currentState = states.get(index[0]); // Get state before incrementing
    
                SwingUtilities.invokeLater(() -> {
                    labelPanel.removeAll();
                    showCurrentState(currentState);
                    playSound("assets/wood.wav");
                    labelPanel.revalidate();
                    labelPanel.repaint();
                });
    
                index[0]++; // Increment AFTER UI update
            }
        });
    
        timer.setInitialDelay(800);
        timer.start();
    }    

    public void stopSolving() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        frame.dispose();
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
}
