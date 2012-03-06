package org.kahvi.paketti.dtobuilder.util;

import java.lang.reflect.Method;
import org.kahvi.paketti.dtobuilder.annotations.DtoProperty;

public class DtoUtil {
	
    public static boolean isPropertyNameGiven(final DtoProperty dtoProperty) {
        return !"".equals(dtoProperty.sourceProperty());
    }
    
	public static String capitalize(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	public static String getPropertyGetterName(final Method m) {
		final String base = m.getName().substring(3);
		return "get" + base;
	}

	public static String getPropertySetterName(final Method m) {
		final String base = m.getName().substring(3);
		return "set" + base; 
	}
}
