package com.demoapis.responseDtos;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.demoapis.models.ChatHistory;
import lombok.Data;

@Data
public class ChatHistoryResponse{
	private HttpStatus responseCode;
	private String message;
	private List<ChatHistory> chatHistory;

}
