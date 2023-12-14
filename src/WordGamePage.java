import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordGamePage {

    private JFrame frame;
    private JPanel panel;
    private JLabel questionLabel;
    private List<String> currentWordLetters;
    private List<String> enteredLetters;
    private List<JButton> letterButtons;
    private JButton exitButton;

    private User user;

    private int correctAnswers;

    public WordGamePage(User user) {
        this.user = user;

        frame = new JFrame("Kelime Oyunu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        exitButton = new JButton("Çıkış");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.BLACK);
        exitButton.addActionListener(e -> exitGame());

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(questionLabel);
        frame.add(panel);
        frame.add(exitButton);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        startGame();
    }

    private void startGame() {
        correctAnswers = 0;
        showNextWord();
    }

    private void showNextWord() {
        String word = "example";
        currentWordLetters = shuffleLetters(word);
        enteredLetters = new ArrayList<>();
        displayLettersAsButtons();

        // Soru etiketini sıfırla
        questionLabel.setText("Kelime: ");
    }

    private List<String> shuffleLetters(String word) {
        List<String> letters = new ArrayList<>();
        for (char letter : word.toCharArray()) {
            letters.add(String.valueOf(letter));
        }
        Collections.shuffle(letters);
        return letters;
    }

    private void displayLettersAsButtons() {
        panel.removeAll();
        letterButtons = new ArrayList<>();

        for (String letter : currentWordLetters) {
            JButton letterButton = new JButton(letter);
            letterButton.setPreferredSize(new Dimension(50, 50));
            letterButton.addActionListener(new LetterButtonListener());
            letterButtons.add(letterButton);
            panel.add(letterButton);
        }

        frame.revalidate();
        frame.repaint();
    }

    private void checkWord() {
        String enteredWord = String.join("", enteredLetters);
        String correctWord = String.join("", currentWordLetters);

        if (enteredWord.equals(correctWord)) {
            correctAnswers++;
            displayCorrectWord(correctWord);
            clearEnteredLetters();
            enableAllButtons();
            // Kelime doğru bilindi, istediğiniz bir mesaj gösterebilirsiniz.
            JOptionPane.showMessageDialog(frame, "Tebrikler, doğru bildiniz!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        } else if (enteredLetters.size() == currentWordLetters.size()) {
            // Yanlış kelime, butonları etkinleştir ve kullanıcıyı uyar
            enableAllButtons();
            // Yanlış kelime durumunda doğru kelime etiketini ve girilen harfleri temizle
            clearCorrectWordLabel();
            clearEnteredLetters();
            JOptionPane.showMessageDialog(frame, "Yanlış kelime! Tekrar deneyin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayCorrectWord(String word) {
        JLabel correctWordLabel = new JLabel(word);
        frame.add(correctWordLabel);
        frame.revalidate();
        frame.repaint();
    }

    private void clearEnteredLetters() {
        enteredLetters.clear();
        frame.revalidate();
        frame.repaint();
    }

    private void enableAllButtons() {
        for (JButton button : letterButtons) {
            button.setEnabled(true);
        }
    }

    private void clearCorrectWordLabel() {
        Component[] components = frame.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                frame.remove(component);
            }
        }
        frame.revalidate();
        frame.repaint();
    }

    private class LetterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String letter = clickedButton.getText();
            enteredLetters.add(letter);

            displayEnteredLetters();
            clickedButton.setEnabled(false);
            checkWord();
        }
    }

    private void displayEnteredLetters() {
        questionLabel.setText("Kelime: " + String.join("", enteredLetters));
    }

    private void exitGame() {
        user.updateUser(user);
        frame.dispose();
        new MainPage(user);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordGamePage(new User()));
    }
}
