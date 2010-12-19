package org.kahvi.paketti.dtobuildertest.model;

import java.util.Date;

public class Person {

	private String name;
	private Date bday;
	private String address;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBday() {
		return bday;
	}
	public void setBday(Date bday) { 
		this.bday = bday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
