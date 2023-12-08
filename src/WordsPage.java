import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class WordsPage extends JFrame {

    private List<Word> wordList;
    private int currentIndex;

    public WordsPage(User user) {
        this.wordList = user.getLibrary();
        this.currentIndex = 0;

        setTitle("Kelimeler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // JScrollPane içine cardPanel ekleniyor
        JScrollPane scrollPane = new JScrollPane(createCardPanel());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Dikey kaydırma çubuğunu sürekli göster

        JButton dictionaryButton = new JButton("Sözlük");
        JButton homeButton = new JButton("Ana Sayfa");

        dictionaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LibraryPage(user);
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainPage(user);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(dictionaryButton);
        buttonPanel.add(homeButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER); // JScrollPane kullanılan cardPanel ekleniyor
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // Boşluk ekleniyor

        for (Word word : wordList) {
            JPanel card = createWordCard(word);
            cardPanel.add(card);
        }

        return cardPanel;
    }

    private JPanel createWordCard(Word word) {
        JPanel card = new JPanel();
        card.setLayout(new CardLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Kartın köşelerini belirginleştirmek için sınır eklendi
        card.setBackground(new Color(255, 255, 255)); // Mat gri renk eklendi

        JLabel frontLabel = new JLabel(word.getMeanTR());
        JLabel backLabel = new JLabel(word.getWordEN());

        frontLabel.setHorizontalAlignment(JLabel.CENTER); // Etiketin içeriğini yatayda ortala
        backLabel.setHorizontalAlignment(JLabel.CENTER); // Etiketin içeriğini yatayda ortala

        // Font büyüklüğünü artırmak için
        frontLabel.setFont(frontLabel.getFont().deriveFont(Font.BOLD, 16));
        backLabel.setFont(backLabel.getFont().deriveFont(Font.BOLD, 16));

        card.add(frontLabel, "front");
        card.add(backLabel, "back");

        // CardListener, kartın üzerine tıklandığında ön yüzünü gösterir
        card.addMouseListener(new CardClickListener(card));

        return card;
    }

    private class CardClickListener extends MouseAdapter {
        private final JPanel card;

        public CardClickListener(JPanel card) {
            this.card = card;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            CardLayout cardLayout = (CardLayout) card.getLayout();
            cardLayout.next(card);
        }
    }
}
