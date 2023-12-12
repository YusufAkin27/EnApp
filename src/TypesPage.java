import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypesPage {

    private JFrame frame;
    private JPanel panel;

    private List<String> types = Arrays.asList(
            "Ev Esyalari", "Okul Esyalari", "Meyve İsimleri", "Hayvanlar", "Renkler",
            "Yiyecekler", "Meslekler", "Tasitlar", "Ulkeler", "Sporlar",
            "Unlu Sahsiyetler", "Film Karakterleri"
    );

    public TypesPage(User user) {
        frame = new JFrame("Türler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        panel = new MyPanel(); // Kendi oluşturduğumuz JPanel sınıfını kullanıyoruz
        panel.setLayout(new GridLayout(0, 3, 20, 20)); // 3 sütunlu bir grid layout
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        createTypeButtons(user);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void createTypeButtons(User user) {
        for (String typeName : types) {
            JButton button = createStyledButton(typeName);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    showWordsPage(typeName, user);
                }
            });

            panel.add(button);
        }
    }

    private JButton createStyledButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(44, 62, 80)); // Koyu mavi tonu
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Set a custom border with a lighter blue line
        button.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));

        // Set a custom rollover effect with a slightly lighter background
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(34, 49, 63)); // Daha açık mavi tonu
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(44, 62, 80));
            }
        });

        return button;
    }

    private void showWordsPage(String typeName, User user) {
        JFrame wordFrame = new JFrame(typeName);
        wordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wordFrame.setSize(800, 600);

        JPanel wordPanel = new MyPanel(); // Kendi oluşturduğumuz JPanel sınıfını kullanıyoruz
        wordPanel.setLayout(new BorderLayout());
        wordPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Renk geçişini ayarlayabilirsiniz
        GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(44, 62, 80), 800, 600, new Color(34, 49, 63));

        wordPanel.setOpaque(true);
        wordPanel.setBackground(new Color(44, 62, 80));
        ((MyPanel) wordPanel).setGradientPaint(gradientPaint); // Renk geçişini ayarlıyoruz

        // Seçilen türün kelimelerini içeren dosyayı oku
        List<String> words = readWordsFromFile(typeName);

        // Kelimeleri JTable içinde göster
        String[] columnNames = {"English", "Türkçe"};
        String[][] data = new String[words.size()][2];

        for (int i = 0; i < words.size(); i++) {
            String[] wordPair = words.get(i).split(",");
            data[i][0] = wordPair[0].trim(); // Türkçe kelime
            data[i][1] = wordPair[1].trim(); // İngilizce kelime
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        wordPanel.add(scrollPane, BorderLayout.CENTER);

        wordFrame.add(wordPanel);
        wordFrame.setVisible(true);
        wordFrame.setLocationRelativeTo(null);
    }

    private List<String> readWordsFromFile(String typeName) {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("types/" + typeName.replaceAll(" ", "") + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TypesPage(new User()));
    }

    // Kendi JPanel sınıfınız
    private class MyPanel extends JPanel {
        private GradientPaint gradientPaint;

        public void setGradientPaint(GradientPaint gradientPaint) {
            this.gradientPaint = gradientPaint;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gradientPaint != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
