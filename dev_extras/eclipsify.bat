@echo off
echo this needs to be updated, due to folders changed, no effect now
echo TODO: remove .project too and have this file generate it, or rather copy it from this folder into ..\
echo all this due to the constrain that root folder is desired to be clean(er)
exit
rem since windows won't allow you to rename a file from ie. ".classpath.something" to 
rem ".classpath" because it would ask for a name (.classpath being the extension)
rem this small script will do the copying with new names of the right eclipse files
rem so that you can have them right before doing the Import Project in Eclipse

rem the folder this script was executed in, let's hope it's still in the root of the project
rem in the same place where the .settings and .classpath should be
set folder=%~dp0

set filedest=.classpath
set file=.classpath.eclipse

set settingsdirdest=.settings
set settingsdir=.settings.eclipse

rem it's ok if two backslashes are in a path, it will not affect ie. c:\folder\\file.txt

if not exist "%filedest%" (
	if exist "%folder%\%file%" (
		copy /b "%file%" "%filedest%"
		if errorlevel 1 (
			echo failed to copy "%file%" to "%filedest%"
			goto :epicfail
		) else (
			echo copied "%filedest%"
		)
	) else (
		echo "%file%" doesn't exist, this shouldn't have happened
		goto :epicfail
	)
) else (
	echo nothing changed for "%filedest%" already existed
)

if not exist "%settingsdirdest%" (
	if exist "%folder%\%settingsdir%" (
		mkdir "%settingsdirdest%"
		if errorlevel 1 (
			echo failed to create folder "%settingsdirdest%"
			goto :epicfail
		) else (
			echo created folder "%settingsdirdest%"
		)
		copy /b "%settingsdir%\*.*" "%settingsdirdest%\"
		if errorlevel 1 (
			echo failed to copy files from folder "%settingsdir%" into folder "%settingsdirdest%"
			goto :epicfail
		) else (
			echo copied files into "%settingsdirdest%"
		)
	) else (
		echo "%settingsdirdest%" folder doesn't exist, this shouldn't have happened
		goto :epicfail
	)
) else (
	echo nothing changed for "%settingsdirdest%" already existed
)


rem reaching this means that we do have both .classpath and .settings folders
rem either they already existed or they were just copied from the *.eclipse  ones
echo All done here.
pause
exit 0

:epicfail
exit 1


