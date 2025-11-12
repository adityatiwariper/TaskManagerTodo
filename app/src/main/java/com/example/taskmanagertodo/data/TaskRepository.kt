package com.example.taskmanagertodo.data

import androidx.lifecycle.LiveData

// Repository provides clean API to ViewModel
class TaskRepository(private val dao: TaskDao) {
    fun getAllTasks(): LiveData<List<Task>> = dao.getAll()
    fun searchTasks(query: String): LiveData<List<Task>> = dao.search("%$query%")

    suspend fun insert(task: Task) = dao.insert(task)
    suspend fun update(task: Task) = dao.update(task)
    suspend fun delete(task: Task) = dao.delete(task)
}