import level.GameWords;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WordGamePage {

    private JFrame frame;
    private JPanel panel;
    private JLabel questionLabel;
    private List<String> originalWordLetters;
    private List<String> currentWordLetters;
    private List<String> enteredLetters;
    private List<JButton> letterButtons;
    private JButton exitButton;
    private JButton helpButton;
    private int helpCount;

    private User user;

    private int correctAnswers;

    private Map<String, String> words = GameWords.getWords();

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

        helpButton = new JButton("Yardım Al");
        helpButton.addActionListener(e -> getHelp());

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(questionLabel);
        frame.add(panel);
        frame.add(exitButton);
        frame.add(helpButton);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        startGame();
    }

    private void startGame() {
        correctAnswers = 0;
        helpCount = 0;
        showNextWord();
    }

    private void shuffleLetters(String word) {
        originalWordLetters = new ArrayList<>();
        for (char letter : word.toCharArray()) {
            originalWordLetters.add(String.valueOf(letter));
        }
        currentWordLetters = new ArrayList<>(originalWordLetters);
        Collections.shuffle(currentWordLetters);
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

    private void clearEnteredLetters() {
        enteredLetters.clear();
        questionLabel.setText("Kelime: ");
        frame.revalidate();
        frame.repaint();
    }

    private void enableAllButtons() {
        for (JButton button : letterButtons) {
            button.setEnabled(true);
        }
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
        String enteredText = String.join("", enteredLetters);
        questionLabel.setText("Kelime: " + enteredText);
    }

    private void checkWord() {
        String enteredWord = String.join("", enteredLetters).toLowerCase();
        String correctWord = String.join("", originalWordLetters).toLowerCase();

        if (enteredWord.equals(correctWord)) {
            correctAnswers++;
            displayCorrectWord(correctWord);
        } else if (enteredLetters.size() == currentWordLetters.size()) {
            clearEnteredLetters();
            enableAllButtons();
            JOptionPane.showMessageDialog(frame, "Yanlış kelime! Tekrar deneyin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNextWord() {
        clearCorrectWordLabel();
        String word = getRandomWord();
        shuffleLetters(word);
        enteredLetters = new ArrayList<>();
        displayLettersAsButtons();
        questionLabel.setText("Kelime: ");
    }

    private void clearCorrectWordLabel() {
        questionLabel.setText("Kelime: ");
    }

    private void displayCorrectWord(String word) {
        JOptionPane.showMessageDialog(frame, "Tebrikler, doğru bildiniz! Kelime: " + words.get(word), "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        clearEnteredLetters();
        clearCorrectWordLabel();
        showNextWord();
    }

    private String getRandomWord() {
        List<String> wordList = new ArrayList<>(words.keySet());
        return wordList.get((int) (Math.random() * wordList.size()));
    }

    private void exitGame() {
        frame.dispose();
        new MainPage(user);
    }

    private void getHelp() {
        if (originalWordLetters != null && !originalWordLetters.isEmpty() && helpCount < 3) {
            // Eğer yeni bir kelime başlıyorsa, yardım hakkını sıfırla
            if (enteredLetters.isEmpty()) {
                helpCount = 0;
            }

            helpCount++;

            // Kullanıcının doğru bildiği harfleri ve girdiği harfleri birleştir
            List<String> knownLetters = new ArrayList<>(enteredLetters);
            knownLetters.retainAll(originalWordLetters);

            // Eğer tüm harfler girilmişse yardım almasına gerek yok
            if (knownLetters.size() < originalWordLetters.size()) {
                // Doğru tahmin edilmemiş harfleri bul
                List<String> remainingLetters = new ArrayList<>(originalWordLetters);
                remainingLetters.removeAll(knownLetters);

                // Rastgele bir doğru tahmin edilmemiş harf seç
                String randomLetter = remainingLetters.get(0);  // İlk indeksi al

                // Eğer seçilen harf daha önce girilmemişse, enteredLetters listesine ekle
                if (!enteredLetters.contains(randomLetter)) {
                    enteredLetters.add(randomLetter);

                    // Butonları kontrol et, doğru harfi bulup kapat
                    for (JButton button : letterButtons) {
                        if (button.getText().equals(randomLetter)) {
                            button.setEnabled(false);
                            break;
                        }
                    }

                    displayEnteredLetters();
                    checkWord();
                }
            }
        } else if (helpCount == 3) {
            JOptionPane.showMessageDialog(frame, "Yardım almak için hakkınız kalmadı.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordGamePage(new User()));
    }
}
