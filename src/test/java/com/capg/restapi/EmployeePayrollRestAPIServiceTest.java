package com.capg.restapi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRestAPIServiceTest {
	EmployeePayrollRestAPIService employeePayrollRestAPIService = new EmployeePayrollRestAPIService() ;

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;		
	}
	
	private Response addNewEmployeeToJsonServer(Employee employee) {
		String employeeJson = new Gson().toJson(employee);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(employeeJson);
		return requestSpecification.post("/employees");
	}

	@Test
	public void givenNewEmployeeDetails_WhenAdded_ShouldBeAddedToTheJsonServer() {
		Employee newEmployee = new Employee(4, "Mukesh Ambani", "M", 400000.0);
		employeePayrollRestAPIService.addNewEmployee(newEmployee);
		Response response = addNewEmployeeToJsonServer(newEmployee);
		System.out.println(response.getStatusCode());
		Assert.assertEquals(201,response.getStatusCode());
	}

	
}
