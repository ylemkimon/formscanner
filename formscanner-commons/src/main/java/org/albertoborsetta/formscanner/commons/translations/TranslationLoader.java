package org.albertoborsetta.formscanner.commons.translations;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.Validate;

public class TranslationLoader {

	private String name;

	public void setName(final String name) {
		this.name = name;
	}

	public Configuration load() throws TranslationException {
		try {
			validateFields();
			final Configuration configuration = loadConfiguration();
			return configuration;
		} catch (final Exception e) {
			throw new TranslationException();
		}
	}

	private void validateFields() {
		Validate.isTrue(isNotBlank(name), format("invalid name '%s'", name));
	}

	private Configuration loadConfiguration() throws TranslationException {
		try {
			final TranslationClassLoader loader = TranslationClassLoader.getInstance();
			final String resource = loader.getResource(name);
			Validate.notNull(resource, format("missing resource '%s'", name));
			final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
			propertiesConfiguration.setEncoding("UTF-8");
			propertiesConfiguration.load(resource);
			return propertiesConfiguration;
		} catch (final Exception e) {
			throw new TranslationException(e);
		}
	}

}
