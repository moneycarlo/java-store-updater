@echo off
echo Cleaning
del excel\beans\feed\*.class
del excel\beans\store\*.class
del excel\*.class
echo Cleaning Complete
echo.
echo Compiling
"c:\program files\java\jdk1.7.0_11\bin\javac.exe" excel\beans\feed\*.java
"c:\program files\java\jdk1.7.0_11\bin\javac.exe" excel\beans\store\*.java
"c:\program files\java\jdk1.7.0_11\bin\javac.exe" -classpath jxl.jar;.; excel\UpdateStore.java
echo.
echo Compile Complete
echo.
echo Select 1 for New Products.
echo Select 2 for Updating Quantities.
choice /c 12
if errorlevel 2 goto updating
if errorlevel 1 goto newprods
:updating
echo Updating Quantities
java -Xms1024m -Xmx2048m -classpath jxl.jar;.; excel.UpdateStore updateqty
echo Complete
goto exit
:newprods
echo Adding New Products
java -Xms1024m -Xmx2048m -classpath jxl.jar;.; excel.UpdateStore
echo Complete
goto exit
:exit
pause
exit
