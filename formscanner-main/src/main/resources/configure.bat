rem #    Together Workflow Editor
rem #    Copyright (C) 2011 Together Teamsolutions Co., Ltd.
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
cls
rem *********************************************
rem *  Initialize environment variables
rem *********************************************

SET SET_JDKHOME=off

SET JDKHOME=%JAVA_HOME%

if exist configure.properties goto init

rem *********************************************
rem *  Set properties values from user input
rem *********************************************
:start
if %~1.==. goto make
if %~1==-help goto help
if %~1==-jdkhome goto jdkhome
goto error

rem *********************************************
rem *  Set default values
rem *********************************************
:default
SET JDKHOME=%JAVA_HOME%

goto make


rem *********************************************
rem *  Init configure.properties parameters
rem *********************************************
:init
find "jdk.dir" < configure.properties > javadir.txt
for /F "tokens=1,2* delims==" %%i in (javadir.txt) do SET JDKHOME=%%j
del javadir.txt>nul
if "X%JDKHOME%"=="X" goto initjava
goto start

:initjava
JDKHOME=%JAVA_HOME%
goto start

rem *********************************************************
rem *  Edit parameters (configure.properties)
rem *********************************************************
:make
if exist configure.properties del configure.properties
echo jdk.dir=^%JDKHOME%>configure.properties

set OLDCLASSPATH=%CLASSPATH%
set OLDPATH=%PATH%
set CLASSPATH="%CD%\lib\ant.jar"
set CLASSPATH=%CLASSPATH%;"%CD%\lib\ant-launcher.jar"
set CLASSPATH=%CLASSPATH%;"%CD%\lib\xercesImpl.jar"

set PATH="%JDKHOME%\bin"
java -cp %CLASSPATH% org.apache.tools.ant.Main -DSYSTEMROOT=%SYSTEMROOT%

set PATH=%OLDPATH%
set CLASSPATH=%OLDCLASSPATH%
set OLDCLASSPATH=
set OLDPATH=

goto end


rem *********************************************************
rem *  Display ERROR message
rem *********************************************************
:error
echo.
echo Invalid options using with configure.bat
echo.


rem *********************************************************
rem *  Display HELP message
rem *********************************************************
:help
echo.
echo Parameters value for using with configure.bat :
echo.
echo configure       - Make configure.properties file with default 'jdk.dir' value
echo.
echo configure -help - Display this screen
echo.
echo configure [-jdkhome jdk_home_dir]
echo.
echo.
echo Examples :
echo.
echo configure -jdkhome C:/jdk1.6.0_20
echo.
goto end

rem *********************************************************
rem *  Set JDKHOME parameter value
rem *********************************************************
:jdkhome
if %SET_JDKHOME%==on goto error
shift
if "X%~1"=="X" goto error
SET JDKHOME=%~f1
SET SET_JDKHOME=on
shift
if "X%~1"=="X" goto make
goto error


rem *********************************************************
rem *  Reset evironment variables
rem *********************************************************
:end
SET JDKHOME=

SET SET_JDKHOME=
