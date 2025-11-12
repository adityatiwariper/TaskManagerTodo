package com.example.taskmanagertodo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE title LIKE :query OR description LIKE :query ORDER BY id DESC")
    fun search(query: String): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}
