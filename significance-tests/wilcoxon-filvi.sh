#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder


decode(){
	test=$1
	hyp=$2
	config=$3
	#echo "decode:"
	#echo "test=$test"
	#echo "hyp=$hyp"
	#echo""
	
	$MOSES_DIR/bin/moses -f $config/moses.ini -i < $test > $hyp
}

bleu(){
	ref=$1
	hyp=$2
	bleu=$3
	#echo "bleu:"
	#echo "bleu=$bleu"
	#echo "ref=$ref"
	#echo "hyp=$hyp"
	#echo "-----------"
	
	> $bleu $MOSES_DIR/scripts/generic/multi-bleu.perl -lc $ref < $hyp
}


decode_bleu_single(){
	src_lang=$1
	trg_lang=$2
	test_dir=$3
	decode_dir=$4
	config=$5
	
	if [[ ! -e $decode_dir ]]; then
		mkdir -p $decode_dir
	fi
	
	for file in $test_dir/*; do
		if [[ "$file" == *.$src_lang ]]
		then
			test_name=${file##*/}
			#name=${test_name##*.}
			name="$(cut -d'.' -f1 <<<"$test_name")"
			
			
			hyp_name=$name".hyp"
			bleu_name=$name".bleu"
			ref_name=$name"."$trg_lang
			
			test_file=$file
			ref_file=$test_dir"/"$ref_name
			hyp_file=$decode_dir"/"$hyp_name
			bleu_file=$decode_dir"/"$bleu_name
			
			decode $test_file $hyp_file $config
			bleu $ref_file $hyp_file $bleu_file
		fi
	done
}

decode_bleu_multi(){
	src_lang=$1
	trg_lang=$2
	setup=$3
	in_dir=$4
	config=$5
	
	setup_dir="decode/"$src_lang-$trg_lang"/"$setup
	
	for sample_size in $in_dir/*; do
		sample_size_name=${sample_size##*/};
		for sample_set in $sample_size/*; do
			sample_set_name=${sample_set##*/}
			decode_dir=$setup_dir"/"$sample_size_name"/"$sample_set_name
			#echo "decode=$decode_dir"
			decode_bleu_single $src_lang $trg_lang $sample_set $decode_dir $config;
		done
	done
}

decode_bleu_multi_fast(){
	src_lang=$1
	trg_lang=$2
	setup=$3
	in_dir=$4
	sample_size_name=$5
	config=$6
	
	setup_dir="decode/wilcoxon/"$src_lang-$trg_lang"/"$setup
	sample_size=$in_dir"/"$sample_size_name
	echo $sample_size
	
	#for sample_set in $sample_size/*; do
		#sample_set_name=${sample_set##*/}
	decode_dir=$setup_dir"/"$sample_size_name
	sample_set=$sample_size
		#echo "decode=$decode_dir"
	decode_bleu_single $src_lang $trg_lang $sample_set $decode_dir $config;
	#done
}


#------------------------------------

#(ms-vi)
SRCLANG=fil
TRGLANG=vi
IN_DIR=$PWD/test-sets/$SRCLANG-$TRGLANG/broad-samples
CONFIGS=$PWD/configs/$SRCLANG-$TRGLANG
# surface VS surface-pos-lemma-(weights)

decode_bleu_multi_fast $SRCLANG $TRGLANG "surface" $IN_DIR 400 $CONFIGS"/surface"
decode_bleu_multi_fast $SRCLANG $TRGLANG "surface-pos-(weights)" $IN_DIR 400 $CONFIGS"/surface-pos-(weights)"

decode_bleu_multi_fast $SRCLANG $TRGLANG "surface" $IN_DIR 200 $CONFIGS"/surface"
decode_bleu_multi_fast $SRCLANG $TRGLANG "surface-pos-(weights)" $IN_DIR 200 $CONFIGS"/surface-pos-(weights)"