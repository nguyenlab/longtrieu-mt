#!/bin/sh

JAVA_DIR=sentence_alignment

SRCLANG=ja
TRGLANG=vi

SETUP=length-and-word-based

SEARCHING_LIMIT=20
THRESHOLD1=0.99
THRESHOLD2=0.9


INDIR=data/input
LENGTH_BASED_DIR=length-based
IBM1_FILE=ibm1-similarity/ibm1-similarity.model
ALIGNED_DIR=aligned


java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner $SETUP \
	$INDIR $SRCLANG $TRGLANG $SEARCHING_LIMIT $THRESHOLD2 $LENGTH_BASED_DIR \
	$IBM1_FILE $ALIGNED_DIR