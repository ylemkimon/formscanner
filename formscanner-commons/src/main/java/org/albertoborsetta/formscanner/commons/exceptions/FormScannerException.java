package org.albertoborsetta.formscanner.commons.exceptions;

public class FormScannerException extends Exception {

	private static final long serialVersionUID = 1L;

	public FormScannerException() {
		super();
	}

	public FormScannerException(final String message) {
		super(message);
	}

	public FormScannerException(final Throwable cause) {
		super(cause);
	}

	public FormScannerException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
