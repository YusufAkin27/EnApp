import level.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String nameAndSurname;
    private String username;
    private String password;
    private List<Word>library;
    private String level;
    private String email;


    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Word> getLibrary() {
        return library;
    }

    public void setLibrary(List<Word> library) {
        this.library = library;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // User sınıfındaki levelUp metodu
    public void levelUp() {
        switch (level) {
            case "A1":
                level = "A2";
                break;
            case "A2":
                level = "B1";
                break;
            case "B1":
                level = "B2";
                break;
            case "B2":
                level = "C1";
                break;
            case "C1":
                level = "C2";
                break;
            case "C2":
                break;
            default:
                throw new IllegalArgumentException("Geçersiz seviye: " + level);
        }

        // Yeni seviyenin kelime listesini yükle
        loadWordList();
    }

    // loadWordList metodunu User sınıfına ekle
    public List<String> loadWordList() {
        List<String> wordList = new ArrayList<>();

        switch (level) {
            case "A1":
                wordList.addAll(new A1Words().getWords().keySet());
                break;
            case "A2":
                wordList.addAll(new A2Words().getWords().keySet());
                break;
            case "B1":
                wordList.addAll(new B1Words().getWords().keySet());
                break;
            case "B2":
                wordList.addAll(new B2Words().getWords().keySet());
                break;
            case "C1":
                wordList.addAll(new C1Words().getWords().keySet());
                break;
            case "C2":
                wordList.addAll(new C2Words().getWords().keySet());
                break;
            default:
                throw new IllegalArgumentException("Geçersiz seviye: " + level);
        }

        return wordList;
    }


    public void updateUser(User user) {
        Path filePath = Paths.get("kullanici_bilgileri.txt");
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userInfo = line.split(",");

                if (userInfo.length > 3 && user.getUsername().equals(userInfo[1])) {
                    // Kullanıcı adı eşleşti, güncellenmiş bilgilerle satırı oluştur
                    String updatedLine = user.getNameAndSurname() + "," +
                            user.getUsername() + "," +
                            user.getPassword() + "," +
                            user.getLevel();
                    updatedLines.add(updatedLine);
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



}
