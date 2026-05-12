# QuickDo ✅
> A simple and fast Task Manager Android app built with Java.
 
## 📱 About the App
 
**QuickDo** is a lightweight task manager app for Android that helps you manage your daily tasks quickly and efficiently. Supports **Google Sign-In** and **Email/Password authentication** powered by Firebase.
 
---
 
## ✨ Features
 
- ✅ Add, edit, and delete tasks
- 🔐 Sign in with Google
- 📧 Email & Password authentication
- ☁️ Cloud sync with Firebase Firestore
- 🎨 Clean and simple Material UI
---
 
## 🛠️ Tech Stack
 
| **Language** | Java |
| **IDE** | Android Studio |
| **Min SDK** | 24 (Android 7.0 Nougat) |
| **Target SDK** | 36 |
| **Authentication** | Firebase Auth 22.3.0 |
| **Database** | Firebase Firestore 24.10.0 |
| **Google Sign-In** | Play Services Auth 21.0.0 |
 
---
 
## 🚀 Getting Started
 
### Prerequisites
 
- Android Studio (latest version recommended)
- Java 11 or higher
- A Firebase account
### Setup Instructions
 
1. **Clone the repository**
   ```bash
   https://github.com/lakkanadulshan/QuickDo-Task-Manager-Mobile-App.git
   cd QuickDo
   ```
 
2. **Set up Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project
   - Add an Android app with package name: `com.example.quickdo`
   - Download the `google-services.json` file
   - Place it inside the `app/` folder:
     ```
     QuickDo/
     └── app/
         └── google-services.json  ← place here
     ```
 
3. **Enable Firebase Services**
   In your Firebase Console:
   - **Authentication** → Sign-in Methods → Enable **Email/Password** and **Google**
   - **Firestore Database** → Create database (Start in test mode for development)
4. **Open in Android Studio**
   - Open the project folder in Android Studio
   - Wait for Gradle sync to complete
5. **Build and Run**
   - Connect an Android device (Android 7.0+) or start an emulator
   - Click **Run ▶** or press `Shift + F10`
---
 
## 🔒 Security Note
 
> ⚠️ **`google-services.json` is NOT included in this repository.**
>
> This file contains sensitive Firebase credentials. Generate your own by following the Firebase setup steps above. It is excluded via `.gitignore`.
 
Add this to your `.gitignore`:
```
# Firebase
app/google-services.json
```
 
---
 
## 📁 Project Structure
 
```
QuickDo/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/quickdo/
│   │       │   ├── MainActivity.java
│   │       │   └── ...
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   └── values/
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   └── google-services.json  ← (not in repo, add manually)
├── .gitignore
└── build.gradle
```
 
---
 
## 📦 Dependencies
 
```gradle
// Firebase Authentication
implementation("com.google.firebase:firebase-auth:22.3.0")
 
// Firebase Firestore
implementation("com.google.firebase:firebase-firestore:24.10.0")
 
// Google Sign-In
implementation("com.google.android.gms:play-services-auth:21.0.0")
```
 
---
 
## 📄 License
 
This project is open source and available under the [MIT License](LICENSE).
 
---
 
## 👤 Author
 
**Your Name**
- GitHub: https://github.com/lakkanadulshan/
---
