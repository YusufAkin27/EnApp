import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterPage {

    private static int idCounter = 0;

    private int id;
    private String nameAndSurname;
    private String username;
    private String password;
    private String level;
    private String email;

    private JFrame frame;
    private JTextField adSoyadField;
    private JTextField kullaniciAdiField;
    private JTextField emailField;
    private JPasswordField sifreField;
    private JButton kayitButton;
    private JButton girisButton;
    private JLabel sonucEtiketi;

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

        JLabel baslikEtiketi = new JLabel("KAYIT OL");
        baslikEtiketi.setForeground(Color.BLACK);
        Font etiketFontu = baslikEtiketi.getFont();
        baslikEtiketi.setFont(new Font(etiketFontu.getName(), Font.BOLD, 24));
        baslikEtiketi.setBounds(250, 20, 150, 30);
        panel.add(baslikEtiketi);

        JLabel adSoyadEtiketi = new JLabel("Ad Soyad:");
        adSoyadEtiketi.setForeground(Color.RED);
        Font adSoyadFontu = new Font("SansSerif", Font.PLAIN, 16);
        adSoyadEtiketi.setFont(adSoyadFontu);
        adSoyadEtiketi.setBounds(120, 80, 100, 30);
        panel.add(adSoyadEtiketi);

        adSoyadField = new JTextField();
        adSoyadField.setBounds(220, 80, 300, 30);
        adSoyadField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(adSoyadField);

        JLabel kullaniciAdiEtiketi = new JLabel("Kullanıcı Adı:");
        kullaniciAdiEtiketi.setForeground(Color.RED);
        Font kullaniciAdiFontu = new Font("SansSerif", Font.PLAIN, 16);
        kullaniciAdiEtiketi.setFont(kullaniciAdiFontu);
        kullaniciAdiEtiketi.setBounds(120, 120, 100, 30);
        panel.add(kullaniciAdiEtiketi);

        kullaniciAdiField = new JTextField();
        kullaniciAdiField.setBounds(220, 120, 300, 30);
        kullaniciAdiField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(kullaniciAdiField);

        JLabel emailEtiketi = new JLabel("Email:");
        emailEtiketi.setForeground(Color.RED);
        Font emailFontu = new Font("SansSerif", Font.PLAIN, 16);
        emailEtiketi.setFont(emailFontu);
        emailEtiketi.setBounds(120, 160, 100, 30);
        panel.add(emailEtiketi);

        emailField = new JTextField();
        emailField.setBounds(220, 160, 300, 30);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(emailField);

        JLabel sifreEtiketi = new JLabel("Şifre:");
        sifreEtiketi.setForeground(Color.RED);
        Font sifreFontu = new Font("SansSerif", Font.PLAIN, 16);
        sifreEtiketi.setFont(sifreFontu);
        sifreEtiketi.setBounds(120, 200, 100, 30);
        panel.add(sifreEtiketi);

        sifreField = new JPasswordField();
        sifreField.setBounds(220, 200, 300, 30);
        sifreField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(sifreField);

        kayitButton = new JButton("Kayıt Ol");
        kayitButton.setBackground(Color.GREEN);
        kayitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adSoyad = adSoyadField.getText();
                String kullaniciAdi = kullaniciAdiField.getText();
                String sifre = new String(sifreField.getPassword());
                String email = emailField.getText();

                if (!isUniqueTaken(kullaniciAdi, email)) {
                    if (isEmailValid(email) && isPasswordValid(sifre)) {
                        if (isUsernameValid(kullaniciAdi)) {
                            saveUserToTextFile(adSoyad, kullaniciAdi, sifre, email);
                            createUserLibraryFile(kullaniciAdi);
                            sonucuGoster("Kayıt işlemi başarılı.");
                        } else {
                            sonucuGoster("Kullanıcı adı kurallarına uyunuz: En az 4 karakter uzunluğunda olmalı, sadece harf, rakam ve alt çizgi içermelidir, harf veya rakam ile başlamalıdır.");
                        }
                    } else {
                        sonucuGoster("Geçersiz email adresi veya şifre. Lütfen kontrol edip tekrar deneyin.");
                    }
                } else {
                    sonucuGoster("Bu kullanıcı adı veya email zaten alınmış.");
                }
            }
        });
        kayitButton.setBounds(220, 240, 150, 40);
        panel.add(kayitButton);

        girisButton = new JButton("Giriş Yap");
        girisButton.setBackground(Color.ORANGE);
        girisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginPage();
            }
        });
        girisButton.setBounds(380, 240, 150, 40);
        panel.add(girisButton);

        sonucEtiketi = new JLabel();
        sonucEtiketi.setBounds(220, 300, 300, 30);
        panel.add(sonucEtiketi);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private boolean isUniqueTaken(String kullaniciAdi, String email) {
        Path dosyaYolu = Paths.get("kullanici_bilgileri.txt");

        try (BufferedReader okuyucu = new BufferedReader(new FileReader(dosyaYolu.toFile()))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                String[] kullaniciBilgisi = satir.split("/");

                if (kullaniciBilgisi.length > 1) {
                    String kullaniciAdiDosyadan = kullaniciBilgisi[2]; // Kullanıcı adı dosyada üçüncü sırada
                    String emailDosyadan = kullaniciBilgisi[4];
                    if (kullaniciAdi.equals(kullaniciAdiDosyadan) || email.equals(emailDosyadan)) {
                        return true; // Kullanıcı adı veya email daha önce kullanılmış
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

    private void saveUserToTextFile(String adSoyad, String kullaniciAdi, String sifre, String email) {
        this.id = generateId();
        this.nameAndSurname = adSoyad;
        this.username = kullaniciAdi;
        this.password = sifre;
        this.level = "A1";
        this.email = email;

        Path dosyaYolu = Paths.get("kullanici_bilgileri.txt");

        try (BufferedWriter yazici = new BufferedWriter(new FileWriter(dosyaYolu.toFile(), true))) {
            yazici.write(this.id + "/" + this.nameAndSurname + "/" + this.username + "/" + this.password + "/" + this.level + "/" + this.email);
            yazici.newLine();
            sonucuGoster("Kullanıcı bilgileri başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("Kullanıcı bilgilerini dosyaya kaydederken bir hata oluştu: " + e.getMessage());
        }
    }

    private int generateId() {
        synchronized (RegisterPage.class) {
            return ++idCounter;
        }
    }

    private void createUserLibraryFile(String kullaniciAdi) {
        Path kutuphaneKlasorYolu = Paths.get("library");

        if (!kutuphaneKlasorYolu.toFile().exists()) {
            try {
                Files.createDirectories(kutuphaneKlasorYolu);
            } catch (IOException e) {
                System.err.println("Kütüphane klasörü oluşturulurken bir hata oluştu: " + e.getMessage());
                return;
            }
        }

        Path kullaniciKutuphaneYolu = Paths.get("library", kullaniciAdi + "_kutuphane.txt");

        try {
            if (kullaniciKutuphaneYolu.toFile().createNewFile()) {
                System.out.println("Kullanıcı kütüphanesi dosyası oluşturuldu: " + kullaniciKutuphaneYolu.toString());
            } else {
                System.out.println("Kullanıcı kütüphanesi dosyası zaten var veya oluşturulamadı.");
            }
        } catch (IOException e) {
            System.err.println("Kullanıcı kütüphanesi dosyasını oluştururken bir hata oluştu: " + e.getMessage());
        }
    }

    private void sonucuGoster(String mesaj) {
        sonucEtiketi.setText("");
        sonucEtiketi.setText(mesaj);
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
