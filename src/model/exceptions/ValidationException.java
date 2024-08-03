package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	static final long serialVersionUID = 1L;
	
	private Map<String, String> erros = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros() {
		return erros;
	}
	
	public void addError(String fieldName, String erroMessage) {
		erros.put(fieldName, erroMessage);
	}

}