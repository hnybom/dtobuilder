package org.kahvi.paketti.dtobuildertest.model;

import java.util.Date;

import org.kahvi.paketti.dtobuilder.annotations.DtoProperty;

public class PersonDto {
	
	private String name;
	private Date birthday;
	
	@DtoProperty(sourceClass=Person.class)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@DtoProperty(sourceClass=Person.class, sourceProperty="bday")
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
	 
}
