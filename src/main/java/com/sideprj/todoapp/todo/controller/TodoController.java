package com.sideprj.todoapp.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	 * {id}와 같이 URI를 이용하여 파라미터를 받을 수 있음
	 */
	@GetMapping("/{id}")
	public String testPathVariables(@PathVariable(required = false) int id) {
		return "hello world";
	}
	
	/*
	 * RequestParam
	 * ?id={id}형태의 URI를 이용하여 파라미터를 받을 수 있음
	 */
	@GetMapping("/requestParam")
	public String testRequestParam(@RequestParam(required = false) int id) {
		return "hello world";
	}
	
	/*
	 * RequestBody
	 * 기본 자료형외에 오브젝트같은거 복잡한 자료형을 파라미터로 받을 수 있음
	 */
	@GetMapping("/requestBody")
	public String testRequestBody(@RequestBody TodoDTO todoDto) {
		return "hello world";
	}
	
	/*
	 * responseBody
	 * return 값을 JSON형태로 바꾸고 HttpResponse에 담아 반환한다.
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
	 * 응답 Body외에 status나 header를 지정한다.
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
	
	
	@PostMapping("/create")
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
		
		try {
			
			String userid = "temporary-user";
			// (1) TodoEntity로 변환한다.
			TodoEntity entity = TodoDTO.toEntity(dto);

			// (2) id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문이다.
			entity.setId(null);

			// (3) 임시 유저 아이디를 설정 해 준다.
			entity.setUserId(userid);

			// (4) 서비스를 이용해 Todo엔티티를 생성한다.
			List<TodoEntity> entities = todoService.create(entity);

			// (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

			// (6) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

			// (7) ResponseDTO를 리턴한다.
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			
			// (8) 혹시 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴한다.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
}
