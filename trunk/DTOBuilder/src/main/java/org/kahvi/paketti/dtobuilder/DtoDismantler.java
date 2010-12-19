package org.kahvi.paketti.dtobuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kahvi.paketti.dtobuilder.annotations.DtoClass;
import org.kahvi.paketti.dtobuilder.annotations.DtoProperty;
import org.kahvi.paketti.dtobuilder.util.DtoUtil;

public class DtoDismantler<T, DT> {

	private final Class<T> type;
	private final Class<DT> targetType;
	private DtoClass dtoClass;
	private List<Method> getters = new ArrayList<Method>();
	private final Map<Method, DtoProperty> dtoPropertyClasses = new HashMap<Method, DtoProperty>();

	public DtoDismantler(final Class<T> type, final Class<DT> targetType) {
		this.type = type;
		this.targetType = targetType;

		if (this.type.isAnnotationPresent(DtoClass.class)) {
			this.dtoClass = this.type.getAnnotation(DtoClass.class);
		}

		for (final Method m : type.getMethods()) {
			if (m.getName().startsWith("get")) {
				getters.add(m); 
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

	public DT dismantle(final T source)
			throws DtoConfigurationException {
		
		try {
			final DT target = this.targetType.newInstance();
			dismantleFromClass(source, target);
			dismantleFromProperties(source, target);
			return target;
		} catch (Exception e) {
			throw new DtoConfigurationException(
					"To use this method target class must have public non argument constructor!", e);
		} 
		
	}
	
	public void dismantleToTarget(final T source, final DT target)
	throws DtoConfigurationException {
		dismantleFromClass(source, target);
		dismantleFromProperties(source, target);
	}

	private void dismantleFromClass(final T source, final DT target)
			throws DtoConfigurationException {
		if (this.dtoClass == null 
			|| !Arrays.asList(this.dtoClass.sourceClasses()).contains(target.getClass())) {
			return;
		}

		for (final Method m : this.getters) {
			insertProperties(m, source, target);
		}
	}

	private void dismantleFromProperties(final T source, final DT target)
			throws DtoConfigurationException {
		for (final Method m : dtoPropertyClasses.keySet()) {
			final DtoProperty p = dtoPropertyClasses.get(m);
			
			if(!target.getClass().equals(p.sourceClass())) {
				continue;
			}
			
			if ("".equals(p.sourceProperty())) {
				insertProperties(m, source, target);
			} else {
				try {
					final Object result = m.invoke(source);
					target.getClass().getMethod("set" + DtoUtil.capitalize(p.sourceProperty()), 
							result.getClass()).invoke(target, result);
				} catch (NoSuchMethodException e) {

				} catch (Exception e) {
					throw new DtoConfigurationException("Error setting properties", e);
				}
			}

		}
	}

	private void insertProperties(final Method m, final T source,
			final DT target) throws DtoConfigurationException {
		try {
			final Object result = m.invoke(source);
			target.getClass().getMethod(DtoUtil.getPropertySetterName(m), result.getClass()).invoke(target, result);

		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new DtoConfigurationException("Error setting properties", e);
		}
	}
}
