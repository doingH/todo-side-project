package com.sideprj.todoapp.todo.model;

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
 * Entity�� DB�� ���̺�� ��Ű���� ǥ��
 */
public class TodoEntity {

	private String id;
	private String userId;
	private String title;
	private boolean done;
	
}
