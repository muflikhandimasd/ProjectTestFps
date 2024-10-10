package com.muflikhandimasd.projecttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflikhandimasd.projecttest.entities.Employee
import kotlinx.coroutines.launch

class EmployeeViewModel : ViewModel() {
    private val _employees = MutableLiveData<List<Employee>>()
    val employees: LiveData<List<Employee>> = _employees

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getEmployees() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitInstance.api.getEmployees()
                if (response.isSuccessful) {
                    _employees.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
            _loading.value = false
        }
    }

    fun sortEmployeesByName() {
        _employees.value = _employees.value?.sortedBy { it.name }
    }

    fun sortEmployeesByEmail() {
        _employees.value = _employees.value?.sortedBy { it.email }
    }
}
