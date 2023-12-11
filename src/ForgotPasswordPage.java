import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 180), 0, h, new Color(255, 255, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panel.setLayout(new GridLayout(5, 2, 10, 10)); // Satır sayısını 5'e çıkardık
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Metin boyutunu büyüttük
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14)); // Metin boyutunu büyüttük
        panel.add(usernameField);

        JLabel emailLabel = new JLabel("E-posta:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Metin boyutunu büyüttük
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14)); // Metin boyutunu büyüttük
        panel.add(emailField);

        JLabel newPasswordLabel = new JLabel("Yeni Şifre:");
        newPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Metin boyutunu büyüttük
        panel.add(newPasswordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Arial", Font.PLAIN, 14)); // Metin boyutunu büyüttük
        panel.add(newPasswordField);

        backButton = new JButton("Giriş Sayfasına Dön");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                SwingUtilities.invokeLater(() -> new LoginPage());
            }
        });
        panel.add(backButton);

        resetPasswordButton = new JButton("Şifreyi Sıfırla");
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Boş alan kontrolü
                if (usernameField.getText().isEmpty() || emailField.getText().isEmpty() || new String(newPasswordField.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                } else {
                    String enteredUsername = usernameField.getText();
                    String enteredEmail = emailField.getText();
                    String newPassword = new String(newPasswordField.getPassword());

                    if (resetPassword(enteredUsername, enteredEmail, newPassword)) {
                        JOptionPane.showMessageDialog(frame, "Şifre sıfırlama başarılı.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        SwingUtilities.invokeLater(() -> new LoginPage());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Kullanıcı adı veya e-posta hatalı veya böyle bir kullanıcı bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(resetPasswordButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private boolean resetPassword(String enteredUsername, String enteredEmail, String newPassword) {
        try {
            String dosyaYolu = "kullanici_bilgileri.txt";
            File dosya = new File(dosyaYolu);

            BufferedReader br = new BufferedReader(new FileReader(dosya));
            String satir;
            StringBuilder yeniIcerik = new StringBuilder();
            boolean kullaniciBulundu = false;

            while ((satir = br.readLine()) != null) {
                String[] bilgiler = satir.split("/");

                if (bilgiler.length >= 6 && bilgiler[2].equals(enteredUsername) && bilgiler[5].equals(enteredEmail)) {
                    kullaniciBulundu = true;
                    if (isPasswordValid(newPassword)) {
                        bilgiler[3] = newPassword;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Şifre en az 8 karakter uzunluğunda olmalı, en az bir büyük harf ve bir sayı içermelidir.", "Şifre Hatası", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }

                yeniIcerik.append(String.join("/", bilgiler)).append("\n");
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(dosya));
            bw.write(yeniIcerik.toString());
            bw.close();

            return kullaniciBulundu;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z]+.*") && password.matches(".*\\d+.*");
    }


}
