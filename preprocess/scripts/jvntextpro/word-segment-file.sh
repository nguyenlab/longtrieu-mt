#!/bin/sh

JVNTEXTPRO_DIR=~/git/path


word_segment(){
	
	input_file=$1
	
	java -mx64G -cp $JVNTEXTPRO_DIR/target/classes:$JVNTEXTPRO_DIR/target/libs/args4j.jar:libs/lbfgs.jar jvnsegmenter.WordSegmenting -modeldir $JVNTEXTPRO_DIR/models/jvnsegmenter -inputfile $input_file
}

input_file=$1

word_segment $input_file