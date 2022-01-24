package com.sideprj.todoapp.todo.dto;
import java.util.List;
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
public class ResponseDTO<T> {
	private String error;
	private List<T> data;
}
