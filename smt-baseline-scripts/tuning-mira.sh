#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

SRCLANG=en
TRGLANG=vi

TUNE_SRC=data/tune.clean.$SRCLANG
TUNE_TRG=data/tune.clean.$TRGLANG

 
$MOSES_DIR/scripts/training/mert-moses.pl $TUNE_SRC $TUNE_TRG \
	$MOSES_DIR/bin/moses train/model/moses.ini \
	--mertdir $MOSES_DIR/bin \
	--rootdir $MOSES_DIR/scripts --batch-mira --return-best-dev \
	--batch-mira-args '-J 60' --decoder-flags '-threads 20 -v 0' \
	>& tuning-mira.out &