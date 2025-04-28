import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class SetUpBoard {

    private JFrame frame;
    private JPanel buttonPanel;
    private Set<Integer> usedNumbers;

    public SetUpBoard() {
        usedNumbers = new HashSet<>();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Button Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        buttonPanel = new JPanel(new GridLayout(3, 3)); // 3x3 grid for 9 buttons

        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setBackground(Color.LIGHT_GRAY);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(button);
                }
            });

            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void handleButtonClick(JButton button) {
        String input = JOptionPane.showInputDialog(frame, "Enter a number between 0 and 8 (each number only once):");
        if (input == null) {
            return; // User pressed cancel
        }
        try {
            int number = Integer.parseInt(input);
            if (number < 0 || number > 8) {
                JOptionPane.showMessageDialog(frame, "Please enter a number between 0 and 8.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Remove the old number associated with this button (if any)
            String oldText = button.getText();
            if (!oldText.isEmpty()) {
                try {
                    int oldNumber = Integer.parseInt(oldText);
                    usedNumbers.remove(oldNumber);
                } catch (NumberFormatException ex) {
                    // If the button had non-number text, ignore
                }
            }

            // Check if the new number is already used
            if (usedNumbers.contains(number)) {
                JOptionPane.showMessageDialog(frame, "This number has already been used. Choose a different number.",
                        "Duplicate Number", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Set new number and mark it as used
            button.setText(String.valueOf(number));
            usedNumbers.add(number);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a valid number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
