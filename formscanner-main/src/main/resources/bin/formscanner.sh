#!/bin/sh

run()
{
	DIRNAME=`dirname $0`

	ROOT_DIR="${DIRNAME}/.."
	BIN_DIR="${ROOT_DIR}/bin"
	LIB_DIR="${ROOT_DIR}/lib"
	CONFIG_DIR="${ROOT_DIR}/config"
	LANGUAGE_DIR="${ROOT_DIR}/language"
	TEMPLATE_DIR="${ROOT_DIR}/template"
	ICONS_DIR="${ROOT_DIR}/icons"

	java -jar \
		-cp "${LIB_DIR}/*" \
		-Dorg.albertoborsetta.formscanner.configuration.path="${CONFIG_DIR}" \
		-Dorg.albertoborsetta.formscanner.translation.path="${LANGUAGE_DIR}" \
		...
}

run
