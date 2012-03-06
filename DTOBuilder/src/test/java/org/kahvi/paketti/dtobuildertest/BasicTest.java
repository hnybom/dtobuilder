package org.kahvi.paketti.dtobuildertest;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.kahvi.paketti.dtobuilder.DtoBuilder;
import org.kahvi.paketti.dtobuilder.DtoConfigurationException;
import org.kahvi.paketti.dtobuilder.DtoDismantler;
import org.kahvi.paketti.dtobuildertest.model.Person;
import org.kahvi.paketti.dtobuildertest.model.PersonClassDto;
import org.kahvi.paketti.dtobuildertest.model.PersonDto;


public class BasicTest {
	 
	@Test
	public void basicTest() {
		final DtoBuilder<PersonDto> personDtoBuilder = 
			new DtoBuilder<PersonDto>(PersonDto.class);
		final DtoBuilder<PersonClassDto> personClassDtoBuilder = 
			new DtoBuilder<PersonClassDto>(PersonClassDto.class);
		
		final DtoDismantler<PersonDto, Person> personDtoDismantler = 
			new DtoDismantler<PersonDto, Person>(PersonDto.class, Person.class);
		final DtoDismantler<PersonClassDto, Person> personClassDtoDismantler = 
			new DtoDismantler<PersonClassDto, Person>(PersonClassDto.class, Person.class);
		
		final Date now = new Date();
		final Person p = new Person();
		p.setAddress("Test street1");
		p.setBday(now);
		p.setName("Test Name");
		
		try {
			final PersonDto pd = personDtoBuilder.build(p);
			final PersonClassDto pcd = personClassDtoBuilder.build(p);
			
			Assert.assertEquals(p.getName(), pd.getName());
			Assert.assertEquals(p.getBday(), pd.getBirthday());
			Assert.assertEquals(p.getName(), pcd.getName());
			Assert.assertEquals(p.getBday(), pcd.getBirthday());
			
			final Person p2 = personDtoDismantler.dismantle(pd);
			Assert.assertEquals(p2.getName(), pd.getName());
			Assert.assertEquals(p2.getBday(), pd.getBirthday());
			
			final Person p3 = personClassDtoDismantler.dismantle(pcd);
			Assert.assertEquals(p3.getName(), pcd.getName());
			Assert.assertEquals(p3.getBday(), pcd.getBirthday());
			
			
		} catch (DtoConfigurationException e) {
			Assert.assertTrue(false);
            e.printStackTrace();
		}
		
		
	}
	
}
