#!/bin/sh

MODEL_FILE=w2v-model/ja-w2v.model
IBM_FILE=ibm1-similarity/words.ja
OUT_DIR=ibm1-similarity
SIMILAR_FILE=most-similar.ja


python most-similar.py $MODEL_FILE \
	$IBM_FILE \
	$OUT_DIR $SIMILAR_FILE

MODEL_FILE=w2v-model/vi-w2v.model
IBM_FILE=ibm1-similarity/words.vi
OUT_DIR=ibm1-similarity
SIMILAR_FILE=most-similar.vi


python most-similar.py $MODEL_FILE \
	$IBM_FILE \
	$OUT_DIR $SIMILAR_FILE