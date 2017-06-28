#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

decode(){
	test=$1
	hyp=$2
	config_dir=$3
	ref=$4
	bleu=$5

	$MOSES_DIR/bin/moses -f $config_dir/moses.ini -i < $test > $hyp
	> $bleu $MOSES_DIR/scripts/generic/multi-bleu.perl -lc $ref < $hyp
}


src_lang=$1
trg_lang=$2
test=data/test.$src_lang
ref=data/test.$trg_lang
config_dir=tuning/$src_lang-$trg_lang/mert-work
decode_dir=decode/$src_lang-$trg_lang
mkdir -p $decode_dir
hyp=$decode_dir/$src_lang-$trg_lang.hyp
bleu=$decode_dir/$src_lang-$trg_lang.bleu

decode $test $hyp $config_dir $ref $bleu