import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

public class AutoSolve implements ActionListener {
    JFrame frame;
    JMenuBar menuBar;
    JMenu options, help;
    JMenuItem newGame, exit, contact, rules, about;
    JLabel labNoOfMoves;
    JPanel labelPanel;
    ImageIcon woodBack = new ImageIcon("assets/woodBack.png");
    JLabel tiles[] = new JLabel[9];

    public AutoSolve(ArrayList<Integer> numbers) {
        frame = new JFrame("Eight Puzzle - Solved View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        initComponents();
        showFinalState(numbers);
        frame.setVisible(true);
    }

    private void initComponents() {
        // Set up the menu bar and menu items
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

        labNoOfMoves = new JLabel("Solving...");
        labNoOfMoves.setFont(new Font("Arial", Font.BOLD, 24));
        labNoOfMoves.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(labNoOfMoves, BorderLayout.NORTH);

        labelPanel = new JPanel(new GridLayout(3, 3));
        frame.add(labelPanel, BorderLayout.CENTER);
    }

    private void showFinalState(ArrayList<Integer> numbers) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "New Game":
                frame.dispose();
                new Main(); // Assuming you have a Main class like in your EightPuzzle
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
