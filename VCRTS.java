import javax.swing.*;
import java.awt.*;

public class VCRTS {
    public static void main(String[] args) {
        showMainMenu();
        showCoverPage();
    }

    private static void showCoverPage() {
        JFrame coverFrame = new JFrame("Welcome to Group 3's VCRTS");
        coverFrame.setSize(400, 400);
        coverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        coverFrame.setLayout(new BorderLayout());

        JLabel coverLabel = new JLabel("Welcome to Group 3's VCRTS", SwingConstants.CENTER);
        coverLabel.setFont(new Font("Serif", Font.BOLD, 22));
        coverLabel.setForeground(new Color(59, 89, 182));

        coverFrame.add(coverLabel, BorderLayout.CENTER);

        coverFrame.setVisible(true);

        Timer timer = new Timer(5000, e -> coverFrame.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    public static void showMainMenu() {
        MainMenuGui mainMenu = new MainMenuGui();
        mainMenu.display();
    }
}