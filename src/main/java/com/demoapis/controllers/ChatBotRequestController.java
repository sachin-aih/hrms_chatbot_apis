package com.demoapis.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoapis.models.ChatHistory;
import com.demoapis.repositories.ChatHistoryRepository;
import com.demoapis.requestDtos.ChatBotRequestDto;
import com.demoapis.responseDtos.ChatHistoryResponse;
import com.demoapis.services.ChatBotServices;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping("/search")
public class ChatBotRequestController{
	
	@Autowired
	private ChatBotServices chatService;
	
	@Autowired
	private ChatHistoryRepository chatRepo;
	
	@GetMapping("/chat-history-data")
	public ResponseEntity<List<ChatHistory>> getAllChatHistoryData()
	{
		List<ChatHistory> chatHistories = chatRepo.findAll();
		return new ResponseEntity<List<ChatHistory>>(chatHistories, HttpStatus.OK);
		
	}
	
	@PostMapping("/send-message")
	public ResponseEntity<Object> chatBotConversation(@RequestBody ChatBotRequestDto request){
		Object object = chatService.chatBotResponse(request);
		return new ResponseEntity<Object>(object, HttpStatus.OK);
	}
	
	@PostMapping("/getChatHistory")
	public ResponseEntity<ChatHistoryResponse> getChatHistoryByEmployeeId(@RequestBody ChatBotRequestDto.HistoryById request){
	      
		ChatHistoryResponse response = chatService.getChatHistoryOfEmployee(request.getUserIdOrEmplyeeId());
		return new ResponseEntity<ChatHistoryResponse>(response, response.getResponseCode());
	}
	

}


