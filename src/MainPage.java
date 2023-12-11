import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainPage {

    private JFrame frame;
    private JPanel panel;
    private JButton myInfoButton;
    private JButton dictionaryButton;
    private JButton quizButton;
    private JButton profileButton;
    private JButton logoutButton;
    private JLabel remainingTimeLabel;

    private User user;
    private Date loginTime;

    public MainPage(User user) {
        this.user = user;
        this.loginTime = new Date();
        frame = new JFrame("Ana Sayfa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

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
        myInfoButton.setBounds(50, 50, 200, 40);
        myInfoButton.addActionListener(e -> {
            frame.dispose();
            new EditProfilePage(user);
        });
        panel.add(myInfoButton);

        dictionaryButton = createStyledButton("Sözlük");
        dictionaryButton.setBounds(50, 110, 200, 40);
        dictionaryButton.addActionListener(e -> {
            frame.dispose();
            new LibraryPage(user);
        });
        panel.add(dictionaryButton);

        quizButton = createStyledButton("QUIZ");
        quizButton.setBounds(50, 170, 200, 40);
        quizButton.addActionListener(e -> {
            if (canUserClickQuizButton(user.getId())) {
                frame.dispose();
                new QuizPage(user);
                updateLastClickTime(user.getId());
            } else {
                showMessage("Günlük tıklama limitine ulaşıldı. Yarın tekrar deneyin.");
            }
        });
        panel.add(quizButton);

        profileButton = createRoundButton("Profilim");
        profileButton.setBounds(650, 20, 130, 40);
        panel.add(profileButton);


        logoutButton = createRedButton("Çıkış");
        logoutButton.setBounds(650, 500, 100, 40);
        logoutButton.addActionListener(e -> showConfirmation("Çıkış yapmak istediğinize emin misiniz?"));
        panel.add(logoutButton);

        remainingTimeLabel = new JLabel("Kalan Süre: 60 saniye");
        remainingTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        remainingTimeLabel.setForeground(Color.RED);
        remainingTimeLabel.setBounds(400, 170, 400, 40);
        panel.add(remainingTimeLabel);

        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        Timer timer = new Timer(1000, e -> updateRemainingTime(user.getId()));
        timer.start();
    }

    private JButton createRoundButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false); // Kenar çizgisini kaldırır
        button.setBorderPainted(false); // Dış kenar çizgisini kaldırır
        button.setOpaque(true); // Arka plan rengini gösterir
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Boşluk ekler, daha yuvarlak görünüm için

        return button;
    }

    private void updateRemainingTime(long userId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("kullanici_bilgileri.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("/");
                int id = Integer.parseInt(userInfo[0]);

                if (id == userId) {
                    // Kullanıcının bilgilerini bulduk, şimdi zamanı alalım
                    String lastClickTimeStr = userInfo[6].trim(); // Trim to handle leading/trailing whitespaces

                    // Check if the date string is not blank
                    if (!lastClickTimeStr.isEmpty()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                        try {
                            Date lastClickTime = dateFormat.parse(lastClickTimeStr);

                            // Rest of your code...
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Handle parsing exception
                        }
                    } else {
                        System.err.println("Last click time is blank for user " + userId);
                    }

                    break; // Gerekli bilgiyi bulduktan sonra döngüden çık
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // updateLastClickTime metodunun içinde
    private void updateLastClickTime(long userId) {
        File file = new File("kullanici_bilgileri.txt");

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            String line;
            long lastPosition = 0;

            while ((line = raf.readLine()) != null) {
                String[] userInfo = line.split("/");
                int id = Integer.parseInt(userInfo[0]);

                if (id == userId) {
                    // Kullanıcının bilgilerini güncelle, en son tıklama zamanını ekleyerek
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String updatedLine = userInfo[0] + "/" + userInfo[1] + "/" + userInfo[2] + "/" + userInfo[3] + "/" +
                            userInfo[4] + "/" + userInfo[5] + "/" + dateFormat.format(new Date());

                    // Güncellenmiş satırı dosyaya yaz
                    raf.seek(lastPosition);
                    raf.writeBytes(updatedLine);

                    // QUIZ butonuna basıldığı tarihi user nesnesine setle
                    user.setDate(new Date());
                    break;
                }

                lastPosition = raf.getFilePointer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private boolean canUserClickQuizButton(long userId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("kullanici_bilgileri.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split("/");
                int id = Integer.parseInt(userInfo[0]);

                if (id == userId) {
                    String lastClickTimeStr = userInfo[6];
                    if (lastClickTimeStr.equals("null")) {
                        return true;
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date lastClickTime = dateFormat.parse(lastClickTimeStr);
                        Date now = new Date();

                        user.setDate(now);
                        long elapsedTime = now.getTime() - lastClickTime.getTime();
                        long oneDayInMillis = 24 * 60 * 60 * 1000;

                        if (elapsedTime > oneDayInMillis) {
                            return true;  // Bir gün geçmişse, kullanıcı tekrar tıklayabilir
                        } else {
                            return false;  // Bir gün geçmemişse, kullanıcı tıklamamalı
                        }
                    }
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return false;  // Kullanıcı bulunamadı veya bir hata oluştu
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainPage(new User()));
    }
}
