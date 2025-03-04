package cn.itcast.exception;

import javax.transaction.SystemException;

public class SysException extends SystemException{

	public SysException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
