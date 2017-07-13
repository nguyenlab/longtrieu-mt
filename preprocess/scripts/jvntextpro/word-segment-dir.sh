#!/bin/sh

JVNTEXTPRO_DIR=~/git/path
cd $PBS_O_WORKDIR
export JAVA_HOME=~/jdk1.8.0_40
export PATH=~/jdk1.8.0_40/bin:$PATH


word_segment(){
	
	input_dir=$1
	
	java -mx64G -cp $JVNTEXTPRO_DIR/target/classes:$JVNTEXTPRO_DIR/target/libs/args4j.jar:libs/lbfgs.jar jvnsegmenter.WordSegmenting -modeldir $JVNTEXTPRO_DIR/models/jvnsegmenter -inputdir $input_dir
}

input_dir=$1

word_segment $input_dir