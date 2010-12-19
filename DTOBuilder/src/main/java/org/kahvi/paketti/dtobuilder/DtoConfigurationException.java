package org.kahvi.paketti.dtobuilder;

public class DtoConfigurationException extends Exception {

	private static final long serialVersionUID = -6576826602953203255L;

	public DtoConfigurationException(String msg) {
		super(msg);
	}
	
	public DtoConfigurationException(String msg, Throwable t) {
		super(msg, t);
	}
	 
}
