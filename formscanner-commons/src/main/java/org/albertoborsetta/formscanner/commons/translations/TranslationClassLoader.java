package org.albertoborsetta.formscanner.commons.translations;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.lang.Validate;

public class TranslationClassLoader {

	public static final String CONFIGURATION_PATH_PROPERTY = "org.albertoborsetta.formscanner.translation.path";

	private final URLClassLoader urlClassLoader;

	private TranslationClassLoader(final URL url) {
		urlClassLoader = new URLClassLoader(new URL[] { url }, null);
	}

	public String getResource(final String name) {
		Validate.notEmpty(name);
		final URL resourceURL = urlClassLoader.getResource(name);
		if (resourceURL == null) {
			System.out.println("resource " + name + " not found");
		}
		return (resourceURL == null) ? null : resourceURL.getFile();
	}

	public static TranslationClassLoader getInstance() throws TranslationException {
		final String configurationPath = getConfigurationPath();
		try {
			final URL url = new URL("file://" + configurationPath);
			return new TranslationClassLoader(url);
		} catch (final MalformedURLException e) {
			throw new TranslationException(e);
		}
	}

	private static String getConfigurationPath() throws TranslationException {
		final SystemConfiguration systemConfiguration = new SystemConfiguration();
		final String property = systemConfiguration.getString(CONFIGURATION_PATH_PROPERTY);
		final String configurationPath = parseConfigurationPath(property);
		if (isBlank(configurationPath)) {
			final String message = format("missing property '%s'", CONFIGURATION_PATH_PROPERTY);
			throw new TranslationException(message);
		}
		final File file = new File(configurationPath);
		if (!file.isDirectory()) {
			final String message = format("invalid path '%s'", configurationPath);
			throw new TranslationException(message);
		}
		return configurationPath;
	}

	private static String parseConfigurationPath(final String property) throws TranslationException {
		final String configurationPath;
		if (isNotBlank(property)) {
			configurationPath = property + (property.endsWith(File.separator) ? EMPTY : File.separator);
		} else {
			configurationPath = property;
		}
		return configurationPath;
	}

}
