**[Türkçe](#turkce) | [English](#english)**

# <a name="turkce"></a>Kotlin ile Yazılmış Haber Uygulaması

Haber Uygulaması, kullanıcıların en son haberleri keşfedebileceği, favori haberlerini
kaydedebileceği ve kişiselleştirilmiş ayarlarını yönetebileceği modern arayüze sahip, MVVM mimarisi
ile yazılmış bir mobil uygulamadır. Uygulama, kullanıcı deneyimini ve etkileşimi artırmak için
çeşitli özellikler içermektedir. İşte bu uygulamanın sunduğu özellikler:

## Özellikler

- **Firebase Authentication**
    - Kullanıcıların authentication işlemleri(kayıt, giriş ve e-posta doğrulama işlemi) Firebase
      Authentication ile yönetilir.

- **Firebase Firestore**
    - Kullanıcı bilgilerini (isim, soyisim) depolama ve güncelleme işlemleri Firebase Firestore ile
      yönetilir.

- **Haber Verileri**
    - Haberler [NewsAPI](https://newsapi.org/)'den Retrofit ile çekilir.

- **Room Veritabanı**
    - Kullanıcı istediği haberi kaydedebilir, bu işlem için Room veritabanı kullanılmaktadır.

- **Haber Arama Sistemi**
    - Kullanıcıların istediği haberi araması için arama bölümü mevcuttur.

- **Kategorize Edilmiş Haberler**
    - Haberler kategorilerine göre tabbar ile görüntülenebilmektedir, bu sayede kullanıcı deneyimi
      ve etkileşimi arttırılmak istenmiştir.

- **Açık & Koyu Mod**
    - Kullanıcı tercihlerine göre tema değiştirme.

## APK Dosyası:

[APK Dosyasını indir](https://drive.google.com/file/d/1GIH8WItwObzWQevIUCfZVN1Rd89JvQsT/view?usp=sharing)

## Uygulamada Kullanılan Teknolojiler ve Kütüphaneler:

 İsim                    | Versiyon 
-------------------------|----------
 Dagger & Hilt           | 2.52     
 Retrofit                | 2.11.0   
 GSON                    | 2.11.0   
 Coroutines              | 1.9.0    
 Room                    | 2.6.1    
 Navigation              | 2.8.4    
 Firebase Authentication | 23.1.0   
 Firebase Firestore      | 25.1.1   
 Glide                   | 4.16.0   
 Lottie                  | 6.0.0    
 Splashscreen            | 1.0.1    
 Swipe Refresh Layout    | 1.1.0    

## Kurulum

1. **Projeyi klonlayın**

 ```bash
 git clone https://github.com/kullaniciadi/news-app.git
 cd news-app
 ```

2. **Gerekli bağımlılıkları yükleyin**

 ```bash
 ./gradlew build
 ```

3. **API anahtarınızı alın ve Constants dosyasına ekleyin**

 ```bash
 const val API_KEY = your_api_key
 ```

4. **Projeyi çalıştırın**

 ```bash
 ./gradlew run
 ```

## Uygulama Tanıtım Videosu (Youtube):

[Uygulama tanıtım videosunu youtube'dan izle.](https://youtu.be/q55WRQbTF9o)

## Ekran Görüntüleri:

<p>Aşağıda uygulamanın açık ve koyu mod olarak ekran görüntülerini bulabilirsiniz</p>

## Açık Mod

### 1. Giriş ve Kayıt Ekranları

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/auth-1.png" alt="Giriş ve Kayıt" style="max-width: 100%;" /></a>
<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/auth-2.png" alt="Giriş ve Kayıt" style="max-width: 100%;" /></a>

### 2. Anasayfa, Keşfet, Kaydedilen Haberler ve Ayarlar Ekranları

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/home.png" alt="Anasayfa, Keşfet, Kaydedilen Haberler ve Ayarlar" style="max-width: 100%;" /></a>

### 3. Haber Detay Ekranı

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/details.png" alt="Haber Detay" style="max-width: 100%;" /></a>

### 4. Ayar Ekranları

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/settings.png" alt="Ayarlar" style="max-width: 100%;" /></a>

## Koyu Mod

### 1. Giriş ve Kayıt Ekranları

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/auth-1.png" alt="Giriş ve Kayıt" style="max-width: 100%;" /></a>
<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/auth-2.png" alt="Giriş ve Kayıt" style="max-width: 100%;" /></a>

### 2. Anasayfa, Keşfet, Kaydedilen Haberler ve Ayarlar Ekranları

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/home.png" alt="Anasayfa, Keşfet, Kaydedilen Haberler ve Ayarlar" style="max-width: 100%;" /></a>

### 3. Haber Detay Ekranı

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/details.png" alt="Haber Detay" style="max-width: 100%;" /></a>

### 4. Ayar Ekranları

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/settings.png" alt="Ayarlar" style="max-width: 100%;" /></a>

<br><br>

# <a name="english"></a>News Application Written in Kotlin

The News Application is a mobile app written with the MVVM architecture, offering a modern interface
where users can discover the latest news, save their favorite articles, and manage personalized
settings. The app includes various features to enhance user experience and engagement. Here are the
key features of the app:

## Features

- **Firebase Authentication**
    - User authentication (sign-up, login, and email verification) is managed using Firebase
      Authentication.

- **Firebase Firestore**
    - User information (first name, last name) is stored and updated using Firebase Firestore.

- **News Data**
    - News articles are fetched from [NewsAPI](https://newsapi.org/) using Retrofit.

- **Room Database**
    - Users can save their favorite articles using Room database.

- **News Search System**
    - A search feature is provided for users to find news articles by query.

- **Categorized News**
    - News articles are organized by categories and can be displayed using a tab bar, enhancing the
      user experience and interaction.

- **Light & Dark Mode**
    - Users can switch between themes based on their preferences.

## APK File:

[Download the APK File](https://drive.google.com/file/d/1GIH8WItwObzWQevIUCfZVN1Rd89JvQsT/view?usp=sharing)

## Technologies and Libraries Used:

 Name                    | Version 
-------------------------|---------
 Dagger & Hilt           | 2.52    
 Retrofit                | 2.11.0  
 GSON                    | 2.11.0  
 Coroutines              | 1.9.0   
 Room                    | 2.6.1   
 Navigation              | 2.8.4   
 Firebase Authentication | 23.1.0  
 Firebase Firestore      | 25.1.1  
 Glide                   | 4.16.0  
 Lottie                  | 6.0.0   
 Splashscreen            | 1.0.1   
 Swipe Refresh Layout    | 1.1.0   

## Setup Instructions

1. **Clone the project**

 ```bash
 git clone https://github.com/kullaniciadi/news-app.git
 cd news-app
 ```

2. **Install the required dependencies**

 ```bash
 ./gradlew build
 ```

3. **Obtain your API key and add it to the Constants file**

 ```bash
 const val API_KEY = your_api_key
 ```

4. **Run the project**

 ```bash
 ./gradlew run
 ```

## Application Demo Video (Youtube)::

[Watch the application demo video on YouTube.](https://youtu.be/q55WRQbTF9o)

## Screenshots:

<p>Below are screenshots of the application in both light and dark modes.</p>

## Light Mode

### 1. Login and Sign-up Screens

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/auth-1.png" alt="Login and Sign-up" style="max-width: 100%;" /></a>
<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/auth-2.png" alt="Login and Sign-up" style="max-width: 100%;" /></a>

### 2. Home, Discover, Saved News, and Settings Screens

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/home.png" alt="Home, Discover, Saved News, and Settings" style="max-width: 100%;" /></a>

### 3. News Detail Screen

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/details.png" alt="News Detail" style="max-width: 100%;" /></a>

### 4. Settings Screens

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/light/settings.png" alt="Settings" style="max-width: 100%;" /></a>

## Dark Mode

### 1. Login and Sign-up Screens

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/auth-1.png" alt="Login and Sign-up" style="max-width: 100%;" /></a>
<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/auth-2.png" alt="Login and Sign-up" style="max-width: 100%;" /></a>

### 2. Home, Discover, Saved News, and Settings Screens

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/home.png" alt="Home, Discover, Saved News, and Settings" style="max-width: 100%;" /></a>

### 3. News Detail Screen

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/details.png" alt="News Detail" style="max-width: 100%;" /></a>

### 4. Settings Screens

<a><img src="https://github.com/Cengizhanerturan/kotlin_news_app_mvvm/blob/main/app/src/main/github-images/dark/settings.png" alt="Settings" style="max-width: 100%;" /></a>

