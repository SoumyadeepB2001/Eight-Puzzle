import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SetUpBoard {

    private JFrame frame;
    private JPanel buttonPanel;
    private Set<Integer> usedNumbers;
    private ArrayList<JButton> buttons;
    private ImageIcon woodBack;

    public SetUpBoard() {
        usedNumbers = new HashSet<>();
        buttons = new ArrayList<>();
        woodBack = new ImageIcon("assets/woodBack.png"); // Load the background once
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Set Up Puzzle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 550);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Set up the menu bar and menu items
        MenuBarController menuHelper = new MenuBarController(frame);
        frame.setJMenuBar(menuHelper.createMenuBar());

        buttonPanel = new JPanel(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 44));
            button.setForeground(Color.BLACK);
            button.setIcon(woodBack);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            button.setContentAreaFilled(false);
            button.setOpaque(false);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(button);
                }
            });

            buttonPanel.add(button);
            buttons.add(button);
        }

        JButton startButton = new JButton("Start Game");
        startButton.setBackground(new Color(33, 3, 4));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(startButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void handleButtonClick(JButton button) {
        String input = JOptionPane.showInputDialog(frame, "Enter a number between 1 and 8 (leave one space blank):");
        if (input == null) {
            return; // User pressed cancel
        }
        input = input.trim();
        if (input.isEmpty()) {
            String oldText = button.getText();
            if (!oldText.isEmpty()) {
                try {
                    int oldNumber = Integer.parseInt(oldText);
                    usedNumbers.remove(oldNumber);
                } catch (NumberFormatException ex) {
                    // Ignore
                }
            }
            button.setText("");
            return;
        }

        try {
            int number = Integer.parseInt(input);

            if (number < 1 || number > 8) {
                JOptionPane.showMessageDialog(frame, "Please enter a number between 1 and 8.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String oldText = button.getText();
            if (!oldText.isEmpty()) {
                try {
                    int oldNumber = Integer.parseInt(oldText);
                    usedNumbers.remove(oldNumber);
                } catch (NumberFormatException ex) {
                    // Ignore
                }
            }

            if (usedNumbers.contains(number)) {
                JOptionPane.showMessageDialog(frame, "This number has already been used. Choose a different number.",
                        "Duplicate Number", JOptionPane.WARNING_MESSAGE);
                return;
            }

            button.setText(String.valueOf(number));
            usedNumbers.add(number);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a valid number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static int countInversions(ArrayList<Integer> numbers) {
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
        return inversions;
    }

    private void startGame() {
        if (usedNumbers.size() != 8) {
            JOptionPane.showMessageDialog(frame,
                    "Please fill numbers 1 to 8 (leave exactly one blank space) before starting.", "Incomplete Setup",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Integer> numbers = new ArrayList<>();
        for (JButton button : buttons) {
            String text = button.getText();
            if (text.isEmpty()) {
                numbers.add(0);
            } else {
                numbers.add(Integer.parseInt(text));
            }
        }

        if (countInversions(numbers) % 2 == 1) {
            JOptionPane.showMessageDialog(frame,
                    "This setup is unsolvable.", "Unsolvable Setup",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        frame.dispose();
        new EightPuzzle(numbers);
    }
}
