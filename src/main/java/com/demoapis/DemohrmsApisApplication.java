package com.demoapis;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demoapis.models.EmployeeData;
import com.demoapis.repositories.EmployeeDataRepository;

@SpringBootApplication
public class DemohrmsApisApplication implements CommandLineRunner{

	@Autowired
	private EmployeeDataRepository repo; 
	
	public static void main(String[] args) {
		SpringApplication.run(DemohrmsApisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
	 EmployeeData employees = repo.findByemployeeID("3012-1A41");
	 System.out.println(employees);
	 String pythonScriptPath = "D://demo_apis/demohrms_apis/src/main/resources/main.py";
	 File file = new File(pythonScriptPath);

     if (file.exists()) {
         System.out.println("Path is correct and exists.");
     } else {
         System.out.println("Path is incorrect or does not exist.");
     }
     
     ProcessBuilder processBuilder = new ProcessBuilder(
    		    "C://Users/sachi/hrms/myenv/Scripts/python.exe", 
    		    "D://demo_apis/demohrms_apis/src/main/resources/demo.py"
    		);
    Process process = processBuilder.start();
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    StringBuilder output = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
    }

    // Read any errors (if the output is in the error stream, as some versions do this)
    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    StringBuilder errorOutput = new StringBuilder();
    while ((line = errorReader.readLine()) != null) {
        errorOutput.append(line).append("\n");
    }

    // Wait for the process to complete
    int exitCode = process.waitFor();
    if (exitCode == 0) {
        // Print Python version
        System.out.println("Python Version Output: " + output.toString().trim());
    } else {
        System.err.println("Error Output: " + errorOutput.toString().trim());

	}
	
	
}
}
