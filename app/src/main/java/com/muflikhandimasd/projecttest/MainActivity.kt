package com.muflikhandimasd.projecttest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.muflikhandimasd.projecttest.entities.Employee
import com.muflikhandimasd.projecttest.ui.theme.ProjectTestTheme
import com.muflikhandimasd.projecttest.viewmodels.EmployeeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),) { innerPading ->

                    EmployeeScreen(modifier = Modifier.padding(innerPading))
                }
            }
        }
    }
}

@Composable
fun DropDownApp(viewModel: EmployeeViewModel) {

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    val filters = listOf("By Name", "By Email", "By Team", "By Employee Type",)
Box(
    Modifier

        .background(Color.Gray) // Warna putih untuk container
        .clip(RoundedCornerShape(16.dp))
        .padding(16.dp)

){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                Text(text = filters[itemPosition.value])
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "DropDown Icon"
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                filters.forEachIndexed { index, value ->
                    DropdownMenuItem(text = {
                        Text(text = value)
                    },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                            viewModel.sortByParam(filters[index])

                        })
                }
            }
        }

    }}
}
@Composable
fun EmployeeScreen(viewModel: EmployeeViewModel= viewModel(), modifier: Modifier){
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val employees by viewModel.employees.collectAsState()
    Column (modifier = modifier){
        Button(onClick = { viewModel.getEmployees() }, modifier = Modifier.fillMaxWidth()) {
            Text("Reload")
        }
        Spacer(modifier = Modifier.height(16.dp))
        DropDownApp(viewModel)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading){
            CircularProgressIndicator(
              modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }else if(!errorMessage.isNullOrEmpty()){
            Text(
                text = errorMessage.toString(),
                color = Color.Red,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            )
        }else if (employees.isEmpty()){
            Text(
                text = "No employees found",
                modifier = Modifier.fillMaxSize().padding(16.dp)
            )
        }else{
            EmployeeList(employees)
        }

    }

}


@Composable
fun EmployeeList(employees: List<Employee>, ) {
    LazyColumn {
        items(employees.size) { index ->
            EmployeeItem(employee = employees[index])
        }
    }
}

@Composable
fun EmployeeItem(employee: Employee) {
    Row(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
           model =employee.photoUrlSmall,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "Full Name : "+employee.fullName)
            Text(text = "Email Address : "+employee.emailAddress)
            Text(text = "Team : "+employee.team)
            Text(text = "Employee Type : "+employee.employeeType)

        }
    }
}
