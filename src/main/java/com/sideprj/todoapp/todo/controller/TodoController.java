package com.sideprj.todoapp.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sideprj.todoapp.todo.dto.ResponseDTO;
import com.sideprj.todoapp.todo.dto.TodoDTO;
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
	
	
	
}
