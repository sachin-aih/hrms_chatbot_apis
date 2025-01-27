package com.demoapis.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name="employee_tbl")
public class Employee{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String uid;
	private String email;
	private String password;
//	@OneToMany(mappedBy = "employee")
//	private List<ChatHistory> chatHistory;
	
	
	

}
