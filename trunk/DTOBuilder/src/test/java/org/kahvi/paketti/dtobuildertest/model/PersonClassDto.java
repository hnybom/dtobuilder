package org.kahvi.paketti.dtobuildertest.model;

import java.util.Date;

import org.kahvi.paketti.dtobuilder.annotations.DtoClass;
import org.kahvi.paketti.dtobuilder.annotations.DtoProperty;

@DtoClass(sourceClasses={Person.class})
public class PersonClassDto {
	
	private String name;
	private Date birthday;
	
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
