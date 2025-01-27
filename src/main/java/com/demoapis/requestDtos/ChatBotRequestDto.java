package com.demoapis.requestDtos;
import lombok.Data;

@Data
public class ChatBotRequestDto{
	
	private String userIdOrEmployeeId;
	private String prompt;

}
