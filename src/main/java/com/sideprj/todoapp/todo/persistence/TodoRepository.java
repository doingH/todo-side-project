package com.sideprj.todoapp.todo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sideprj.todoapp.todo.model.TodoEntity;
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
	
	/*
	 * ?1은 메서드의 매개변수의 순서 위치이다.
	 */
//	@Query("select * from Todo a where a.userId = ?1")
	
	/*
	 * 함수 이름을 파싱해서 select * from TodoRepository WHERE userId = '{userId}' 와 같은 쿼리로 작성해 실행
	 */
	List<TodoEntity> findByUserId(String userId);

}
