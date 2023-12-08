import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage {

    private JFrame frame;
    private JPanel panel;
    private JButton myInfoButton;
    private JButton dictionaryButton;
    private JButton quizButton;
    private JButton profileButton;
    private JButton logoutButton;

    public MainPage(User user) {
        frame = new JFrame("Ana Sayfa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(35, 137, 218), w, h, new Color(173, 216, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        panel.setLayout(null);

        myInfoButton = createStyledButton("Profil Düzenle");
        myInfoButton.setBounds(50, 50, 220, 40);
        myInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             frame.dispose();
             new EditProfilePage(user);
            }
        });
        panel.add(myInfoButton);

        dictionaryButton = createStyledButton("Sözlük");
        dictionaryButton.setBounds(50, 110, 220, 40);
        dictionaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LibraryPage(user);
            }
        });
        panel.add(dictionaryButton);

        quizButton = createStyledButton("QUIZ");
        quizButton.setBounds(50, 170, 220, 40);
        quizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new QuizPage(user);
            }
        });
        panel.add(quizButton);

        profileButton = createRoundButton("Profilim");
        profileButton.setBounds(500, 20, 80, 80);
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage(user.getLevel());
            }
        });
        panel.add(profileButton);

        logoutButton = createRedButton("Çıkış");
        logoutButton.setBounds(500, 400, 80, 40);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfirmation("Çıkış yapmak istediğinize emin misiniz?");
            }
        });
        panel.add(logoutButton);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setBackground(new Color(124, 190, 190));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        return button;
    }

    private JButton createRedButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setBackground(new Color(220, 20, 60));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        return button;
    }


    private JButton createRoundButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setBackground(new Color(124, 190, 190));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setSize(40, 40);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        return button;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private void showConfirmation(String message) {
        int option = JOptionPane.showConfirmDialog(frame, message, "Çıkış Onayı", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            frame.dispose();
            System.out.println("Çıkış yapılıyor...");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainPage(new User());
            }
        });
    }
}
