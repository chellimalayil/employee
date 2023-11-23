package com.image.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.image.model.Employee;
import com.image.model.TaxDeductionResponse;
import com.image.service.EmployeeService;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private static final Logger lOG = (Logger) LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService empService;

	@PostMapping
	public ResponseEntity<String> addEmployee(@Valid @RequestBody Employee employee) {
		try {
			empService.addEmployee(employee);

			return new ResponseEntity<>("Employee added successfully", HttpStatus.OK);
		} catch (Exception ex) {
			lOG.error("exception in addEmploye:{}", ex);
			return new ResponseEntity<>("Employee not added", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tax-deduction")
	public ResponseEntity<List<TaxDeductionResponse>> calculateTaxDeduction() {
		try {

			List<TaxDeductionResponse> list = empService.calculateTaxDeduction();

			return ResponseEntity.ok(list);
		} catch (Exception ex) {
			lOG.error("exception in calculateTaxDeduction:{}", ex);
			return new ResponseEntity<List<TaxDeductionResponse>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
