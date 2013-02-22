package org.albertoborsetta.formscanner.commons.translations;

import org.albertoborsetta.formscanner.commons.exceptions.FormScannerException;

public class TranslationException extends FormScannerException {

	private static final long serialVersionUID = 1L;

	public TranslationException() {
		super();
	}

	public TranslationException(final String message) {
		super(message);
	}

	public TranslationException(final Throwable cause) {
		super(cause);
	}

	public TranslationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
