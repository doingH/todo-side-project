package com.sideprj.todoapp.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sideprj.todoapp.todo.model.TodoEntity;
import com.sideprj.todoapp.todo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/*
 * 내부에 @Component 어노테이션이 포함되어 있고 기능적으로 비즈니스 로직을 서비스임을 명시
 */
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
	
	public String testSerivce() {
		log.info("11122");
		TodoEntity todoEntity = TodoEntity.builder().title("Good Job").build();
		
		todoRepository.save(todoEntity);
		
		TodoEntity savedEntity = todoRepository.findById(todoEntity.getId()).get();
		
		return savedEntity.getTitle();
	}
}
