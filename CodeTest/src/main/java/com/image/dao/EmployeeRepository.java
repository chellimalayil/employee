package com.image.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.image.model.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer>{

}
