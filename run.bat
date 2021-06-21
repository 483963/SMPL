cd src\lang\parser\
java -cp javacc.jar jjtree SMPL.jjt 
cd ast\
java -cp ..\javacc.jar javacc SMPL.jj
cd ..\..\..\..
dir /s /B src\*.java > java_files.txt
javac -d bin\ @java_files.txt
del java_files.txt
java -cp bin SMPL %*
