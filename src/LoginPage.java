import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoginPage {

    private JFrame frame;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton forgotPasswordButton; // Yeni eklendi
    private JLabel resultLabel;

    public LoginPage() {
        frame = new JFrame("Giriş Sayfası");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(75, 0, 130), w, h, Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panel.setLayout(new GridLayout(6, 1, 10, 10)); // 6 satır, 1 sütunlu bir GridLayout
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel mainLabel = new JLabel("Giriş Yap");
        mainLabel.setForeground(new Color(255, 255, 255)); // Beyaz renk
        Font labelFont = mainLabel.getFont();
        mainLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 32));
        panel.add(mainLabel);

        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Kullanıcı Adı"));
        Font fieldFont = usernameField.getFont();
        usernameField.setFont(new Font(fieldFont.getName(), Font.PLAIN, 16));
        panel.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Şifre"));
        passwordField.setFont(new Font(fieldFont.getName(), Font.PLAIN, 16));
        panel.add(passwordField);

        loginButton = new JButton("Giriş Yap");
        loginButton.setBackground(new Color(255, 165, 0)); // Turuncu renk
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                if (checkCredentials(enteredUsername, enteredPassword)) {
                    User user = createUserObject(enteredUsername);
                    frame.dispose();
                    new MainPage(user);
                } else {
                    displayResult("Giriş başarısız. Kullanıcı adı veya şifre hatalı.");
                }
            }
        });
        panel.add(loginButton);

        registerButton = new JButton("Kayıt Ol");
        registerButton.setBackground(new Color(0, 128, 0)); // Yeşil renk
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new RegisterPage();
            }
        });
        panel.add(registerButton);

        forgotPasswordButton = new JButton("Şifremi Unuttum"); // Yeni eklendi
        forgotPasswordButton.setForeground(new Color(0, 0, 255)); // Mavi renk
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ForgotPasswordPage();
            }
        });
        panel.add(forgotPasswordButton);

        resultLabel = new JLabel();
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(resultLabel);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void showForgotPasswordPage() {
        // Burada şifre sıfırlama sayfasının tasarımı ve mantığı oluşturulabilir.
        // Örneğin, yeni bir pencere açabilir veya mevcut pencere içinde panel değiştirebilirsiniz.
        // Aşağıda sadece bir mesaj gösterimi bulunmaktadır.

        JOptionPane.showMessageDialog(frame, "Şifrenizi sıfırlamak için sistem yöneticinize başvurun.", "Şifre Sıfırlama", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean checkCredentials(String enteredUsername, String enteredPassword) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");

                if (userInfo.length > 3) {
                    String usernameFromFile = userInfo[1];
                    String passwordFromFile = userInfo[2];

                    if (enteredUsername.equals(usernameFromFile) && enteredPassword.equals(passwordFromFile)) {
                        displayResult("Giriş başarılı. Hoş geldiniz, " + enteredUsername + "!");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        displayResult("Giriş başarısız. Kullanıcı adı veya şifre hatalı.");
        return false;
    }

    private User createUserObject(String enteredUsername) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");
        User user = new User();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("/");

                if (userInfo.length > 3) {
                    String usernameFromFile = userInfo[1];
                    if (enteredUsername.equals(usernameFromFile)) {
                        user.setNameAndSurname(userInfo[0]);
                        user.setUsername(userInfo[1]);
                        user.setPassword(userInfo[2]);
                        user.setLevel(userInfo[3]);
                        user.setEmail(userInfo[4]);
                        return user;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        return null;
    }

    private void displayResult(String message) {
        resultLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>");
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
