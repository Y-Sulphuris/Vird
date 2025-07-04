package com.ydo4ki.vird.lang;

import com.ydo4ki.vird.ast.Location;

/**
 * @since 5/7/2025 1:27 PM
 * @author Sulphuris
 */
public class LangValidationException extends LangException {
	
	public LangValidationException(Location location, String message) {
		super(location, message);
	}
	
	public LangValidationException(Location location, String message, Throwable cause) {
		super(location, message, cause);
	}
	
	public LangValidationException(Location location, String message, String rawMessage) {
		super(location, message, rawMessage);
	}
	
	public LangValidationException(Location location, String message, Throwable cause, String rawMessage) {
		super(location, message, cause, rawMessage);
	}
	
	public LangValidationException(Location location, Throwable cause, String rawMessage) {
		super(location, cause, rawMessage);
	}
	
	@Override
	public String errName() {
		return "Validation Error";
	}
}
