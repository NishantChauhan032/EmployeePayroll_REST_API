package com.capg.restapi;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollRestAPIService {

	private List<Employee> employeesList = new ArrayList<>();;

	public void addNewEmployee(Employee employee) {
		employeesList.add(employee);
	}

	public int getCount() {
		return employeesList.size();
	}

	public List<Employee> getEmployeeList() {
		return employeesList;
	}
	
	public void addEmployeeToList(List<Employee> employees) {
		employees.forEach(employee -> this.employeesList.add(employee));
	}
}
