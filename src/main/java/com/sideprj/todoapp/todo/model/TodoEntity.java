package com.sideprj.todoapp.todo.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
 * 클래스를 Entity로 지정
 */
@Entity

@Table(name = "TODOTB")
/*
 * Entity는 DB의 테이블과 스키마를 표현
 * ORM시 주의사항
 * 1. @NoArgsConstructor 필요
 * 2. Getter/Setter 필요
 * 3. Primary Key 필요
 */
public class TodoEntity {

	/*
	 * 기본 키가 될 필드를 지정 
	 */
	@Id
	/*
	 * ID 자동생성 
	 */
	@GeneratedValue(generator="system-uuid")
	/*
	 * custom Generator 
	 */
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;
	private String userId;
	private String title;
	private boolean done;
	
}
