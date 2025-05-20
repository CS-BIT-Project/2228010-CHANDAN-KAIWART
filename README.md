# 🍽️ Recipe Trove- Your Personal Cooking Companion

An Android application that helps users discover, save, and manage delicious recipes with smart search features and video support. Built with Firebase, Spoonacular API, and YouTube Data API.

---

## ✨ Features

- 🔐 **Authentication**
  - Firebase Email/Password Login
  - Google Sign-In
  - Forgot Password Recovery

- 🔍 **Smart Recipe Search**
  - Search by recipe title or ingredients
  - Random recipe discovery
  - Spoonacular API integration

- 🎥 **Video Support**
  - Recipe video tutorials via YouTube Data API

- 👤 **Profile Management**
  - Basic profile view with name, email, and profile picture
  - Tabs for My Recipes, Saved Recipes, History, and Collections(integration in progress)

- 💬 **Chatbot Assistant** *(Coming Soon)*
  - Dialogflow-based recipe assistant (integration in progress)

---

## 🔧 Tech Stack

- **Kotlin** - Android development
- **Firebase Auth & Firestore** - User authentication & data storage
- **Spoonacular API** - Recipe data
- **YouTube Data API** - Recipe videos
- **MVVM Architecture** - Clean and maintainable code
- **Retrofit** - API requests
- **Glide / Coil** - Image loading

---

## 🛠️ Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/recipe-app.git
2. Open in Android Studio

3. Add your API keys:

      google-services.json for Firebase
    
      Spoonacular API key in RecipeRepository.kt
    
      YouTube API key in YoutubeHelper.kt
    
      Dialogflow credentials (optional)

4. Build and run the app on a device or emulator
