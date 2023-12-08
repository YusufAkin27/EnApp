import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class EditProfilePage {

    private JFrame frame;
    private JPanel panel;
    private JTextField nameAndSurnameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton updateButton;

    private User user;

    public EditProfilePage(User user) {
        this.user = user;

        frame = new JFrame("Profil Düzenle");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        nameAndSurnameField = new JTextField(user.getNameAndSurname());
        usernameField = new JTextField(user.getUsername());
        passwordField = new JTextField(user.getPassword());

        panel.add(new JLabel("Ad Soyad:"));
        panel.add(nameAndSurnameField);

        panel.add(new JLabel("Kullanıcı Adı:"));
        panel.add(usernameField);

        panel.add(new JLabel("Şifre:"));
        panel.add(passwordField);

        updateButton = new JButton("Bilgilerimi Güncelle");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        panel.add(updateButton);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void updateUser() {
        String newNameAndSurname = nameAndSurnameField.getText();
        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();

        // Kullanıcının kullanıcı adını değiştirdiyse ve eski kütüphane dosyası varsa, dosya adını güncelle
        if (!user.getUsername().equals(newUsername)) {
            renameUserLibraryFile(user.getUsername(), newUsername);
        }

        user.setNameAndSurname(newNameAndSurname);
        user.setUsername(newUsername);
        user.setPassword(newPassword);

        // Kullanıcı bilgilerini dosyaya güncelle
        updateUserInfoInFile(user);

        JOptionPane.showMessageDialog(frame, "Bilgileriniz güncellendi!");

        // MainPage sayfasına dön
        frame.dispose(); // Pencereyi kapat
        new MainPage(user);
    }

    private void updateUserInfoInFile(User user) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");

                if (userInfo.length >= 4 && user.getUsername().equals(userInfo[1])) {
                    // Kullanıcı adı eşleşti, güncellenmiş bilgilerle satırı oluştur
                    StringBuilder updatedLine = new StringBuilder();
                    updatedLine.append(user.getNameAndSurname()).append("/")
                            .append(user.getUsername()).append("/")
                            .append(user.getPassword()).append("/")
                            .append(user.getLevel()).append("/")
                            .append(user.getEmail());
                    updatedLines.add(updatedLine.toString());
                } else {
                    // Kullanıcı adı eşleşmiyorsa, satırı değiştirmeden listeye ekle
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }

        // Dosyayı güncelle
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }


    private void renameUserLibraryFile(String oldUsername, String newUsername) {
        File oldFile = new File("library", oldUsername + "_kutuphane.txt");
        File newFile = new File("library", newUsername + "_kutuphane.txt");

        if (oldFile.exists() && !newFile.exists()) {
            try {
                // Eski dosyayı yeni dosyaya kopyala
                Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                oldFile.delete();
                System.out.println("Kütüphane dosyası adı güncellendi ve içeriği kopyalandı: " + newFile.toString());
            } catch (IOException e) {
                System.err.println("Kütüphane dosyası adı güncellenirken bir hata oluştu: " + e.getMessage());
            }
        }
    }
}