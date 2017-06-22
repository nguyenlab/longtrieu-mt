#!/bin/sh

# this sample script preprocesses a sample corpus, including tokenization,
# truecasing, and subword segmentation. 
# for application to a different language pair,
# change source and target prefix, optionally the number of BPE operations,
# and the file names (currently, data/corpus and data/newsdev2016 are being processed)

# in the tokenization step, you will want to remove Romanian-specific normalization / diacritic removal,
# and you may want to add your own.
# also, you may want to learn BPE segmentations separately for each language,
# especially if they differ in their alphabet

# suffix of source language files
SRC=en

# suffix of target language files
TRG=vi

TRAIN_FILE=train.tags.en-vi.in-domain.truecase
DEV_FILE=IWSLT15.TED.tst2012.en-vi.truecase
TEST_FILE1=IWSLT15.TED.tst2013.en-vi.truecase
TEST_FILE2=IWSLT15.TED.tst2015.en-vi.truecase

# number of merge operations. Network vocabulary should be slightly larger (to include characters),
# or smaller if the operations are learned on the joint vocabulary
bpe_operations=89500

# path to moses decoder: https://github.com/moses-smt/mosesdecoder
#mosesdecoder=/path/to/mosesdecoder
mosesdecoder=~/git/mosesdecoder

# path to subword segmentation scripts: https://github.com/rsennrich/subword-nmt
subword_nmt=~/git/subword-nmt 

# path to nematus ( https://www.github.com/rsennrich/nematus )
nematus=~/git/nematus



# train BPE
cat data/$TRAIN_FILE.$SRC data/$TRAIN_FILE.$TRG | python $subword_nmt/learn_bpe.py -s $bpe_operations > model/$SRC$TRG.bpe

# apply BPE
for prefix in $TRAIN_FILE $DEV_FILE $TEST_FILE1 $TEST_FILE2

 do
  python $subword_nmt/apply_bpe.py -c model/$SRC$TRG.bpe < data/$prefix.$SRC > data/$prefix.bpe.$SRC
  python $subword_nmt/apply_bpe.py -c model/$SRC$TRG.bpe < data/$prefix.$TRG > data/$prefix.bpe.$TRG
 done

# build network dictionary
python $nematus/data/build_dictionary.py data/$TRAIN_FILE.bpe.$SRC data/$TRAIN_FILE.bpe.$TRG
