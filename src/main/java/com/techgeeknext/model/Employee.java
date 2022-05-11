package com.techgeeknext.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
@NamedNativeQuery (name = "Employee.findByName",
		query = "SELECT * FROM employees WHERE emp_name = ?1",
		resultClass = Employee.class)
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "emp_name")
	private String empName;

	@Column(name = "role")
	private String role;
}
