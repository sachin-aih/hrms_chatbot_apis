package com.demoapis.requestDtos;
import lombok.Data;

@Data
public class ChatBotRequestDto{
	
	private String userIdOrEmployeeId;
	private String prompt;
	
	@Data
	public static class HistoryById{
		
		private String userIdOrEmplyeeId;
		
	}

}
