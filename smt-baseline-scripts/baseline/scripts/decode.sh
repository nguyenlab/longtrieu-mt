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


test=$1
hyp=$2
config_dir=$3
ref=$4
bleu=$5

decode $test $hyp $config_dir $ref $bleu