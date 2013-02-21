package org.albertoborsetta.formscanner.commons.configuration;

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

public class ConfigurationClassLoader {

	public static final String CONFIGURATION_PATH_PROPERTY = "org.cmdbuild.connector.configuration.path";

	private final URLClassLoader urlClassLoader;

	private ConfigurationClassLoader(final URL url) {
		urlClassLoader = new URLClassLoader(new URL[] { url }, null);
	}

	public String getResource(final String name) {
		Validate.notEmpty(name);
		final URL resourceURL = urlClassLoader.getResource(name);
		if (resourceURL == null) {
			logger.warn("resource '{}' not found", name);
		}
		return (resourceURL == null) ? null : resourceURL.getFile();
	}

	public static ConfigurationClassLoader getInstance() throws ConfigurationException {
		final String configurationPath = getConfigurationPath();
		try {
			final URL url = new URL("file://" + configurationPath);
			return new ConfigurationClassLoader(url);
		} catch (final MalformedURLException e) {
			throw new ConfigurationException(e);
		}
	}

	private static String getConfigurationPath() throws ConfigurationException {
		final SystemConfiguration systemConfiguration = new SystemConfiguration();
		final String property = systemConfiguration.getString(CONFIGURATION_PATH_PROPERTY);
		final String configurationPath = parseConfigurationPath(property);
		if (isBlank(configurationPath)) {
			final String message = format("missing property '%s'", CONFIGURATION_PATH_PROPERTY);
			throw new ConfigurationException(message);
		}
		final File file = new File(configurationPath);
		if (!file.isDirectory()) {
			final String message = format("invalid path '%s'", configurationPath);
			throw new ConfigurationException(message);
		}
		return configurationPath;
	}

	private static String parseConfigurationPath(final String property) throws ConfigurationException {
		final String configurationPath;
		if (isNotBlank(property)) {
			configurationPath = property + (property.endsWith(File.separator) ? EMPTY : File.separator);
		} else {
			configurationPath = property;
		}
		return configurationPath;
	}

}
