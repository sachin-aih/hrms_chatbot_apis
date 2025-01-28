package com.demoapis.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demoapis.models.ChatHistory;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long>{
     
	// return chathistory of employee
     public List<ChatHistory> findByemployeeId(String employeeId);
}
