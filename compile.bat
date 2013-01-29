echo off
echo Compiling
"c:\program files\java\jdk1.7.0_11\bin\javac.exe" excel\beans\feed\*.java
"c:\program files\java\jdk1.7.0_11\bin\javac.exe" excel\beans\store\*.java
"c:\program files\java\jdk1.7.0_11\bin\javac.exe" -classpath jxl.jar;.; excel\UpdateStore.java
pause
echo Complete