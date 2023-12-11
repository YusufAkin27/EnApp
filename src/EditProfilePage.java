import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class EditProfilePage {

    private JFrame frame;
    private JPanel panel;
    private JTextField nameAndSurnameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField emailField;
    private JButton updateButton;
    private JButton backButton;

    private User user;

    public EditProfilePage(User user) {
        this.user = user;

        frame = new JFrame("Profil Düzenle");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                int w = getWidth();
                int h = getHeight();

                Color color1 = new Color(255, 223, 186); // Başlangıç rengi (açık sarı)
                Color color2 = new Color(255, 182, 193); // Bitiş rengi (pembe)

                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        panel.setLayout(new GridLayout(5, 2));

        nameAndSurnameField = new JTextField(user.getNameAndSurname());
        usernameField = new JTextField(user.getUsername());
        passwordField = new JTextField(user.getPassword());
        emailField = new JTextField(user.getEmail());

        panel.add(new JLabel("Ad Soyad:"));
        panel.add(nameAndSurnameField);

        panel.add(new JLabel("Kullanıcı Adı:"));
        panel.add(usernameField);

        panel.add(new JLabel("E-Posta:"));
        panel.add(emailField);

        panel.add(new JLabel("Şifre:"));
        panel.add(passwordField);

        updateButton = new JButton("Bilgilerimi Güncelle");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        backButton = new JButton("Geri");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToMainPage();
            }
        });

        panel.add(updateButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void updateUser() {
        String newNameAndSurname = nameAndSurnameField.getText();
        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();
        String newEmail = emailField.getText();

        // Şartları kontrol et
        if (!isUsernameValid(newUsername)) {
            JOptionPane.showMessageDialog(frame, "Geçersiz kullanıcı adı! Kullanıcı adı en az 4 karakter uzunluğunda olmalıdır ve sadece harf, rakam ve alt çizgi içerebilir.");
            return;
        }


        if (!isEmailValid(newEmail)) {
            JOptionPane.showMessageDialog(frame, "Geçersiz e-posta adresi!");
            return;
        }

        if (!isPasswordValid(newPassword)) {
            JOptionPane.showMessageDialog(frame, "Geçersiz şifre! Şifre en az 8 karakter uzunluğunda olmalı ve en az bir büyük harf ve bir rakam içermelidir.");
            return;
        }

        if (isUniqueTaken(newUsername, newEmail)) {
            JOptionPane.showMessageDialog(frame, "Kullanıcı adı veya e-posta adresi zaten kullanılmış!");
            return;
        }

        // Kullanıcının ID'sini bul
        long userId = user.getId();

        if (userId != -1) {
            // Kullanıcının eski ID'siyle dosyadan bilgilerini sil
            removeUserInfoFromFile(userId);

            if (!user.getUsername().equals(newUsername)) {
                renameUserLibraryFile(user.getUsername(), newUsername);
            }

            // Yeni bilgileri kullanıcının yeni ID'siyle dosyaya ekle
            user.setNameAndSurname(newNameAndSurname);
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            user.setEmail(newEmail);

            updateUserInfoInFile(user);

            JOptionPane.showMessageDialog(frame, "Bilgileriniz güncellendi!");

            frame.dispose();
            goBackToMainPage();
        } else {
            JOptionPane.showMessageDialog(frame, "Kullanıcı bulunamadı!");
        }
    }

    private void removeUserInfoFromFile(long userId) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("/");

                if (userInfo.length > 0 && Long.parseLong(userInfo[0]) == userId) {
                    continue;
                }

                updatedLines.add(line);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    private void updateUserInfoInFile(User user) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            StringBuilder updatedLine = new StringBuilder();
            updatedLine.append(user.getId()).append("/")
                    .append(user.getNameAndSurname()).append("/")
                    .append(user.getUsername()).append("/")
                    .append(user.getPassword()).append("/")
                    .append(user.getLevel()).append("/")
                    .append(user.getEmail()).append("/")
                    .append(user.getDate());

            writer.write(updatedLine.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    private void renameUserLibraryFile(String oldUsername, String newUsername) {
        File oldFile = new File("library", oldUsername + "_kutuphane.txt");
        File newFile = new File("library", newUsername + "_kutuphane.txt");

        if (oldFile.exists() && !newFile.exists()) {
            try {
                Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                oldFile.delete();
                System.out.println("Kütüphane dosyası adı güncellendi ve içeriği kopyalandı: " + newFile.toString());
            } catch (IOException e) {
                System.err.println("Kütüphane dosyası adı güncellenirken bir hata oluştu: " + e.getMessage());
            }
        }
    }

    private boolean isUniqueTaken(String kullaniciAdi, String email) {
        Path dosyaYolu = Paths.get("kullanici_bilgileri.txt");
        try (BufferedReader okuyucu = new BufferedReader(new FileReader(dosyaYolu.toFile()))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                String[] kullaniciBilgisi = satir.split("/");

                if (kullaniciBilgisi.length > 1) {
                    String kullaniciAdiDosyadan = kullaniciBilgisi[2]; // Kullanıcı adı dosyada üçüncü sırada
                    String emailDosyadan = kullaniciBilgisi[5];

                    // Check uniqueness only if the username or email is different from the current user's data
                    if (!kullaniciAdiDosyadan.equals(user.getUsername()) || !emailDosyadan.equals(user.getEmail())) {
                        if (kullaniciAdi.equals(kullaniciAdiDosyadan) || email.equals(emailDosyadan)) {
                            return true; // Kullanıcı adı veya email daha önce kullanılmış
                        }
                    }
                } else {
                    System.err.println("Dosyada geçersiz bir satır bulundu: " + satir);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        return false; // Kullanıcı adı veya email kullanılmamış
    }

    private boolean isUsernameValid(String kullaniciAdi) {
        return kullaniciAdi.length() >= 4 && kullaniciAdi.matches("[a-zA-Z0-9_]+") && Character.isLetterOrDigit(kullaniciAdi.charAt(0));
    }

    private boolean isEmailValid(String email) {
        return email.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
    }

    private boolean isPasswordValid(String sifre) {
        return sifre.length() >= 8 && sifre.matches(".*[A-Z]+.*") && sifre.matches(".*\\d+.*");
    }

    private void goBackToMainPage() {
        frame.dispose();
        new MainPage(user);
    }
}
