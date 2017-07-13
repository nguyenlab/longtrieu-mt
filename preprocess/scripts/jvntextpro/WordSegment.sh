#!/bin/sh

JVnTextPro_Dir=~/git/JVnTextPro-v.2.0

cd $PBS_O_WORKDIR
export JAVA_HOME=~/jdk1.8.0_40
export PATH=~/jdk1.8.0_40/bin:$PATH


word_segmenter(){
	setup=$1
	in_file=$2
	
	java -mx64G -cp $JVnTextPro_Dir/target/classes:$JVnTextPro_Dir/target/libs/args4j.jar:libs/lbfgs.jar jvnsegmenter.WordSegmenting -modeldir $JVnTextPro_Dir/models/jvnsegmenter -$setup $in_file

}

# ---------------------------------------------
# Vietnamese word segmentation
setup=$1
in_file=$2
word_segmenter $setup $in_file
