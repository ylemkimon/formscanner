rem #    Formscanner
rem #
rem #    This program is free software: you can redistribute it and/or modify
rem #    it under the terms of the GNU General Public License as published by
rem #    the Free Software Foundation, either version 3 of the License, or 
rem #    (at your option) any later version.
rem #
rem #    This program is distributed in the hope that it will be useful, 
rem #    but WITHOUT ANY WARRANTY; without even the implied warranty of
rem #    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
rem #    GNU General Public License for more details.
rem # 
rem #    You should have received a copy of the GNU General Public License
rem #    along with this program. If not, see http://www.gnu.org/licenses
rem #-----------------------------------------------------------------------
@echo off
for /f %%j in ("java.exe") do (
    set JAVA_PATH=%%~dp$PATH:j
)

set INSTALL_DIR=%CD%

for /f "delims=" %%f in ('dir /s /b /a-d "%INSTALL_DIR%"\lib\formscanner*') do (
	set FS_EXECUTABLE=%%~f
)

echo "%JAVA_PATH%java" -jar -DFormScanner_HOME="%INSTALL_DIR%" "%FS_EXECUTABLE%" > "%INSTALL_DIR%\bin\run.bat"
