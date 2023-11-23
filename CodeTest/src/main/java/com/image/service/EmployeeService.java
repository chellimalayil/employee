package com.image.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.image.dao.EmployeeRepository;
import com.image.model.Employee;
import com.image.model.TaxDeductionResponse;

import ch.qos.logback.classic.Logger;

@Service
public class EmployeeService {

	private static final Logger lOG = (Logger) LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepository empRepo;

	public void addEmployee(Employee employee) {
		empRepo.save(employee);
	}

	public List<TaxDeductionResponse> calculateTaxDeduction() {
		List<TaxDeductionResponse> taxDeduceList = new ArrayList<>();

		try {

			Iterable<Employee> employees = empRepo.findAll();

			employees.forEach((emp) -> {
				// Parse dateOfJoining from String to LocalDate
				LocalDate doj = LocalDate.parse(emp.getDoj().toString());

				// Calculate total months from DOJ to the end of the financial year (March)
				LocalDate currentDate = LocalDate.now();
				LocalDate financialYearEnd = LocalDate.of(currentDate.getYear(), Month.MARCH, 31);
				int monthsWorked = (int) Period.between(doj, financialYearEnd.plusDays(1)).toTotalMonths();

				// Calculate total salary 
				double totalSalary = emp.getSalary().doubleValue()*monthsWorked;

				// Calculate tax based on tax slabs
				double tax = calculateTax(totalSalary);

				// Calculate cess if salary is more than 2500000
				double cess = (totalSalary > 2500000) ? 0.02 * (totalSalary - 2500000) : 0;
				TaxDeductionResponse response = new TaxDeductionResponse();
				response.setEmployeeCode(emp.getEmployeeId());
				response.setFirstName(emp.getFirstName());
				response.setLastName(emp.getLastName());
				response.setYearlySalary(totalSalary);
				response.setTaxAmount(tax);
				response.setCessAmount(cess);
				taxDeduceList.add(response);
			});
		} catch (Exception ex) {
			lOG.error("exception in employee service : calculateTaxDeduction:{}", ex);
		}

		return taxDeduceList;

	}

	private double calculateTax(double yearlySalary) {
		if (yearlySalary <= 250000) {
			return 0;
		} else if (yearlySalary <= 500000) {
			return 0.05 * (yearlySalary - 250000);
		} else if (yearlySalary <= 1000000) {
			return 0.1 * (yearlySalary - 500000) + 12500;
		} else {
			return 0.2 * (yearlySalary - 1000000) + 12500 + 50000;
		}
	}

}
