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
 * Ŭ������ Entity�� ����
 */
@Entity

@Table(name = "TODOTB")
/*
 * Entity�� DB�� ���̺�� ��Ű���� ǥ��
 * ORM�� ���ǻ���
 * 1. @NoArgsConstructor �ʿ�
 * 2. Getter/Setter �ʿ�
 * 3. Primary Key �ʿ�
 */
public class TodoEntity {

	/*
	 * �⺻ Ű�� �� �ʵ带 ���� 
	 */
	@Id
	/*
	 * ID �ڵ����� 
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
