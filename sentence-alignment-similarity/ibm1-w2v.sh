#!/bin/sh

JAVA_DIR=sentence_alignment

SETUP=enrich-ibm1-w2v

IBM1_FILE=ibm1/ibm1.model
IBM1_NBEST_FILE=ibm1-similarity/ibm1.5-best.model
SRC_SIM=ibm1-similarity/word-similarity.w2v.para.ja
TRG_SIM=ibm1-similarity/word-similarity.w2v.para.vi
OUT_DIR=ibm1-similarity
IBM1_W2V=ibm1-similarity.model


java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner $SETUP \
	$IBM1_FILE $IBM1_NBEST_FILE $SRC_SIM $TRG_SIM \
	$OUT_DIR $IBM1_W2V