#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

SRCLANG=en
TRGLANG=vi

TRAIN_SRC=data/train.$SRCLANG
TRAIN_TRG=data/train.$TRGLANG
TUNE_SRC=data/tune.$SRCLANG
TUNE_TRG=data/tune.$TRGLANG
TEST_SRC=data/test.$SRCLANG
TEST_TRG=data/test.$TRGLANG

# --------------------------------------------
# tokenize, truecase, clean
# training files
$MOSES_DIR/scripts/tokenizer/tokenizer.perl -l $SRCLANG < $TRAIN_SRC > data/train.tok.$SRCLANG 
$MOSES_DIR/scripts/recaser/train-truecaser.perl --model data/train.recaser.$SRCLANG --corpus data/train.tok.$SRCLANG 
$MOSES_DIR/scripts/recaser/truecase.perl --model data/train.recaser.$SRCLANG < data/train.tok.$SRCLANG > data/train.true.$SRCLANG

$MOSES_DIR/scripts/tokenizer/tokenizer.perl -l $TRGLANG < $TRAIN_TRG > data/train.tok.$TRGLANG 
$MOSES_DIR/scripts/recaser/train-truecaser.perl --model data/train.recaser.$TRGLANG --corpus data/train.tok.$TRGLANG 
$MOSES_DIR/scripts/recaser/truecase.perl --model data/train.recaser.$TRGLANG < data/train.tok.$TRGLANG > data/train.true.$TRGLANG 


# dev files

$MOSES_DIR/scripts/tokenizer/tokenizer.perl -l $SRCLANG < $TUNE_SRC > data/tune.tok.$SRCLANG 
$MOSES_DIR/scripts/recaser/train-truecaser.perl --model data/tune.recaser.$SRCLANG --corpus data/tune.tok.$SRCLANG 
$MOSES_DIR/scripts/recaser/truecase.perl --model data/tune.recaser.$SRCLANG < data/tune.tok.$SRCLANG > data/tune.true.$SRCLANG 

$MOSES_DIR/scripts/tokenizer/tokenizer.perl -l $TRGLANG < $TUNE_TRG > data/tune.tok.$TRGLANG 
$MOSES_DIR/scripts/recaser/train-truecaser.perl --model data/tune.recaser.$TRGLANG --corpus data/tune.tok.$TRGLANG 
$MOSES_DIR/scripts/recaser/truecase.perl --model data/tune.recaser.$TRGLANG < data/tune.tok.$TRGLANG > data/tune.true.$TRGLANG 

# test files
$MOSES_DIR/scripts/tokenizer/tokenizer.perl -l $SRCLANG < $TEST_SRC > data/test.tok.$SRCLANG 
$MOSES_DIR/scripts/recaser/train-truecaser.perl --model data/test.recaser.$SRCLANG --corpus data/test.tok.$SRCLANG 
$MOSES_DIR/scripts/recaser/truecase.perl --model data/test.recaser.$SRCLANG < data/test.tok.$SRCLANG > data/test.true.$SRCLANG 

$MOSES_DIR/scripts/tokenizer/tokenizer.perl -l $TRGLANG < $TEST_TRG > data/test.tok.$TRGLANG 
$MOSES_DIR/scripts/recaser/train-truecaser.perl --model data/test.recaser.$TRGLANG --corpus data/test.tok.$TRGLANG 
$MOSES_DIR/scripts/recaser/truecase.perl --model data/test.recaser.$TRGLANG < data/test.tok.$TRGLANG > data/test.true.$TRGLANG 


# clean

$MOSES_DIR/scripts/training/clean-corpus-n.perl data/train.true $SRCLANG $TRGLANG data/train.clean 1 80 >& preprocess-train.out
$MOSES_DIR/scripts/training/clean-corpus-n.perl data/tune.true $SRCLANG $TRGLANG data/tune.clean 1 80 >& preprocess-tune.out
$MOSES_DIR/scripts/training/clean-corpus-n.perl data/test.true $SRCLANG $TRGLANG data/test.clean 1 80 >& preprocess-test.out