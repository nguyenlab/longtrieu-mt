#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

language_model(){
	lang=$1
	corpus=$2
	out_dir=$3
	out_file=$out_dir/lm.$lang
	out_file_bin=$out_dir/lm.$lang.bin
	
	$MOSES_DIR/bin/lmplz -o 5 -S 80% -T /tmp < $corpus > $out_file
	$MOSES_DIR/bin/build_binary $out_file $out_file_bin
}


mkdir -p language_model
language_model en data/train-lm.en language_model
language_model vi data/train-lm.en language_model
language_model fil data/train-lm.en language_model
language_model id data/train-lm.en language_model
language_model ms data/train-lm.en language_model
