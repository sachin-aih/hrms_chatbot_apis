package com.demoapis.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapis.models.EmployeeData;
import com.demoapis.repositories.ChatHistoryRepository;
import com.demoapis.requestDtos.ChatBotRequestDto;
import com.demoapis.services.ChatBotServices;

@RestController
@RequestMapping("/search")
public class ChatBotRequestController{
	
	@Autowired
	private ChatBotServices chatService;
	
	@Autowired
	private ChatHistoryRepository chatRepo;
	
	@GetMapping("/employees")
	public ResponseEntity<List<EmployeeData>> getAllEmployees()
	{
		List<EmployeeData> employees = chatService.getAllEmployees();
		return new ResponseEntity<List<EmployeeData>>(employees, HttpStatus.OK);
		
	}
	
	@PostMapping("/send-message")
	public ResponseEntity<Object> chatBotConversation(@RequestBody ChatBotRequestDto request){
		Object object = chatService.chatBotResponse(request);
		return new ResponseEntity<Object>(object, HttpStatus.OK);
	}
	

}


