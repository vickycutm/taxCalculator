package com.project.taxCalc.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Employee {

	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(name = "EmpID")

	@NotNull(message = "Employee id is mandatory")
	private int employeeID;

	// @Column(name = "FirstName", length = 50, nullable = false)
	@NotNull(message = "FirstName is mandatory")
	private String firstName;

	// key LastName starts with capital letter, thats a invalid json request.
	// If we need the capital letter start as a key then we need to @jsonproperty
	@JsonProperty("LastName")
	@NotNull(message = "LastName is mandatory")
	private String LastName;

	@NotEmpty(message = "Email cant be null")
	@Email(message = "Email is not valid")
	private String email;

	// (May have multiple phone numbers)
	@NotNull(message = "Phone Number cant be null")
	private String phoneNumber;

	// yyyy-MM-dd HH:mm:ss
	@NotNull(message = "Date of joining(doj) Mandatory")
	@Past(message = "Future is not allowed")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate doj;

	// (per month)
	@NotNull(message = "Salary cant be null")
	private double salary;
}

// {
//     "employeeID": "",
//     "firstName": "Susant",
//     "LastName": "Kumar",
//     "email": "VickyGupta@gmail.com",
//     "phoneNumber": "7853931756",
//     "doj": "2022-06-10",
//     "salary": 400000
// }
