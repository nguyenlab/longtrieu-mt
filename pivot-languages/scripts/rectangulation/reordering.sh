#!/bin/sh
# triangulation

MOSES_DIR=/work/$USER/moses/mosesdecoder

extract_phrases(){
	src_lang=$1
	trg_lang=$2
	out_dir=$3
	
	$MOSES_DIR/scripts/training/train-model.perl -root-dir $out_dir/reordering/train \
	-first-step 5 -last-step 5 \
	-corpus $out_dir/reordering/train/model/phrases \
	-f $src_lang -e $trg_lang \
	-alignment grow-diag-final-and \
	-reordering msd-bidirectional-fe \
	>& $out_dir/reordering/extract-phrases.log
}

train_reordering(){
	src_lang=$1
	trg_lang=$2
	out_dir=$3
	
	$MOSES_DIR/scripts/training/train-model.perl -root-dir $out_dir/reordering/train \
	-first-step 7 -last-step 7 \
	-f $src_lang -e $trg_lang \
	-reordering msd-bidirectional-fe \
	>& $out_dir/reordering/train-reordering.log
}

#---------------------------------------

src_lang=$1
trg_lang=$2
pvt_lang=$3
out_dir=$4


#---------------------------------------
# extract alignment
python scripts/triangulation/extract-alignment.py $out_dir/reordering $out_dir/pivot/phrase-table.model $src_lang $trg_lang

#---------------------------------------
# extract phrases
 extract_phrases $src_lang $trg_lang $out_dir

#---------------------------------------
# train reordering
train_reordering $src_lang $trg_lang $out_dir
