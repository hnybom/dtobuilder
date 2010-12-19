package org.kahvi.paketti.dtobuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kahvi.paketti.dtobuilder.annotations.DtoClass;
import org.kahvi.paketti.dtobuilder.annotations.DtoProperty;
import org.kahvi.paketti.dtobuilder.util.DtoUtil;

public class DtoBuilder<T> {

	private final Class<T> type;
	private DtoClass dtoClass;
	private List<Method> setters = new ArrayList<Method>();
	private final Map<Method, DtoProperty> dtoPropertyClasses = new HashMap<Method, DtoProperty>();

	public DtoBuilder(final Class<T> type) {
		this.type = type;

		if (this.type.isAnnotationPresent(DtoClass.class)) {
			this.dtoClass = this.type.getAnnotation(DtoClass.class);
		}

		for (final Method m : type.getMethods()) {
			if (m.getName().startsWith("set")) {
				setters.add(m);
			} 

			if (m.isAnnotationPresent(DtoProperty.class)) {
				if (m.getName().startsWith("get")) {
					dtoPropertyClasses.put(m,
							m.getAnnotation(DtoProperty.class));
				} else {
					throw new IllegalArgumentException(
							"DtoProperty is allowed only for getter methods!");
				}
			}
		}

	}

	public T build(final Object... sources) throws DtoConfigurationException {
		final Map<String, Object> sourceMap = new HashMap<String, Object>();
		for (final Object o : sources) {
			sourceMap.put(o.getClass().getCanonicalName(), o);
		}
		try {
			final T target = this.type.newInstance();
			buildFromClassAnnotations(target, sourceMap);
			buildFromPropertyAnnotations(target, sourceMap);
			return target;
		} catch (Exception e) {
			throw new DtoConfigurationException("Dto class must have public non argument constructor!", e);
		}
		

	}

	private void buildFromPropertyAnnotations(final T target,
			Map<String, Object> sourceMap) throws DtoConfigurationException {
		for (final Method m : dtoPropertyClasses.keySet()) {
			final DtoProperty p = dtoPropertyClasses.get(m);
			final Object source = sourceMap.get(p.sourceClass()
					.getCanonicalName());

			if (source == null) {
				continue;
			}

			try {
				if ("".equals(p.sourceProperty())) {
					final Object result = source.getClass().getMethod(m.getName()).invoke(source);
					this.type.getMethod(DtoUtil.getPropertySetterName(m), result.getClass()).invoke(target, result);
				}
				else {
					final Object result = source.getClass().getMethod("get" + DtoUtil.capitalize(p.sourceProperty())).invoke(source);
					this.type.getMethod(DtoUtil.getPropertySetterName(m), result.getClass()).invoke(target, result);
				}
			} catch (Exception e) {
				throw new DtoConfigurationException("Error setting properties", e);
			} 

		}
	}

	private void buildFromClassAnnotations(final T target,
			Map<String, Object> sourceMap) throws DtoConfigurationException {
		
		if (this.dtoClass == null) {
			return;
		}
		
		for (final Class<?> c : this.dtoClass.sourceClasses()) {
			final Object source = sourceMap.get(c.getCanonicalName());
			if (source == null) {
				continue;
			}
			for (final Method m : setters) {
				// Property annotation takes presedence
				if(m.isAnnotationPresent(DtoProperty.class)) {
					continue;
				}
				try {
					final Method getter = source.getClass().getMethod(
							DtoUtil.getPropertyGetterName(m), new Class<?>[] {});
					m.invoke(target, getter.invoke(source));
				} catch (NoSuchMethodException e) {
					// ignore
				} catch (Exception e) {
					throw new DtoConfigurationException("Error setting properties", e);
				} 
			}

		}
	}

	

}
