package com.project.taxCalc.entity;

import lombok.Data;

@Data
public class EmployeeWithTaxDeduction {
	public int employeeCode;
	public String firstName;
	public String lastName;
	public double yearlySalary;
	public double taxAmount;
	public double cessAmount;
}
