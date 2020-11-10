package com.capg.restapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRestAPIServiceTest {
	EmployeePayrollRestAPIService employeePayrollRestAPIService ;

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
		employeePayrollRestAPIService = new EmployeePayrollRestAPIService(getEmployeeList());
		
	}
	
	private List<Employee> getEmployeeList() {
		Response response = RestAssured.get("/employees");
		Employee[] employees = new Gson().fromJson(response.asString(), Employee[].class);
		return Arrays.asList(employees);
	}

	private Response addNewEmployeeToJsonServer(Employee employee) {
		String employeeJson = new Gson().toJson(employee);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(employeeJson);
		return requestSpecification.post("/employees");
	}

	private void addMultipleEmployeeUsingThreads(List<Employee> employees) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<>();
		for (Employee employee : employees) {
			employeeAdditionStatus.put(employee.getId(), false);
			Runnable task = () -> {
				System.out.println("Employee Being Added : " + Thread.currentThread().getName());
				Response response = addNewEmployeeToJsonServer(employee);
				if (response.getStatusCode() == 201)
					System.out.println("Employee successfully Added : " + Thread.currentThread().getName());
				employeeAdditionStatus.put(employee.getId(), true);
			};
			Thread thread = new Thread(task, employee.getName());
			thread.start();
		}
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private List<Employee> getEmployeesFromJsonServer() {
		Response response = RestAssured.get("/employees");
		Employee[] employees = new Gson().fromJson(response.asString(), Employee[].class);
		return Arrays.asList(employees);
	}

	@Ignore
	@Test
	public void givenNewEmployeeDetails_WhenAdded_ShouldBeAddedToTheJsonServer() {
		Employee newEmployee = new Employee(4, "Mukesh Ambani", "M", 400000.0);
		employeePayrollRestAPIService.addNewEmployee(newEmployee);
		Response response = addNewEmployeeToJsonServer(newEmployee);
		System.out.println(response.getStatusCode());
		Assert.assertEquals(201, response.getStatusCode());
	}

	@Ignore
	@Test
	public void givenMultipleEmployees_WhenAdded_ShouldBeAddedToTheJsonServer() {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee(5, "Harshad Mehta", "M", 500000.0));
		employees.add(new Employee(6, "Modi Jee", "M", 600000.0));
		employees.add(new Employee(7, "Smriti Mandhana", "F", 700000));
		employeePayrollRestAPIService.addEmployeeToList(employees);
		addMultipleEmployeeUsingThreads(employees);
		Assert.assertEquals(7, getEmployeesFromJsonServer().size());
	}

	@Ignore
	@Test
	public void giveNewSalary_whenUpdated_shouldBeAddedToJsonServer() {
		employeePayrollRestAPIService.updateSalary("Harshad Mehta", 1000000.0);
		Employee employee = employeePayrollRestAPIService.getEmployeeDetailsByName("Harshad Mehta");
		String employeeJson = new Gson().toJson(employee);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(employeeJson);
		Response response = request.put("/employees/" + employee.getId());
		System.out.println(response.getStatusCode());
		Assert.assertEquals(200, response.getStatusCode());
	}
	
	
	@Ignore
	@Test
	public void givenAJsonServer_WhenRetrievedEmployees_ShouldMatchEmployeesCount() {
		List<Employee> employeesList = this.getEmployeesFromJsonServer();
		Assert.assertEquals(7, employeesList.size());
	}

	@Test
	public void givenAnEmployee_WhenDeleted_ShouldBeRemovedFromJsonServer() {
		Employee employee = employeePayrollRestAPIService.getEmployeeDetailsByName("Bill Gates");
		System.out.println(employee);
		employeePayrollRestAPIService.removeEmployeeFromTheList(employee);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/employees/" + employee.getId());
		Assert.assertEquals(200, response.getStatusCode());
	}

}
