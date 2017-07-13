#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

CORPUS=$1
SRCLANG=$2
TRGLANG=$3
CLEANED=$4
MIN=$5
MAX=$6

$MOSES_DIR/scripts/training/clean-corpus-n.perl $CORPUS $SRCLANG $TRGLANG $CLEANED $MIN $MAX