@echo off
rem start javaw -cp bin;ojdbc14.jar tools.db.ui.Mainc
java -cp bin;json-taglib-0.4.1.jar Mainc

if errorlevel 1 pause
