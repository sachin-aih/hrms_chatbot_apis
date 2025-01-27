package com.demoapis.models;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(collection="employee_data")
public class EmployeeData{
	    @Id
	    private String id; // Maps to the _id field in MongoDB
	    @Field("EmployeeID")
	    private String employeeID;
	    @Field("FirstName")
	    private String firstName;
	    @Field("LastName")
	    private String lastName;
	    @Field("Gender")
	    private String gender;
	    @Field("Age")
	    private int age;
	    @Field("BusinessTravel")
	    private String businessTravel;
	    @Field("Department")
	    private String department;
	    @Field("DistanceFromHome")
	    private int distanceFromHome;
	    @Field("State")
	    private String state;
	    @Field("Ethnicity")
	    private String ethnicity;
	    @Field("Education")
	    private int education;
	    @Field("EducationField")
	    private String educationField;
	    @Field("JobRole")
	    private String jobRole;
	    @Field("MaritalStatus")
	    private String maritalStatus;
	    @Field("Salary")
	    private int salary;
	    @Field("StockOptionalLevel")
	    private int stockOptionLevel;
	    @Field("OverTime")
	    private String overTime;
	    @Field("HireDate")
	    private String hireDate;
	    @Field("Attrition")
	    private String attrition;
	    @Field("YearsAtCompany")
	    private int yearsAtCompany;
	    @Field("YearsInMostRecentRole")
	    private int yearsInMostRecentRole;
	    @Field("YearsSinceLastPromotion")
	    private int yearsSinceLastPromotion;
	    @Field("YearsWithCurrManager")
	    private int yearsWithCurrManager;
	    @Field("dummy_url")
	    private String salaryUrl;
	    
	    
	
	
	
	

}
