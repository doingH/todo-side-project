package com.sideprj.todoapp.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sideprj.todoapp.todo.dto.ResponseDTO;
import com.sideprj.todoapp.todo.dto.TodoDTO;
import com.sideprj.todoapp.todo.model.TodoEntity;
import com.sideprj.todoapp.todo.service.TodoService;


@RestController
@RequestMapping("/todo")
public class TodoController {
	
	@Autowired
	private TodoService todoService;

	/* 
	 * PathVariable
	 * {id}�� ���� URI�� �̿��Ͽ� �Ķ���͸� ���� �� ����
	 */
	@GetMapping("/{id}")
	public String testPathVariables(@PathVariable(required = false) int id) {
		return "hello world";
	}
	
	/*
	 * RequestParam
	 * ?id={id}������ URI�� �̿��Ͽ� �Ķ���͸� ���� �� ����
	 */
	@GetMapping("/requestParam")
	public String testRequestParam(@RequestParam(required = false) int id) {
		return "hello world";
	}
	
	/*
	 * RequestBody
	 * �⺻ �ڷ����ܿ� ������Ʈ������ ������ �ڷ����� �Ķ���ͷ� ���� �� ����
	 */
	@GetMapping("/requestBody")
	public String testRequestBody(@RequestBody TodoDTO todoDto) {
		return "hello world";
	}
	
	/*
	 * responseBody
	 * return ���� JSON���·� �ٲٰ� HttpResponse�� ��� ��ȯ�Ѵ�.
	 */
	@GetMapping("/responseBody")
	public ResponseDTO<String> testResponseBody(@RequestBody TodoDTO todoDto) {
		List<String> list = new ArrayList<>();
		list.add("hello world");
		ResponseDTO<String> reponse = ResponseDTO.<String>builder().data(list).build();
		return reponse;
	}
	
	/*
	 * responseEntity
	 * ���� Body�ܿ� status�� header�� �����Ѵ�.
	 */
	@GetMapping("/responseEntity")
	public ResponseEntity<?> testResponseEntity(TodoDTO todoDto) {
		List<String> list = new ArrayList<>();
		String str = todoService.testSerivce();
		list.add(str);
		ResponseDTO<String> reponse = ResponseDTO.<String>builder().data(list).build();
//		return ResponseEntity.badRequest().body(reponse);
		return ResponseEntity.ok().body(reponse);
	}
	
	
	@PostMapping()
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto
			,@AuthenticationPrincipal String userId) {
		
		try {
			
			// (1) TodoEntity�� ��ȯ�Ѵ�.
			TodoEntity entity = TodoDTO.toEntity(dto);

			// (2) id�� null�� �ʱ�ȭ �Ѵ�. ���� ��ÿ��� id�� ����� �ϱ� �����̴�.
			entity.setId(null);

			// (3) �ӽ� ���� ���̵� ���� �� �ش�.
			entity.setUserId(userId);

			// (4) ���񽺸� �̿��� Todo��ƼƼ�� �����Ѵ�.
			List<TodoEntity> entities = todoService.create(entity);

			// (5) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO����Ʈ�� ��ȯ�Ѵ�.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

			// (6) ��ȯ�� TodoDTO����Ʈ�� �̿���ResponseDTO�� �ʱ�ȭ�Ѵ�.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

			// (7) ResponseDTO�� �����Ѵ�.
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			
			// (8) Ȥ�� ���ܰ� ���� ��� dto��� error�� �޽����� �־� �����Ѵ�.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
//		System.out.println("UserID : " + userId);
		
		// (1) ���� �޼����� retrieve�޼��带 ����� Todo����Ʈ�� �����´�
		List<TodoEntity> entities = todoService.retrieve(userId);

		// (2) �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO����Ʈ�� ��ȯ�Ѵ�.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

		// (6) ��ȯ�� TodoDTO����Ʈ�� �̿���ResponseDTO�� �ʱ�ȭ�Ѵ�.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

		// (7) ResponseDTO�� �����Ѵ�.
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto
			,@AuthenticationPrincipal String userId) {

		// dto to entity
		TodoEntity entity = TodoDTO.toEntity(dto);

		// id�� temporaryUserId�� �ʱ�ȭ �Ѵ�.
		entity.setUserId(userId);

		// ���񽺸� �̿��� entity�� ������Ʈ �Ѵ�.
		List<TodoEntity> entities = todoService.update(entity);

		// �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO����Ʈ�� ��ȯ�Ѵ�.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

		// ��ȯ�� TodoDTO����Ʈ�� �̿���ResponseDTO�� �ʱ�ȭ�Ѵ�.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

		// ResponseDTO�� �����Ѵ�.
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto
			,@AuthenticationPrincipal String userId) {
		try {

			// TodoEntity�� ��ȯ�Ѵ�.
			TodoEntity entity = TodoDTO.toEntity(dto);

			// �ӽ� ���� ���̵� ���� �� �ش�.
			entity.setUserId(userId);

			// ���񽺸� �̿��� entity�� ���� �Ѵ�.
			List<TodoEntity> entities = todoService.delete(entity);

			// �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO����Ʈ�� ��ȯ�Ѵ�.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

			// ��ȯ�� TodoDTO����Ʈ�� �̿���ResponseDTO�� �ʱ�ȭ�Ѵ�.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

			// ResponseDTO�� �����Ѵ�.
			return ResponseEntity.ok().body(response);
			
		} catch (Exception e) {
			
			// Ȥ�� ���ܰ� ���� ��� dto��� error�� �޽����� �־� �����Ѵ�.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
}
