package com.demoapis.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.demoapis.models.EmployeeData;

@EnableMongoRepositories
public interface EmployeeDataRepository extends MongoRepository<EmployeeData, String>{
 
	 public EmployeeData findByemployeeID(String employeeId);
}
