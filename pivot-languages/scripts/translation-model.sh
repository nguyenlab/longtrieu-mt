#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

translation_model(){
	src_lang=$1
	trg_lang=$2
	corpus=$3
	language_model=$4
	out_dir=$5
	train_dir=$out_dir/$src_lang-$trg_lang/train
	mkdir -p $train_dir

	nohup nice $MOSES_DIR/scripts/training/train-model.perl -root-dir $train_dir \
	-corpus $corpus -f $src_lang -e $trg_lang \
	-alignment grow-diag-final-and -reordering msd-bidirectional-fe \
	-lm 0:5:$language_model \
	-external-bin-dir $MOSES_DIR/tools/mgizapp -mgiza -mgiza-cpus 20 \
	>& $train_dir/translation-model.$src_lang-$trg_lang.log &
}

src_lang=$1
trg_lang=$2
corpus=data/train
language_model=$PWD/language_model/lm.$trg_lang.bin
out_dir=translation_models

translation_model $src_lang $trg_lang $corpus $language_model $out_dir