package com.sector.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sector.todo.data.ToDoDatabase
import com.sector.todo.data.models.ToDoData
import com.sector.todo.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application): AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository

    private val getAllData: LiveData<List<ToDoData>>

    init {
        repository = ToDoRepository(toDoDao)
        getAllData = repository.getAllData
    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }
}