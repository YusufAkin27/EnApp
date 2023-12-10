import level.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class QuizPage {

    private JFrame frame;
    private JPanel panel;
    private JButton startQuizButton;
    private JButton exitButton;
    private JLabel questionLabel;
    private JTextField answerField;
    private JLabel statusLabel;
    private JLabel timeLabel;

    private A1Words a1Words;
    private A2Words a2Words;
    private B1Words b1Words;
    private B2Words b2Words;
    private C1Words c1Words;
    private C2Words c2Words;

    private String currentLevel;
    private String currentWord;
    private String correctAnswer;
    private int correctAnswers;
    private int incorrectAnswers;
    private boolean hasPassed;

    private Random random;

    private User user;

    private List<String> askedWords;

    private Timer questionTimer;
    private int remainingTimeInSeconds;

    public QuizPage(User user) {
        this.user = user;
        initializeWords();

        frame = new JFrame("Quiz Uygulaması");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        startQuizButton = new JButton("Quiz Başlat");
        startQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startQuizButton.addActionListener(e -> startQuiz());
        panel.add(startQuizButton);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(questionLabel);

        answerField = new JTextField(15);
        answerField.setFont(new Font("Arial", Font.PLAIN, 16));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(answerField);

        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkAnswer();
                }
            }
        });

        exitButton = new JButton("Çıkış");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.BLACK);
        exitButton.addActionListener(e -> exitQuiz());
        panel.add(exitButton);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statusLabel);

        timeLabel = new JLabel(); // Eklenen yeni etiket
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(timeLabel);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        random = new Random();
        askedWords = new ArrayList<>();

        remainingTimeInSeconds = 10;
        questionTimer = new Timer(1000, e -> {
            remainingTimeInSeconds--;
            if (remainingTimeInSeconds <= 0) {
                handleTimeout();
            }
            updateStatusLabel(); // Zaman her azaldığında etiketi güncelle
        });
    }

    private void initializeWords() {
        a1Words = new A1Words();
        a2Words = new A2Words();
        b1Words = new B1Words();
        b2Words = new B2Words();
        c1Words = new C1Words();
        c2Words = new C2Words();
    }

    private void startQuiz() {
        correctAnswers = 0;
        incorrectAnswers = 0;
        hasPassed = false;
        currentLevel = user.getLevel();
        askedWords.clear();
        askQuestion();
        panel.remove(startQuizButton);
        frame.revalidate();
        frame.repaint();
        startQuestionTimer(); // Timer'ı başlatma işlemi burada olmalı
    }

    private void askQuestion() {
        if (incorrectAnswers < 3 && correctAnswers < 7) {
            String newWord = getRandomWord(currentLevel);
            while (askedWords.contains(newWord)) {
                newWord = getRandomWord(currentLevel);
            }

            askedWords.add(newWord);

            currentWord = newWord;
            correctAnswer = getCorrectAnswer(currentWord, currentLevel);
            questionLabel.setText("Kelime: " + currentWord);
            answerField.setText("");
            statusLabel.setText("Seviye: " + currentLevel + " | Doğru: " + correctAnswers + " | Yanlış: " + incorrectAnswers);
            panel.setBackground(null);
            hasPassed = false;
            resetQuestionTimer();
            startQuestionTimer();
            updateStatusLabel(); // Eklenen yeni satır
        } else {
            endQuiz(currentLevel);
        }
    }

    private void endQuiz(String currentLevel) {
        if (incorrectAnswers >= 3) {
            showFeedback("3 hata sayısına ulaştınız. Seviyeniz : " + currentLevel, Color.RED);
            user.setLevel(currentLevel);
            user.updateUser(user);
            frame.dispose();
            new MainPage(user);
        } else if (correctAnswers >= 7) {
            showFeedback("Tebrikler! Bir üst seviyeye geçtiniz.", Color.GREEN);
            user.levelUp();
            user.updateUser(user);
            startQuiz();
        }
    }

    private void addToLibrary(String wordEN, String meanTR, User user) {
        List<Word> words = readUserLibraryFile(user);

        Word word = new Word(meanTR, wordEN);
        if (!words.contains(word)) {
            words.add(word);
        }

        user.setLibrary(words);
        writeToUserLibraryFile(user);
        user.updateUser(user);
    }

    private List<Word> readUserLibraryFile(User user) {
        List<Word> words = new ArrayList<>();
        Path userLibraryPath = Paths.get("library", user.getUsername() + "_kutuphane.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(userLibraryPath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String meanTR = parts[0];
                    String wordEN = parts[1];
                    Word word = new Word(meanTR, wordEN);
                    words.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Kütüphane dosyasını okurken bir hata oluştu: " + e.getMessage());
        }

        return words;
    }

    private void writeToUserLibraryFile(User user) {
        Path userLibraryPath = Paths.get("library", user.getUsername() + "_kutuphane.txt");
        List<Word> words = user.getLibrary();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userLibraryPath.toFile()))) {
            for (Word word : words) {
                writer.write(word.getMeanTR() + "," + word.getWordEN());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Kütüphane dosyasına yazarken bir hata oluştu: " + e.getMessage());
        }
    }

    private String getRandomWord(String level) {
        Map<String, String> wordList = getWordListByLevel(level);
        List<String> wordKeys = new ArrayList<>(wordList.keySet());
        return wordKeys.get(random.nextInt(wordList.size()));
    }

    private String getCorrectAnswer(String word, String level) {
        Map<String, String> wordList = getWordListByLevel(level);
        return wordList.get(word);
    }

    private Map<String, String> getWordListByLevel(String level) {
        switch (level) {
            case "A1":
                return a1Words.getWords();
            case "A2":
                return a2Words.getWords();
            case "B1":
                return b1Words.getWords();
            case "B2":
                return b2Words.getWords();
            case "C1":
                return c1Words.getWords();
            case "C2":
                return c2Words.getWords();
            default:
                throw new IllegalArgumentException("Geçersiz seviye: " + level);
        }
    }

    private void checkAnswer() {
        String userAnswer = answerField.getText().trim().toLowerCase();

        if (isCorrectAnswer(userAnswer)) {
            showFeedback("Doğru! Bir sonraki soruya geçebilirsiniz.", Color.GREEN);
            correctAnswers++;
        } else {
            showFeedback("Yanlış cevap. Doğru cevap: " + correctAnswer, Color.RED);
            addToLibrary(currentWord, correctAnswer, user);
            incorrectAnswers++;
        }

        stopQuestionTimer();
        resetQuestionTimer();
        askQuestion();
    }

    private boolean isCorrectAnswer(String userAnswer) {
        for (String meaning : getWordListByLevel(currentLevel).values()) {
            if (userAnswer.equals(meaning.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void showFeedback(String message, Color color) {
        showTempMessage(message, JOptionPane.INFORMATION_MESSAGE);
        panel.setBackground(color);
        Timer timer = new Timer(1000, e -> panel.setBackground(null));
        timer.setRepeats(false);
        timer.start();
    }

    private void showTempMessage(String message, int messageType) {
        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog("Bilgi");

        Timer timer = new Timer(700, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    private void exitQuiz() {
        stopQuestionTimer();
        user.updateUser(user);
        frame.dispose();
        new MainPage(user);
    }

    private void startQuestionTimer() {
        questionTimer.start();
        updateStatusLabel();
    }

    private void stopQuestionTimer() {
        questionTimer.stop();
        updateStatusLabel();
    }

    private void resetQuestionTimer() {
        remainingTimeInSeconds = 10;
        updateStatusLabel();
    }

    private void handleTimeout() {
        showFeedback("Zaman doldu! Yanlış cevap. Doğru cevap: " + correctAnswer, Color.RED);
        addToLibrary(currentWord, correctAnswer, user);
        incorrectAnswers++;
        stopQuestionTimer();
        resetQuestionTimer();
        askQuestion();
    }

    private void updateStatusLabel() {
        timeLabel.setText("Kalan Süre: " + remainingTimeInSeconds + " saniye");
    }
}
