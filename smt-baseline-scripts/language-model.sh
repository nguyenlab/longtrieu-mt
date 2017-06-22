#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

SRCLANG=en
TRGLANG=vi

CORPUS=data/train.true.$TRGLANG

NGRAM=5
LM_DIR=language-model/$TRGLANG
mkdir -p $LM_DIR
LM_FILE=$LM_DIR/lm.$NGRAM-gram.$TRGLANG
LM_FILE_BIN=$LM_DIR/lm.$NGRAM-gram.$TRGLANG.bin

$MOSES_DIR/bin/lmplz -o $NGRAM -S 80% -T /tmp < $CORPUS > $LM_FILE

$MOSES_DIR/bin/build_binary $LM_FILE $LM_FILE_BIN