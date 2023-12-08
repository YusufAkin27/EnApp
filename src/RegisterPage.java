import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterPage {

    private JFrame frame;
    private JTextField nameAndSurnameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel resultLabel;

    public RegisterPage() {
        frame = new JFrame("Kayıt Formu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(184, 83, 232), w, h, Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        panel.setLayout(null);
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        JLabel mainLabel = new JLabel("KAYIT OL");
        mainLabel.setForeground(Color.BLACK);
        Font labelFont = mainLabel.getFont();
        mainLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 21));
        mainLabel.setBounds(250, 20, 100, 220);
        panel.add(mainLabel);

        JLabel nameLabel = new JLabel("Ad Soyad:");
        nameLabel.setForeground(Color.RED);
        nameLabel.setBounds(120, 240, 100, 30);
        panel.add(nameLabel);

        nameAndSurnameField = new JTextField();
        nameAndSurnameField.setBounds(200, 240, 200, 30);
        panel.add(nameAndSurnameField);

        JLabel usernameLabel = new JLabel("Kullanıcı Adı:");
        usernameLabel.setForeground(Color.RED);
        usernameLabel.setBounds(120, 280, 100, 30);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 280, 200, 30);
        panel.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.RED);
        emailLabel.setBounds(120, 320, 100, 30);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(200, 320, 200, 30);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Şifre:");
        passwordLabel.setForeground(Color.RED);
        passwordLabel.setBounds(120, 360, 100, 30);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 360, 200, 30);
        panel.add(passwordField);

        registerButton = new JButton("Kayıt Ol");
        registerButton.setBackground(Color.GREEN);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameAndSurname = nameAndSurnameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();

                // Kullanıcı adının daha önce kullanılıp kullanılmadığını kontrol et
                if (!isUniqueTaken(username, email)) {
                    // Şifre kontrolü

                    // Email uniq kontrol ve email format kontrolü
                    if (isEmailValid(email) && isPasswordValid(password)) {
                        // Kullanıcı adı kontrolü
                        if (isUsernameValid(username)) {
                            // Kullanıcı adı kullanılmamışsa ve şifre uygunsa, verileri metin belgesine kaydet
                            saveUserToTextFile(nameAndSurname, username, password, email);
                            createUserLibraryFile(username); // Her kullanıcı için bir kelime kütüphanesi dosyası oluştur
                            displayResult("Kayıt işlemi başarılı.");
                        } else {
                            displayResult("Kullanıcı adı kurallarına uyunuz: En az 4 karakter uzunluğunda olmalı, sadece harf, rakam ve alt çizgi içermelidir, harf veya rakam ile başlamalıdır.");
                        }
                    } else {
                        displayResult("Geçersiz email adresi veya şifre. Lütfen kontrol edip tekrar deneyin.");
                    }
                } else {
                    // Kullanıcı adı veya email daha önce kullanılmışsa, kullanıcıya bilgi ver
                    displayResult("Bu kullanıcı adı veya email zaten alınmış.");
                }
            }
        });
        registerButton.setBounds(200, 400, 100, 30);
        panel.add(registerButton);

        loginButton = new JButton("Giriş Yap");
        loginButton.setBackground(Color.ORANGE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginPage();
            }
        });
        loginButton.setBounds(320, 400, 100, 30);
        panel.add(loginButton);

        resultLabel = new JLabel();
        resultLabel.setBounds(200, 440, 300, 30);
        panel.add(resultLabel);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private boolean isUniqueTaken(String username, String email) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("/");

                // userInfo dizisinin boyutunu kontrol et
                if (userInfo.length > 1) {
                    String usernameFromFile = userInfo[1]; // Kullanıcı adı dosyada ikinci sırada
                    String emailFromFile = userInfo[4];
                    if (username.equals(usernameFromFile) || email.equals(emailFromFile)) {
                        return true; // Kullanıcı adı veya email daha önce kullanılmış
                    }
                } else {
                    System.err.println("Dosyada geçersiz bir satır bulundu: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        return false; // Kullanıcı adı veya email kullanılmamış
    }

    private boolean isUsernameValid(String username) {
        // Kullanıcı adı en az 4 karakter uzunluğunda olmalıdır.
        // Sadece harf, rakam ve alt çizgi (_) karakterleri içermelidir.
        // Harf veya rakam ile başlamalıdır.
        return username.length() >= 4 && username.matches("[a-zA-Z0-9_]+") && Character.isLetterOrDigit(username.charAt(0));
    }

    private boolean isEmailValid(String email) {
        // Basit bir email format kontrolü
        return email.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
    }

    private boolean isPasswordValid(String password) {
        // Şifre en az 8 karakter uzunluğunda olmalı, en az bir büyük harf ve bir sayı içermelidir.
        return password.length() >= 8 && password.matches(".*[A-Z]+.*") && password.matches(".*\\d+.*");
    }

    private void saveUserToTextFile(String nameAndSurname, String username, String password, String email) {
        User user = new User();
        Path filePath = Paths.get("kullanici_bilgileri.txt");
        user.setLevel("A1");
        user.setLibrary(null);
        user.setPassword(password);
        user.setNameAndSurname(nameAndSurname);
        user.setUsername(username);
        user.setEmail(email);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
            writer.write(user.getNameAndSurname() + "/" + user.getUsername() + "/" + user.getPassword() + "/" + user.getLevel() + "/" + user.getEmail());
            writer.newLine();
            displayResult("Kullanıcı bilgileri başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("Kullanıcı bilgilerini dosyaya kaydederken bir hata oluştu: " + e.getMessage());
        }
    }

    private void createUserLibraryFile(String username) {
        // Kullanıcının kelime kütüphanesi dosyasını oluştur
        Path libraryFolderPath = Paths.get("library");

        // "library" klasörü yoksa oluştur
        if (!libraryFolderPath.toFile().exists()) {
            try {
                Files.createDirectories(libraryFolderPath);
            } catch (IOException e) {
                System.err.println("Kütüphane klasörü oluşturulurken bir hata oluştu: " + e.getMessage());
                return; // Hata durumunda metodu sonlandır
            }
        }

        Path userLibraryPath = Paths.get("library", username + "_kutuphane.txt");

        try {
            if (userLibraryPath.toFile().createNewFile()) {
                System.out.println("Kullanıcı kütüphanesi dosyası oluşturuldu: " + userLibraryPath.toString());
            } else {
                System.out.println("Kullanıcı kütüphanesi dosyası zaten var veya oluşturulamadı.");
            }
        } catch (IOException e) {
            System.err.println("Kullanıcı kütüphanesi dosyasını oluştururken bir hata oluştu: " + e.getMessage());
        }
    }

    private void displayResult(String message) {
        clearResultLabel(); // Sonuç etiketini temizle
        resultLabel.setText(message);
    }

    private void clearResultLabel() {
        resultLabel.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterPage();
            }
        });
    }
}
