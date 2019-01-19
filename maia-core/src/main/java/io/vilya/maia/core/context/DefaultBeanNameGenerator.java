/**
 * 
 */
package io.vilya.maia.core.context;

import java.util.Locale;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * @author erkea <erkea@vilya.io>
 *
 */
public class DefaultBeanNameGenerator implements BeanNameGenerator {

	private static final Logger log = LoggerFactory.getLogger(DefaultBeanNameGenerator.class);
	
	@Override
	public String generate(Class<?> bean) {
		Preconditions.checkNotNull(bean);
		
		String beanName = null;
		
		Named named = bean.getAnnotation(Named.class);
		if (named != null) {
			beanName = named.value();
		}
		
		if (Strings.isNullOrEmpty(beanName)) {
			beanName = bean.getSimpleName();
		}

		return capitalize(beanName);
	}
	
	
	private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
    }
	
}
