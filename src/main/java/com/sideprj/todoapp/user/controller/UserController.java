package com.sideprj.todoapp.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sideprj.todoapp.security.TokenProvider;
import com.sideprj.todoapp.todo.dto.ResponseDTO;
import com.sideprj.todoapp.user.dto.UserDTO;
import com.sideprj.todoapp.user.model.UserEntity;
import com.sideprj.todoapp.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			// ������Ʈ�� �̿��� ������ ���� �����
			UserEntity user = UserEntity.builder()
							.email(userDTO.getEmail())
							.username(userDTO.getUsername())
							.password(passwordEncoder.encode(userDTO.getPassword()))
							.build();
			// ���񽺸� �̿��� �������丮�� ���� ����
			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = UserDTO.builder()
							.email(registeredUser.getEmail())
							.id(registeredUser.getId())
							.username(registeredUser.getUsername())
							.build();
			// ���� ������ �׻� �ϳ��̹Ƿ� �׳� ����Ʈ�� �������ϴ� ResponseDTO�� ������� �ʰ� �׳� UserDTO ����.
			return ResponseEntity.ok(responseUserDTO);
			
		} catch (Exception e) {
			// ���ܰ� ���� ��� bad �������� ����.
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
			
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		
		UserEntity user = userService.getByCredentials(
						userDTO.getEmail(),
						userDTO.getPassword(),
						passwordEncoder);

		if(user != null) {
			
			final String token = tokenProvider.create(user);
			
			// ��ū ����
			final UserDTO responseUserDTO = UserDTO.builder()
							.token(token)
							.email(user.getEmail())
							.id(user.getId())
							.build();
			return ResponseEntity.ok().body(responseUserDTO);
			
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed.").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
