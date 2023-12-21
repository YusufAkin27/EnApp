import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SentenceGamePage {
    private JFrame frame;
    private JPanel panel;
    private JLabel wordLabel;
    private JTextArea sentenceTextArea;
    private JButton submitButton;
    private JButton exitButton;

    private SentenceGenerator sentenceGenerator;

    private User user;

    public SentenceGamePage(User user) {
        this.user = user;

        frame = new JFrame("Cümle Oyunu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wordLabel = new JLabel();
        wordLabel.setFont(new Font("Arial", Font.BOLD, 18));

        sentenceTextArea = new JTextArea();
        sentenceTextArea.setLineWrap(true);
        sentenceTextArea.setWrapStyleWord(true);
        sentenceTextArea.setPreferredSize(new Dimension(400, 100));

        submitButton = new JButton("Gönder");
        submitButton.addActionListener(e -> checkSentence());

        exitButton = new JButton("Çıkış");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.BLACK);
        exitButton.addActionListener(e -> exitGame());

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(wordLabel);
        frame.add(sentenceTextArea);
        frame.add(submitButton);
        frame.add(panel);
        frame.add(exitButton);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        sentenceGenerator = new SentenceGenerator();
        showNextWord();
    }

    private void showNextWord() {
        String randomWord = sentenceGenerator.getRandomWord();
        wordLabel.setText("Kelime: " + randomWord);
        sentenceTextArea.setText("");
    }

    private void checkSentence() {
        String enteredSentence = sentenceTextArea.getText().trim();
        String correctWord = wordLabel.getText().substring(8).trim().toLowerCase();

        if (enteredSentence.toLowerCase().contains(correctWord)) {
            JOptionPane.showMessageDialog(frame, "Tebrikler, doğru bildiniz!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            showNextWord();
        } else {
            JOptionPane.showMessageDialog(frame, "Yanlış cümle! Lütfen tekrar deneyin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitGame() {
        frame.dispose();
        new MainPage(user);
    }

    // Bu metotları SentenceGamePage sınıfına ekleyerek SentenceGenerator sınıfını içeride oluşturabilirsiniz.
    private class SentenceGenerator {
        private static final String[] WORDS = {
                "Adventure", "Imagination", "Courage", "Harmony", "Discovery", "Happiness", /* ... Diğer kelimeler buraya eklenebilir. */ };

        public String getRandomWord() {
            int randomIndex = (int) (Math.random() * WORDS.length);
            return WORDS[randomIndex];
        }
    }
}
