#!/bin/sh

JVNTEXTPRO_DIR=~/git/JVnTextPro-v.2.0


tokenize(){
	
	input_file=$1
	
	java -mx64G -cp $JVNTEXTPRO_DIR/target/classes:$JVNTEXTPRO_DIR/target/libs/args4j.jar:libs/lbfgs.jar jvntokenizer.JVnTokenizer -inputfile $input_file
}

input_file=$1

tokenize $input_file