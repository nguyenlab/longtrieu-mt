#!/bin/sh

SRC_LANG=ms
TRG_LANG=vi
SRC_FILE=data/ms-vi/test.clean.ms
TRG_FILE=data/ms-vi/test.clean.vi

OUT_DIR=output/$SRC_LANG-$TRG_LANG
mkdir -p $OUT_DIR
#-------------------------------------------
# broad samples
python scripts/broad-samples.py $SRC_LANG $TRG_LANG $SRC_FILE $TRG_FILE $OUT_DIR
#-------------------------------------------
# generate random sets
python scripts/draw-random-sets.py $SRC_LANG $TRG_LANG $OUT_DIR

