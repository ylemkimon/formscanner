package org.albertoborsetta.formscanner.commons.configuration;

import org.albertoborsetta.formscanner.commons.exceptions.FormScannerException;

public class ConfigurationException extends FormScannerException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(final String message) {
		super(message);
	}

	public ConfigurationException(final Throwable cause) {
		super(cause);
	}

	public ConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
