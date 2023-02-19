package com.project.taxCalc.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.taxCalc.entity.Employee;
import com.project.taxCalc.entity.EmployeeWithTaxDeduction;

@RestController
@RequestMapping("/empC")
public class Controller {

	public ArrayList<Employee> empList = new ArrayList<Employee>();
	
	@GetMapping("ping")
	public ResponseEntity<?> ping() {
		return ResponseEntity.ok().body("working");
	}

	// @PostMapping(path = "/employee", consumes = MediaType.ALL_VALUE, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/employee")
	public ResponseEntity<?> addEmployee(@Valid @RequestBody Employee emp, BindingResult bindingResult) {
		System.out.println("emp - - - - " + emp);
		if (bindingResult.hasErrors()) {
			// List<FieldError> errors = bindingResult.getFieldErrors();
			List<String> message = bindingResult.getFieldErrors().stream()
					.map(e -> e.getField().toUpperCase() + ":" + e.getDefaultMessage()).collect(Collectors.toList());
			System.out.println("message - - - - " + message);
			return ResponseEntity.ok().body(message);
		}
		empList.add(emp);
		return ResponseEntity.ok().body("employee created successfully");
	}

	@GetMapping("/employeesWithTaxCalc")
	public ResponseEntity<?> employeesWithTaxCalc() {
		List<EmployeeWithTaxDeduction> empTaxList = new ArrayList<EmployeeWithTaxDeduction>();
		empList.forEach(e -> {
			EmployeeWithTaxDeduction emptax = new EmployeeWithTaxDeduction();
			emptax.setEmployeeCode(e.getEmployeeID());
			emptax.setFirstName(e.getFirstName());
			emptax.setLastName(e.getLastName());
			System.out.println(e.getDoj().getDayOfMonth() + " " + e.getDoj().getMonthValue());
			double firstMonthSal = (((30 - e.getDoj().getDayOfMonth()) + 1) * (e.getSalary() / 30));
			System.out.println("firstMonthSal " + firstMonthSal);
			double yearlySalary = 0;
			if (e.getDoj().getMonthValue() < 4) {
				yearlySalary = firstMonthSal + ((3 - e.getDoj().getMonthValue()) * e.getSalary());
			} else {
				yearlySalary = firstMonthSal + ((15 - e.getDoj().getMonthValue()) * e.getSalary());
			}
			emptax.setYearlySalary(yearlySalary);
			double tax = calculateTax(yearlySalary);
			emptax.setTaxAmount(tax);
			emptax.setCessAmount(calculateCess(tax));
			empTaxList.add(emptax);
		});
		return ResponseEntity.ok().body(empTaxList);
	}

	public double calculateTax(double income) { // ti -> total income
		double tax = 0;
		if (income <= 250000) // No Tax
			tax = 0;
		else if (income > 250000 && income <= 500000) // 5%
			tax = (income - 250000) * 0.05; // slab 1
		else if (income > 500000 && income <= 1000000) // 12500 (5% of 250000) + 10% of (income - 500000)
			tax = 12500 + (income - 500000) * 0.10; // slab 1 + slab 2
		else if (income > 1000000) // 12500 (5% of 250000) + 50000 (10% of 500000) + 20% of (income - 1000000)
			tax = 12500 + 50000 + (income - 1000000) * 0.20; // slab 1 + slab 2 + slab 3
		System.out.println("Total tax: " + tax + " for income " + income);
		return tax;
	}

	public double calculateCess(double tax) {
		double cess = 0;
		if (tax > 300000) {
			return tax * 0.03;
		}
		System.out.println("Total cess: " + cess + " for tax " + tax);
		return cess;
	}
}
