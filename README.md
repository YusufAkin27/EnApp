# EnApp – İngilizce Öğrenme Uygulaması

Java Swing ile geliştirilmiş masaüstü İngilizce öğrenme uygulaması. Kelime dağarcığı, quiz ve oyunlarla İngilizce pratik yapmanızı sağlar.

## Özellikler

- **Kullanıcı yönetimi**: Kayıt, giriş, şifremi unuttum
- **Profil**: Ad, e-posta ve seviye düzenleme
- **Sözlük (Kütüphane)**: Seviyeye göre kelime listeleri
- **Quiz**: Günlük limitli kelime quizleri
- **Oyunlar**: Kelime ve cümle oyunları
- **Seviye sistemi**: A1, A2, B1, B2, C1, C2 (CEFR)

## Gereksinimler

- **Java JDK** 8 veya üzeri
- IDE (IntelliJ IDEA önerilir) veya komut satırı

## Proje Yapısı

```
EnApp/
├── src/
│   ├── LoginPage.java        # Giriş sayfası (uygulama giriş noktası)
│   ├── RegisterPage.java     # Kayıt sayfası
│   ├── ForgotPasswordPage.java
│   ├── MainPage.java         # Ana sayfa
│   ├── EditProfilePage.java  # Profil düzenleme
│   ├── LibraryPage.java     # Sözlük / kütüphane
│   ├── QuizPage.java        # Quiz ekranı
│   ├── GamePage.java        # Oyun menüsü
│   ├── WordGamePage.java    # Kelime oyunu
│   ├── SentenceGamePage.java # Cümle oyunu
│   ├── User.java            # Kullanıcı modeli
│   ├── Word.java            # Kelime modeli
│   └── level/
│       ├── A1Words.java ... C2Words.java  # Seviye kelime listeleri
│       ├── GameWords.java
│       └── SentenceGenerator.java
├── library/                  # Kütüphane metin dosyaları
└── kullanici_bilgileri.txt   # Kullanıcı verileri (çalışma zamanında)
```

## Kurulum ve Çalıştırma

### IDE ile (IntelliJ IDEA)

1. Projeyi **File → Open** ile açın.
2. `src` klasörünü kaynak klasörü olarak işaretleyin (genelde otomatik tanınır).
3. **Run** için ana sınıf olarak `LoginPage` seçin.
4. **Run 'LoginPage.main()'** ile uygulamayı başlatın.

### Komut satırı ile

Proje kök dizininde (`EnApp` klasöründe):

```bash
# Derleme
javac -d out -encoding UTF-8 src/*.java src/level/*.java

# Çalıştırma (LoginPage ana sınıf)
java -cp out LoginPage
```

Windows PowerShell:

```powershell
javac -d out -encoding UTF-8 src/*.java src/level/*.java
java -cp out LoginPage
```

> **Not:** Çalıştırırken çalışma dizininin proje kökü (`EnApp`) olması gerekir; `kullanici_bilgileri.txt` ve `library` buradan okunur.

## Kullanım

1. Uygulama açıldığında **Giriş Yap** ekranı gelir.
2. Hesabınız yoksa **Kayıt Ol** ile yeni kullanıcı oluşturun.
3. Giriş yaptıktan sonra ana sayfadan:
   - **Profil Düzenle**: Ad, e-posta, seviye
   - **Sözlük**: Kelime listeleri ve anlamları
   - **QUIZ**: Günlük quiz (limit uygulanır)
   - **Oyunlar**: Kelime ve cümle oyunları
4. İşiniz bitince **Çıkış** ile ana sayfadan çıkın.

## Veri Dosyaları

- **kullanici_bilgileri.txt**: Kullanıcı adı, şifre, seviye vb. (uygulama tarafından oluşturulur/güncellenir)
- **library/*.txt**: Kelime kütüphane dosyaları

## Lisans

Bu proje eğitim amaçlı geliştirilmiştir.
