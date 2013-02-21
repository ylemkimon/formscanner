package org.albertoborsetta.formscanner.commons.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public abstract class CommonConfiguration {

	protected final Configuration configuration;

	public CommonConfiguration(final Configuration configuration) {
		Validate.notNull(configuration, "null configuration");
		this.configuration = configuration;
	}

	public static <T> Collection<T> removeElements(final Collection<T> collection, final T... elements) {
		final Collection<T> remove = Arrays.asList(elements);
		@SuppressWarnings("unchecked")
		final Collection<T> clean = CollectionUtils.subtract(collection, remove);
		return clean;
	}

	protected List<String> cleanList(final String key) {
		final String[] array = configuration.getStringArray(key);
		final List<String> list = Arrays.asList(array);
		final Collection<String> cleanCollection = removeElements(list, StringUtils.EMPTY);
		return new ArrayList<String>(cleanCollection);
	}

	protected String[] cleanArray(final String key) {
		final List<String> list = cleanList(key);
		return list.toArray(new String[list.size()]);
	}

}
