package com.example.taskmanagertodo.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.taskmanagertodo.data.Task
import com.example.taskmanagertodo.data.TaskDatabase
import com.example.taskmanagertodo.data.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    // Mark repository as lateinit so it can be set in init{}
    private lateinit var repository: TaskRepository

    // Declare allTasks as MutableLiveData
    private lateinit var allTasks: LiveData<List<Task>>

    private val _searchQuery = MutableLiveData<String?>()
    val searchQuery: LiveData<String?> = _searchQuery

    // filteredTasks computed based on searchQuery
    val filteredTasks: LiveData<List<Task>> = _searchQuery.switchMap { query ->
        if (query.isNullOrBlank()) {
            allTasks
        } else {
            repository.searchTasks(query)
        }
    }

    init {
        val dao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)
        allTasks = repository.getAllTasks()
        _searchQuery.value = null
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun insert(task: Task, onComplete: (Long) -> Unit = {}) = viewModelScope.launch {
        val id = repository.insert(task)
        onComplete(id)
    }

    fun update(task: Task, onComplete: (() -> Unit)? = null) = viewModelScope.launch {
        repository.update(task)
        onComplete?.invoke()
    }

    fun delete(task: Task, onComplete: (() -> Unit)? = null) = viewModelScope.launch {
        repository.delete(task)
        onComplete?.invoke()
    }
}

// Factory for creating ViewModel with Application
class TaskViewModelFactory(private val app: Application) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(app) as T
        }
        return super.create(modelClass)
    }
}