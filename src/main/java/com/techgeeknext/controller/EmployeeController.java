package com.techgeeknext.controller;

import com.techgeeknext.model.Employee;
import com.techgeeknext.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;


    /**
     * Get the employee by name
     *
     * @param name
     * @return ResponseEntity
     */
    @GetMapping("/employee/name/{name}")
    public ResponseEntity<Employee> getEmployeeByName(@PathVariable("name") String name) {
        try {
            // retrieve the record from database
            Optional<Employee> empObj = Optional.ofNullable(employeeRepository.findByName(name));

            //check if employee exist in database
            if (empObj.isPresent()) {
                return new ResponseEntity<>(empObj.get(), HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    /**
     * Create new employee
     *
     * @param employee
     * @return ResponseEntity
     */
    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeRepository
                .save(Employee.builder()
                        .empName(employee.getEmpName())
                        .role(employee.getRole())
                        .build());
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }

    /**
     * Get all the employees
     *
     * @return ResponseEntity
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        try {
            return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update Employee record by it's id
     *
     * @param id
     * @return
     */
    @PutMapping("/employee/{id}/name/{empName}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable("id") long id, @PathVariable("empName") String empName) {

        //check if employee exist in database
        Employee empObj = getEmpRec(id);

        if (empObj != null) {
            employeeRepository.updateEmployeeById(empName, id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Delete Employee by Id
     *
     * @param id
     * @return ResponseEntity
     */
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<HttpStatus> deleteEmployeeById(@PathVariable("id") long id) {
        try {
            //check if employee exist in database
            Employee emp = getEmpRec(id);

            if (emp != null) {
                employeeRepository.deleteEmployeeById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method to get the employee record by id
     *
     * @param id
     * @return Employee
     */
    private Employee getEmpRec(long id) {
        Optional<Employee> empObj = employeeRepository.findById(id);

        if (empObj.isPresent()) {
            return empObj.get();
        }
        return null;
    }

    @GetMapping("/employee/pagination/sortby/{columnName}")
    public ResponseEntity<List<Employee>> getEmpPaginationAsc(@PathVariable("columnName") String columnName) {
        try {

            Pageable pageRequest = PageRequest.of(0, 5, Sort.by(columnName).ascending());

            // retrieve the record from database
            Optional<List<Employee>> empObj = Optional.ofNullable(
                    employeeRepository.getEmployeePageAcsByCol(pageRequest)
                            .getContent());

            if (empObj.isPresent()) {
                return new ResponseEntity<>(empObj.get(), HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
