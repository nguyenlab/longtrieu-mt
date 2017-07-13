#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

SRCLANG=$1
CORPUS=$2

$MOSES_DIR/scripts/tokenizer/tokenizer.perl -l $SRCLANG < $CORPUS > $CORPUS.tok
$MOSES_DIR/scripts/recaser/train-truecaser.perl --model $CORPUS.recaser --corpus $CORPUS.tok 
$MOSES_DIR/scripts/recaser/truecase.perl --model $CORPUS.recaser < $CORPUS.tok > $CORPUS.tc