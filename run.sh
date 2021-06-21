#!/bin/sh
cd src/lang/parser/
java -cp javacc.jar jjtree SMPL.jjt
cd ast/ # subdir
java -cp ../javacc.jar javacc SMPL.jj
cd ../../../.. # main dir
javac -d bin/ $(find . -name "**.java")
java -cp bin SMPL $@
