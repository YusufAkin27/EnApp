import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ForgotPasswordPage {

    private JFrame frame;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField newPasswordField;
    private JButton resetPasswordButton;
    private JButton backButton;

    public ForgotPasswordPage() {
        frame = new JFrame("Şifre Sıfırlama");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        panel.add(usernameLabel);

        usernameField = new JTextField();
        panel.add(usernameField);

        JLabel emailLabel = new JLabel("E-posta:");
        panel.add(emailLabel);

        emailField = new JTextField();
        panel.add(emailField);

        JLabel newPasswordLabel = new JLabel("Yeni Şifre:");
        panel.add(newPasswordLabel);

        newPasswordField = new JPasswordField();
        panel.add(newPasswordField);

        resetPasswordButton = new JButton("Şifreyi Sıfırla");
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredEmail = emailField.getText();
                String newPassword = new String(newPasswordField.getPassword());

                if (resetPassword(enteredUsername, enteredEmail, newPassword)) {
                    JOptionPane.showMessageDialog(frame, "Şifre sıfırlama başarılı.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose(); // Şifre sıfırlama penceresini kapat
                } else {
                    JOptionPane.showMessageDialog(frame, "Kullanıcı adı veya e-posta hatalı.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(resetPasswordButton);

        backButton = new JButton("Giriş Sayfasına Dön");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginPage();
            }
        });
        panel.add(backButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private boolean resetPassword(String enteredUsername, String enteredEmail, String newPassword) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
             BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("/");

                if (userInfo.length > 4) {
                    String usernameFromFile = userInfo[1].trim();
                    String emailFromFile = userInfo[4].trim();

                    if (enteredUsername.equals(usernameFromFile) && enteredEmail.equals(emailFromFile)) {
                        // Kullanıcı adı ve e-posta doğrulandı, yeni şifreyi güncelle
                        userInfo[2] = newPassword;
                    }
                }

                // Dosyaya satırı yaz
                writer.write(String.join("/", userInfo));
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Dosya işlemleri hatası: " + e.getMessage());
            return false;
        }

        // Dosya isimlerini değiştirerek güncellenmiş dosyayı orijinal adıyla değiştir
        File oldFile = new File("temp.txt");
        File newFile = new File("kullanici_bilgileri.txt");
        return oldFile.renameTo(newFile);
    }

    public static void main(String[] args) {
        new ForgotPasswordPage();
    }
}
