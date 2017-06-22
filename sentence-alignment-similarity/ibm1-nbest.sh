#!/bin/sh

JAVA_DIR=sentence_alignment

SETUP=nbest-ibm1

SRCLANG=ja
TRGLANG=vi


IBM1_DIR=ibm1
IBM1_FILE=$IBM1_DIR/ibm1.model

NBEST=5
THRESHOLD=0.1
OUT_DIR=ibm1-similarity
OUTFILE=ibm1.$NBEST-best.model
SRCFILE=words.$SRCLANG
TRGFILE=words.$TRGLANG

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner $SETUP \
	$IBM1_FILE $NBEST $THRESHOLD $OUT_DIR $OUTFILE $SRCFILE $TRGFILE