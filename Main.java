import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main implements ActionListener {
    JFrame frame;
    JButton btnRandomPuzzle, btnSetupBoard;
    JLabel backgroundLabel;

    public static void main(String[] args) {
        new Main();
    }

    Main() {
        // Create and set up the frame
        frame = new JFrame("Eight Puzzle - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLayout(null); // Disable layout manager to use setBounds
        frame.setLocationRelativeTo(null);

        // Set up background image
        ImageIcon woodBack = new ImageIcon("assets/woodBack.png");
        backgroundLabel = new JLabel(woodBack);
        backgroundLabel.setBounds(0, 0, 500, 500); // Fill the entire frame with the image
        frame.add(backgroundLabel);

        // Create the buttons
        btnRandomPuzzle = new JButton("Generate Random Puzzle");
        btnSetupBoard = new JButton("Set Up Your Own Board");

        // Set font for the buttons
        btnRandomPuzzle.setFont(new Font("Arial", Font.BOLD, 18));
        btnSetupBoard.setFont(new Font("Arial", Font.BOLD, 18));

        // Set the preferred size for both buttons to make them wider
        btnRandomPuzzle.setPreferredSize(new Dimension(350, 60));
        btnSetupBoard.setPreferredSize(new Dimension(350, 60));

        // Set button bounds for positioning and size
        btnRandomPuzzle.setBounds(75, 150, 350, 60); // x, y, width, height
        btnSetupBoard.setBounds(75, 250, 350, 60); // x, y, width, height

        // Button background color
        btnRandomPuzzle.setBackground(Color.white);
        btnSetupBoard.setBackground(Color.white);

        // Add action listeners
        btnRandomPuzzle.addActionListener(this);
        btnSetupBoard.addActionListener(this);

        // Add buttons on top of the background
        backgroundLabel.add(btnRandomPuzzle);
        backgroundLabel.add(btnSetupBoard);

        // Make frame visible
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRandomPuzzle) {
            frame.dispose();
            new GeneratePuzzle(); // Start the normal random puzzle
        } else if (e.getSource() == btnSetupBoard) {
            frame.dispose();
            new SetUpBoard();
        }
    }
}
