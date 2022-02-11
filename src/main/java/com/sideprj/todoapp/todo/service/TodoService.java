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
 * ���ο� @Component ������̼��� ���ԵǾ� �ְ� ��������� ����Ͻ� ������ �������� ���
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

		// �Ѱܹ��� ��ƼƼ id�� �̿��� TodoEntity�� �����´�. �������� �ʴ� ��ƼƼ�� ������Ʈ �� �� ���� �����̴�.
		final Optional<TodoEntity> todoEntityOp = todoRepository.findById(entity.getId());

		//original�� ���� ������ �Һ��ڸ� ȣ�� ()
		todoEntityOp.ifPresent(todo -> {
			// ��ȯ�� TodoEntity�� �����ϸ� ���� �� entity�� ������ ���� �����.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());

			// (4) �����ͺ��̽��� �� ���� �����Ѵ�.
			todoRepository.save(todo);
		});

		// 2.3.2 Retrieve Todo���� ���� �޼��带 �̿��� ������ ��� Todo ����Ʈ�� �����Ѵ�.
		return retrieve(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		
		validate(entity);

		try {
			// ��ƼƼ�� �����Ѵ�.
			todoRepository.delete(entity);
		} catch(Exception e) {
			// exception �߻��� id�� exception�� �α��Ѵ�.
			log.error("error deleting entity ", entity.getId(), e);

			// ��Ʈ�ѷ��� exception�� ������. �����ͺ��̽� ���� ������ ĸ��ȭ �ϱ� ���� e�� �������� �ʰ� �� exception ������Ʈ�� �����Ѵ�.
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		// (5) �� Todo����Ʈ�� ������ �����Ѵ�.
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
