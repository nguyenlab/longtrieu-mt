#!/bin/sh

JAVA_DIR=sentence_alignment


SETUP=ibm1

IBM1_DIR=ibm1

cat data/bilingual/ja-vi.ja length-based.ja > ibm1/data.ja
cat data/bilingual/ja-vi.vi length-based.vi > ibm1/data.vi

LOOP=5
IBM1_FILE=ibm1.model

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner $SETUP \
	ibm1/data.ja ibm1/data.vi $LOOP $IBM1_DIR $IBM1_FILE