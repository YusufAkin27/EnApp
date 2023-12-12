import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypesListPage {

    public TypesListPage(String fileName, User user) {
        JFrame frame = new JFrame("Kelimeler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new MyPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Renk geçişini ayarlayabilirsiniz
        GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(44, 62, 80), 800, 600, new Color(34, 49, 63));

        panel.setOpaque(true);
        panel.setBackground(new Color(44, 62, 80));
        ((MyPanel) panel).setGradientPaint(gradientPaint); // Renk geçişini ayarlıyoruz

        // Seçilen türün kelimelerini içeren dosyayı oku
        List<String> words = readWordsFromFile(fileName);

        // Kelimeleri JTable içinde göster
        String[] columnNames = {"Türkçe", "English"};
        String[][] data = new String[words.size()][2];

        for (int i = 0; i < words.size(); i++) {
            String[] wordPair = words.get(i).split(",");
            data[i][0] = wordPair[0].trim(); // Türkçe kelime
            data[i][1] = wordPair[1].trim(); // İngilizce kelime
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private List<String> readWordsFromFile(String fileName) {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("types/" + fileName.replaceAll(" ", "") + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

    // Kendi JPanel sınıfınız
    private static class MyPanel extends JPanel {
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
