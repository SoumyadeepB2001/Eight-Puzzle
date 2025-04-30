import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class MenuBarController implements ActionListener {
    private final JFrame frame;

    public MenuBarController(JFrame frame) {
        this.frame = frame;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu options = new JMenu("Options");
        JMenu help = new JMenu("Help");

        String[] items = {"New Game", "Exit", "Contact", "Rules", "About"};

        for (String itemText : items) {
            JMenuItem item = new JMenuItem(itemText);
            item.addActionListener(this);
            if (itemText.equals("New Game") || itemText.equals("Exit")) {
                options.add(item);
            } else {
                help.add(item);
            }
        }

        menuBar.add(options);
        menuBar.add(help);
        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "New Game":
                frame.dispose();
                new Main(); // replace with your main menu/game launcher
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
                        "Starting at the top left corner, move the tiles in ascending order in the grid.\n" +
                        "The tile in the lower right corner should remain \"empty\".\n" +
                        "To move a tile you can click on it.");
                break;
            case "About":
                JOptionPane.showMessageDialog(null,
                        "Match The Tiles Game\nVersion: 1.0.1\nProgram written by Soumyadeep Banerjee, MCA");
                break;
        }
    }
}
