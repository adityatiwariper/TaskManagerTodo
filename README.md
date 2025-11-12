📝 Task Manager App

A simple yet powerful Android Task Management Application built using MVVM architecture, Room Database, and LiveData for reactive UI updates.

🚀 Features

# Add, edit, and delete tasks
# Display tasks in a RecyclerView
# Search and filter tasks in real time
# Persist task data locally using Room
# Built with Kotlin and Android Jetpack components

🧩 Tech Stack

Language: Kotlin

Architecture: MVVM (Model–View–ViewModel)

Database: Room (Local Persistence)

UI Components: RecyclerView, Material Design

Reactive Programming: LiveData, ViewModel

Coroutines: For background operations


🏗️ Project Structure
com.example.taskmanagertodo/
│
├── data/
│   ├── Task.kt
│   ├── TaskDao.kt
│   ├── TaskDatabase.kt
│   └── TaskRepository.kt
│
├── ui/
│   ├── MainActivity.kt
│   ├── TaskAdapter.kt
│   └── AddEditTaskActivity.kt
│
└── viewmodel/
    └── TaskViewModel.kt

⚙️ Installation

Clone the repository

git clone https://github.com/adityatiwariper/taskmanagertodo.git


Open the project in Android Studio

Let Gradle sync automatically

Run the app on an emulator or device

🧠 Concepts Implemented

MVVM architecture with clean separation of concerns

LiveData for observing and updating UI reactively

Room Database for offline task persistence

Kotlin Coroutines for smooth background operations

💡 Future Enhancements

Task categories or priorities

Notifications & reminders

Cloud sync support

👨‍💻 Author

Aditya Kumar Tiwari
📧 adityatiwarit97@gmail.com

📱 Android Developer
