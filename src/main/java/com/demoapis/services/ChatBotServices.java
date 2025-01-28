package com.demoapis.services;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.demoapis.models.ChatHistory;
import com.demoapis.models.EmployeeData;
import com.demoapis.repositories.ChatHistoryRepository;
import com.demoapis.repositories.EmployeeDataRepository;
import com.demoapis.requestDtos.ChatBotRequestDto;
import com.demoapis.responseDtos.ChatHistoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Service
public class ChatBotServices{
	
	@Autowired
	private EmployeeDataRepository employeeRepo;
	
	@Autowired
	private ChatHistoryRepository chatRepo;
	
	
	public List<EmployeeData> getAllEmployees(){
		
		List<EmployeeData> allEmployees = employeeRepo.findAll();
		System.out.println("employees" + allEmployees);
		return allEmployees;
		
	}
	
 // hr-policies 
 public Object chatBotResponse(ChatBotRequestDto chatRequest){
		try{
			
			String employeeId = chatRequest.getUserIdOrEmployeeId();
		    String prompt = chatRequest.getPrompt();
			String pythonExecutablePath = "C://Users/sachi/rhms/myenv/Scripts/python.exe";
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 EmployeeData employee = employeeRepo.findByemployeeID(chatRequest.getUserIdOrEmployeeId());
		 if(employee==null || !chatRequest.getUserIdOrEmployeeId().equals(employee.getEmployeeID())) {
			 
			 ObjectNode responseNode = objectMapper.createObjectNode();
			 responseNode.put("response", "employee or user not found of this user id");
			 String jsonResponse = objectMapper.writeValueAsString(responseNode);
			
			 return jsonResponse;
			 
		 }
	ProcessBuilder processBuilder = new ProcessBuilder(
		    "C://Users/sachi/hrms/myenv/Scripts/python.exe",
		    "D:\\demo_apis\\hrms_chatbot_apis\\src\\main\\resources\\main.py",
		    "--employee_id", employeeId,
		    "--query", prompt
		);
         System.out.println("command "+processBuilder.command().get(0));
         // Start the process
         Process process = processBuilder.start();
         int exitCode = process.waitFor();
      // Capture output and errors
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

         StringBuilder output = new StringBuilder();
         String line;

         // Read standard output
         while ((line = reader.readLine()) != null) {
             output.append(line).append("\n");
         }

         // Read error output
         StringBuilder errorOutput = new StringBuilder();
         while ((line = errorReader.readLine()) != null) {
             errorOutput.append(line).append("\n");
         }
         // Check for errors during script execution
         if (exitCode != 0){
        	 System.out.println("Python script failed with exit code: " + exitCode);
        	 System.out.println("Error Output: " + errorOutput);
        	 throw new RuntimeException("Error executing Python script: " + exitCode + "\n" + errorOutput);
         }

         // Path to the generated JSON file
         File jsonFile = new File("D://demo_apis/hrms_chatbot_apis/response.json");

         // Check if the file exists
         if (!jsonFile.exists()) {
             throw new RuntimeException("JSON file not found after script execution.");
         }
         
         Object object = objectMapper.readValue(jsonFile, Object.class);
         LocalDateTime chatTime = LocalDateTime.now();
         ChatHistory chatHistory = new ChatHistory();
         chatHistory.setEmployeeId(employee.getEmployeeID());
         chatHistory.setRequestMessage(chatRequest.getPrompt());
         chatHistory.setChatTime(chatTime);
         chatHistory.setResponseMessage(object.toString());
         System.out.println(chatHistory);
         chatRepo.save(chatHistory);

         // Return the JSON content as the response
         return object;
     } 
    catch(Exception e){
    	
         throw new RuntimeException("Error executing Python script: " + e.getMessage(), e);
     }
 }
 
 public ChatHistoryResponse getChatHistoryOfEmployee(String employeeId){
	 EmployeeData employee = employeeRepo.findByemployeeID(employeeId);
	 
	 if(employee == null || employeeId.isEmpty()) {
		 ChatHistoryResponse response = new ChatHistoryResponse();
		 response.setResponseCode(HttpStatus.NOT_FOUND);
		 response.setMessage("Employee Not Found for this Id !");
		 return response;
	 }
	 List<ChatHistory> chatHistory = chatRepo.findByemployeeId(employeeId);
	 if(chatHistory.isEmpty()) {
		 ChatHistoryResponse response = new ChatHistoryResponse();
		 response.setResponseCode(HttpStatus.NOT_FOUND);
		 response.setMessage("No History Found !");
		 return response; 
	 }
	 ChatHistoryResponse response = new ChatHistoryResponse();
	 response.setResponseCode(HttpStatus.OK);
	 response.setMessage("successfully got your history");
	 response.setChatHistory(chatHistory);
	 
	 return response;
	 
 }




}
	
    
	

