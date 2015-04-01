# dtobuilder
Automatically exported from code.google.com/p/dtobuilder

DtoBuilder is annotation based java library for filling DTO objects from e.g. domain model objects.

Usage is quite simple just annotate your DTO java bean with DtoProperty or DtoClass annotations. The annotations are used to mark from which classes the DTO should be filled from e.g.

```
@DtoProperty(sourceClass=Person.class)
public String getName() {
	return name;
}
```

this means that the name property should be fetched from Person java bean. By default the builder uses the same property name for the source but it can also be defined like this

```
@DtoProperty(sourceClass=Person.class, sourceProperty="bday")
public Date getBirthday() {
	return birthday;
}
```

so here the value is fetched from a property named bday.

If you have multiple properties and they all are named the same way in the source and in the dto  you can also annotate the whole dto class

```
@DtoClass(sourceClasses={Person.class})
public class PersonClassDto
```

this will fill all the properties in the dto with their counter parts from the Person class. You can also specify multiple classes from where to copy the properties. If the source classes have properties which are named the same way the latter class in the annotation always takes precedence. 

Using the actual builder is quite simple just create a new builder for the dto and call build-method with the source objects like this

```
final DtoBuilder<PersonDto> personDtoBuilder = 
			new DtoBuilder<PersonDto>(PersonDto.class);
final Person p = new Person();
final Date now = new Date();
p.setAddress("Test street1");
p.setBday(now);
p.setName("Test Name");
final PersonDto pd = personDtoBuilder.build(p);
```

You can also dismantle the dto using the dismatler like this:

```
final DtoDismantler<PersonDto, Person> personDtoDismantler = 
			new DtoDismantler<PersonDto, Person>(PersonDto.class, Person.class);
final Person p2 = personDtoDismantler.dismantle(pd);
```


And now you get the source class with the same property values as the dto.
