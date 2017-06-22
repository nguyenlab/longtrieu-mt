#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

SRCLANG=en
TRGLANG=vi

CORPUS=data/train.clean
KENLM=0
NGRAM=5
LANGUAGE_MODEL=$PWD/language-model/$TRGLANG/lm.$NGRAM-gram.$TRGLANG.bin



nohup nice $MOSES_DIR/scripts/training/train-model.perl -root-dir train \
-corpus $CORPUS -f $SRCLANG -e $TRGLANG \
-alignment grow-diag-final-and -reordering msd-bidirectional-fe \
-lm $KENLM:$NGRAM:$LANGUAGE_MODEL \
-external-bin-dir $MOSES_DIR/tools/mgizapp -mgiza -mgiza-cpus 20 \
>& translation-model.log &