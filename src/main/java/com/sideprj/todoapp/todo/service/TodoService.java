package com.sideprj.todoapp.todo.service;

import java.util.List;
import java.util.Optional;

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
	
	public List<TodoEntity> create(final TodoEntity entity) {
		
		// Validations
		validate(entity);

		todoRepository.save(entity);
		log.info("Entity Id : {} is saved.", entity.getId());
		return todoRepository.findByUserId(entity.getUserId()); 
		
	}
	
	public List<TodoEntity> retrieve(final String userId) {
		return todoRepository.findByUserId(userId);
	}
	
	public List<TodoEntity> update(final TodoEntity entity) {
		
		validate(entity);

		// 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트 할 수 없기 때문이다.
		final Optional<TodoEntity> todoEntityOp = todoRepository.findById(entity.getId());

		//original에 값이 있으면 소비자를 호출 ()
		todoEntityOp.ifPresent(todo -> {
			// 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌운다.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());

			// (4) 데이터베이스에 새 값을 저장한다.
			todoRepository.save(todo);
		});

		// 2.3.2 Retrieve Todo에서 만든 메서드를 이용해 유저의 모든 Todo 리스트를 리턴한다.
		return retrieve(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		
		validate(entity);

		try {
			// 엔티티를 삭제한다.
			todoRepository.delete(entity);
		} catch(Exception e) {
			// exception 발생시 id와 exception을 로깅한다.
			log.error("error deleting entity ", entity.getId(), e);

			// 컨트롤러로 exception을 날린다. 데이터베이스 내부 로직을 캡슐화 하기 위해 e를 리턴하지 않고 새 exception 오브젝트를 리턴한다.
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		// (5) 새 Todo리스트를 가져와 리턴한다.
		return retrieve(entity.getUserId());
	}
	
	
	private void validate(final TodoEntity entity) {
		
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}

		if(entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
	}
	
}
