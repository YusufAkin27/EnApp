import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePage {

    private JFrame frame;
    private JPanel panel;
    private JButton wordGameButton;
    private JButton sentenceGameButton;
    private User user;

    public GamePage(User user) {
        this.user = user;

        frame = new JFrame("Oyunlar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        panel = new JPanel(new GridLayout(2, 1, 20, 20)); // Two rows, one column
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        wordGameButton = createGameButton("Kelime Oyunu");
        wordGameButton.addActionListener(e -> {
            frame.dispose();
            new WordGamePage(user);
        });
        panel.add(wordGameButton);

        sentenceGameButton = createGameButton("Cümle Kurma Oyunu");
        sentenceGameButton.addActionListener(e -> {
            frame.dispose();
            new SentenceGamePage(user);
        });
        panel.add(sentenceGameButton);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private JButton createGameButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setBackground(new Color(124, 190, 190));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14)); // Küçültülen font
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // Küçültülen boşluklar
        button.setPreferredSize(new Dimension(150, 30)); // Küçültülen boyutlar

        // 3D efekti eklemek için Border
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createLoweredBevelBorder()
        ));

        return button;
    }


}
