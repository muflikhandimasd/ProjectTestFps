package com.muflikhandimasd.projecttest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muflikhandimasd.projecttest.entities.Employee
import com.muflikhandimasd.projecttest.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel : ViewModel() {
    private val _employees = MutableStateFlow<List<Employee>>(listOf())
    val employees: StateFlow<List<Employee>>  get() = _employees

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error




init {
    getEmployees()
}


    fun getEmployees() {

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitInstance.api.getEmployees()
                if (response.isSuccessful) {
                    _employees.value = (response.body()?.employees?: listOf())
                    _employees.value= _employees.value.sortedBy { it.fullName }
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }finally {
                _loading.value = false
            }
        }
    }

    fun sortByParam(value : String){
        when(value){
            "By Name" ->_employees.value=employees.value.sortedBy { it.fullName }
            "By Email"->_employees.value=_employees.value.sortedBy { it.emailAddress }
            "By Employee Type"->_employees.value=_employees.value.sortedBy { it.employeeType }
            "By Team"->_employees.value=_employees.value.sortedBy { it.team }
            else -> _employees.value=employees.value
        }
    }


}

