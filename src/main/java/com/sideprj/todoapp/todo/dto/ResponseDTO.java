package com.sideprj.todoapp.todo.dto;
import java.util.List;
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
public class ResponseDTO<T> {
	private String error;
	private List<T> data;
}
