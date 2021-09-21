package com.sector.todo.data.repository

import androidx.lifecycle.LiveData
import com.sector.todo.data.ToDoDao
import com.sector.todo.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }
}