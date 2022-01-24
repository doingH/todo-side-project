package com.sideprj.todoapp.todo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sideprj.todoapp.todo.model.TodoEntity;
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
	
	/*
	 * ?1�� �޼����� �Ű������� ���� ��ġ�̴�.
	 */
//	@Query("select * from Todo a where a.userId = ?1")
	
	/*
	 * �Լ� �̸��� �Ľ��ؼ� select * from TodoRepository WHERE userId = '{userId}' �� ���� ������ �ۼ��� ����
	 */
	List<TodoEntity> findByUserId(String userId);

}
