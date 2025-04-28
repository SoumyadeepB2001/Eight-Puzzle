import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SetUpBoard implements ActionListener {

    private JFrame frame;
    private JPanel buttonPanel;
    private Set<Integer> usedNumbers;
    private ArrayList<JButton> buttons;
    private ImageIcon woodBack;
    private JMenuBar menuBar;
    private JMenu options, help;
    private JMenuItem newGame, exit, contact, rules, about;

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

        initMenuBar(); // Setup menu bar

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

    private void initMenuBar() {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "New Game":
                frame.dispose();
                new Main(); // Assuming you have a Main class that shows the main menu
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Contact":
                try {
                    Desktop.getDesktop().browse(new URL("https://twitter.com/SoumyadeepB2001").toURI());
                } catch (Exception ex) {
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
