import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LibraryPage extends JFrame {

    private List<Word> wordList;
    private User user;

    private JTextField txtMeanTR;
    private JTextField txtWordEN;
    private JPanel panel;

    public LibraryPage(User user) {
        setTitle("Kelime Kütüphanesi");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(6, 2));

        this.user = user;
        wordList = user.getLibrary();
        if (wordList == null) {
            wordList = new ArrayList<>();
            user.setLibrary(wordList);
        }

        JLabel lblMeanTR = new JLabel("Türkçe Anlam:");
        txtMeanTR = new JTextField();
        JLabel lblWordEN = new JLabel("İngilizce Kelime:");
        txtWordEN = new JTextField();

        JButton btnAdd = new JButton("Kelime Ekle");
        JButton btnRemove = new JButton("Kelime Çıkar");
        JButton btnGetAll = new JButton("Tüm Kelimeleri Getir");
        JButton btnGeri = new JButton("Geri");

        btnAdd.addActionListener(e -> addWord(user));
        btnRemove.addActionListener(e -> removeWord(user));
        btnGetAll.addActionListener(e -> getAllWords());
        btnGeri.addActionListener(e -> goBackToMainPage());

        add(lblMeanTR);
        add(txtMeanTR);
        add(lblWordEN);
        add(txtWordEN);
        add(btnAdd);
        add(btnRemove);
        add(btnGetAll);
        add(btnGeri);

        panel = new JPanel();
        add(panel);

        loadUserLibrary();
        setVisible(true);
        setResizable(false);
        startColorTransition();
    }

    private void getAllWords() {
        dispose();
        new WordsPage(user);
    }

    private void goBackToMainPage() {
        dispose();
        new MainPage(user);
    }

    private void clearFields() {
        txtMeanTR.setText("");
        txtWordEN.setText("");
    }

    private void removeWord(User user) {
        String inputTR = txtWordEN.getText();
        String inputEN = txtMeanTR.getText();

        if (!inputEN.isEmpty() || !inputTR.isEmpty()) {
            Iterator<Word> iterator = user.getLibrary().iterator();
            boolean removed = false;

            while (iterator.hasNext()) {
                Word word = iterator.next();
                if ((word.getWordEN().equalsIgnoreCase(inputEN) && !inputEN.isEmpty()) ||
                        (word.getMeanTR().equalsIgnoreCase(inputTR) && !inputTR.isEmpty())) {
                    iterator.remove();
                    removed = true;
                    break; // Kelime bulunduğunda döngüden çık
                }
            }

            if (removed) {
                updateLibraryFile(user); // Dosyadan da kaldır
                showMessageWithColor("Kelime çıkarıldı!", Color.GREEN);
                clearFields();
            } else {
                showMessageWithColor("Bu kelime bulunamadı!", Color.RED);
            }
        }
    }



    private void addWord(User user) {
        String meanTR = txtMeanTR.getText();
        String wordEN = txtWordEN.getText();

        if (!meanTR.isEmpty() && !wordEN.isEmpty()) {
            Word word = new Word(meanTR, wordEN);
            wordList.add(word);

            updateLibraryFile(user);

            showMessageWithColor("Kelime eklendi!", Color.GREEN);
            clearFields();
        } else {
            showMessageWithColor("Lütfen Türkçe anlam ve İngilizce kelime giriniz!", Color.RED);
        }
    }

    private void showMessageWithColor(String message, Color color) {
        panel.setBackground(color);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                panel.setBackground(null);
            }
        }, 1000);
        JOptionPane.showMessageDialog(this, message);
    }



    private void loadUserLibrary() {
        Path userLibraryPath = Paths.get("library", user.getUsername() + "_kutuphane.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(userLibraryPath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] wordData = line.split(",");

                if (wordData.length == 2) {
                    String meanTR = wordData[0];
                    String wordEN = wordData[1];
                    Word word = new Word(meanTR, wordEN);
                    wordList.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Kütüphane dosyasını okurken bir hata oluştu: " + e.getMessage());
        }
    }

    public static void updateLibraryFile(User user) {
        // Kütüphanedeki dosya yolu
        Path userLibraryPath = Paths.get("library", user.getUsername() + "_kutuphane.txt");

        // Dosyayı temizle ve kullanıcının kütüphanesini yaz
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userLibraryPath.toFile(), false))) {
            List<Word> wordList = user.getLibrary();
            for (Word word : wordList) {
                writer.write(word.getMeanTR() + "," + word.getWordEN());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Kütüphane dosyasına yazarken bir hata oluştu: " + e.getMessage());
        }
    }
    private void startColorTransition() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            Color[] colors = {Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE, Color.ORANGE};
            int index = 0;

            @Override
            public void run() {
                panel.setBackground(colors[index]);
                index = (index + 1) % colors.length;
            }
        }, 0, 1000);
    }
}
