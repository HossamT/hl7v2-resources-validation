package gov.nist.healthcare.resources.domain;

import org.xml.sax.SAXParseException;

public class XMLError {

	private int line;
	private int column;
	private String message;
	
	
	
	public XMLError() {
		super();
		// TODO Auto-generated constructor stub
	}
	public XMLError(SAXParseException e) {
		super();
		this.line = e.getLineNumber();
		this.column = e.getColumnNumber();
		String msg = e.getMessage();
		this.message = msg.substring(msg.indexOf(":") + 2);
	}
	public XMLError(int line, int column, String message) {
		super();
		this.line = line;
		this.column = column;
		this.message = message;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "XMLError \n"+
				"Line : "+line+"\n"+
				"Column :"+column+"\n"+
				"Message : "+message+"\n";
	}
	
	
	
	
}
