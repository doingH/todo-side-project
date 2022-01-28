package com.sideprj.todoapp.todo.dto;

import com.sideprj.todoapp.todo.model.TodoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 
 * 오브젝트 생성을 위한 디자인 패턴중 하나로 Builder패턴을 사용해 객체를 생성할 수 있다.
 * 생성자를 이용하는 것 보다 간편
 */
@Builder
/*
 * 매개 변수 없는 생성자를 구현 
 */
@NoArgsConstructor
/*
 * 모든 매개변수를 파라미터로 하는 생성자를 구현
 */
@AllArgsConstructor
/*
 * getter와 setter를 구현
 */
@Data
/*
 * DTO는 외부 사용자에게 내부의 테이블 구조 및 로직등을 캡슐화 한다.
 * 또한, 클라이언트에 필요한 모델들을 추가하여 포함할 수 있다.
 */
public class TodoDTO {
	
	private String id;
	private String title;
	private boolean done;
	
	public TodoDTO(final TodoEntity entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.done = entity.isDone();
	}
	
	/*
	 *  캡슐화를 위한 DTO를 요청 BODY로 받으면 Entity로 변환하기 위한 함수
	 */
	public static TodoEntity toEntity(final TodoDTO dto) {
		return TodoEntity.builder()
						.id(dto.getId())
						.title(dto.getTitle())
						.done(dto.isDone())
						.build();
	}
	
}
