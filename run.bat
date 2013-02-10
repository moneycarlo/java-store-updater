echo off
echo Running
java -Xms1024m -Xmx2048m -classpath jxl.jar;.; excel.UpdateStore
echo Complete
pause