#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

SRCLANG=ms
TRGLANG=vi

mkdir -p decode

TEST=data/test.clean.$SRCLANG
REF=data/test.clean.$TRGLANG
HYP=decode/test.clean.$SRCLANG.hyp
BLEU=decode/test.bleu

IN_DIR=$PWD/output/$SRCLANG-$TRGLANG/random-sets/100/50

decode(){
	test=$1
	hyp=$2
	echo "decode:"
	echo "test=$test"
	echo "hyp=$hyp"
	echo""
	
	$MOSES_DIR/bin/moses -f moses.ini -i < $test > $hyp
}

bleu(){
	ref=$1
	hyp=$2
	bleu=$3
	echo "bleu:"
	echo "bleu=$bleu"
	echo "ref=$ref"
	echo "hyp=$hyp"
	echo "-----------"
	
	> $bleu $MOSES_DIR/scripts/generic/multi-bleu.perl -lc $ref < $hyp
}

decode_bleu(){
	dir=$1
	src_lang=$2
	trg_lang=$3
	
	for file in $dir/*; do
		if [[ "$file" == *.$src_lang ]]
		then
			test_name=${file##*/}
			#name=${test_name##*.}
			name="$(cut -d'.' -f1 <<<"$test_name")"
			
			
			hyp_name=$name".hyp"
			bleu_name=$name".bleu"
			ref_name=$name"."$trg_lang
			
			test_file=$file
			hyp_file=$dir"/"$hyp_name
			ref_file=$dir"/"$ref_name
			bleu_file=$dir"/"$bleu_name
			
			decode $test_file $hyp_file
			bleu $ref_file $hyp_file $bleu_file
		fi
	done
}

decode_bleu $IN_DIR $SRCLANG $TRGLANG
