package es.usa.isa.exceptions;

public class WebFacadeException extends RuntimeException{

	private static final long serialVersionUID = -255507468837473570L;
	private String input, message;
	
	public WebFacadeException() {
	}

	public WebFacadeException(String input,String message) {
		super(message);
		this.input=input;
		this.message=message;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
