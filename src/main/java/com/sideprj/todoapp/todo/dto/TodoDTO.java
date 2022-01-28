package com.sideprj.todoapp.todo.dto;

import com.sideprj.todoapp.todo.model.TodoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 
 * ������Ʈ ������ ���� ������ ������ �ϳ��� Builder������ ����� ��ü�� ������ �� �ִ�.
 * �����ڸ� �̿��ϴ� �� ���� ����
 */
@Builder
/*
 * �Ű� ���� ���� �����ڸ� ���� 
 */
@NoArgsConstructor
/*
 * ��� �Ű������� �Ķ���ͷ� �ϴ� �����ڸ� ����
 */
@AllArgsConstructor
/*
 * getter�� setter�� ����
 */
@Data
/*
 * DTO�� �ܺ� ����ڿ��� ������ ���̺� ���� �� �������� ĸ��ȭ �Ѵ�.
 * ����, Ŭ���̾�Ʈ�� �ʿ��� �𵨵��� �߰��Ͽ� ������ �� �ִ�.
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
	 *  ĸ��ȭ�� ���� DTO�� ��û BODY�� ������ Entity�� ��ȯ�ϱ� ���� �Լ�
	 */
	public static TodoEntity toEntity(final TodoDTO dto) {
		return TodoEntity.builder()
						.id(dto.getId())
						.title(dto.getTitle())
						.done(dto.isDone())
						.build();
	}
	
}
