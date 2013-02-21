package org.albertoborsetta.formscanner.commons.exceptions;

public class FormScannerRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FormScannerRuntimeException() {
		super();
	}

	public FormScannerRuntimeException(final String message) {
		super(message);
	}

	public FormScannerRuntimeException(final Throwable cause) {
		super(cause);
	}

	public FormScannerRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
