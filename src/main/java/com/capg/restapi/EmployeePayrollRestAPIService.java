package com.capg.restapi;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollRestAPIService {

	private List<Employee> employeesList = new ArrayList<>();
	
	public EmployeePayrollRestAPIService(List<Employee> employeesList) {
		super();
		this.employeesList = employeesList;
	}

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
	
	public void updateSalary(String name, double salary) {
	    System.out.println(employeesList);
		Employee employee = this.employeesList.stream()
				                              .filter(e -> e.getName().equals(name))
										      .findFirst()
										      .orElse(null);
		employee.setSalary(salary);
	}
	
	public Employee getEmployeeDetailsByName(String name) {
		return this.employeesList.stream()
				                 .filter(e -> e.getName().equals(name))
								 .findFirst()
								 .orElse(null);
	}
}
