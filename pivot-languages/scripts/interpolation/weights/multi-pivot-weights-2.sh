#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

bleu_ratio(){
	bleu_1=$1
	bleu_2=$2
	tm_dir=$3
	
	mkdir -p $tm_dir
	
	out_file=$tm_dir/ratios.bleu
	
	python scripts/interpolation/weights/bleu-ratios-2.py $bleu_1 $bleu_2 $out_file
}

multi_pivot_weights_2(){
	model_1=$1
	model_2=$2
	tm_dir=$3
	out_file=$tm_dir/ratios.bleu
	
	ratio_1=1
	ratio_2=1
	
	if [[ -f "$out_file" ]]
	then
		while IFS= read -r line
		do
			#echo "$line"
			ratio_1="$(cut -d' ' -f1 <<<"$line")"
			ratio_2="$(cut -d' ' -f2 <<<"$line")"
		done < "$out_file"
	fi
	
	python $MOSES_DIR/contrib/tmcombine/tmcombine.py combine_given_weights \
	$model_1 $model_2 \
	-w "$ratio_1, $ratio_2" \
	-o $tm_dir/phrase-table-weights
	
	python $MOSES_DIR/contrib/tmcombine/tmcombine.py combine_reordering_tables \
	$model_1 $model_2 \
	--weights "$ratio_1, $ratio_2" \
	--number_of_features 6 \
	-o $tm_dir/reordering-table-weights
}

generate_configuration_file(){
	tm_dir=$1
	language_model=$2
	
	python scripts/interpolation/generate-moses-ini.py $tm_dir/phrase-table.gz $tm_dir/reordering-table.wbe-msd-bidirectional-fe.gz $language_model $tm_dir
}

bleu_1=$1
bleu_2=$2
model_1=$3
model_2=$4
language_model=$5
tm_dir=$6

bleu_ratio $bleu_1 $bleu_2 $tm_dir
multi_pivot_weights_2 $model_1 $model_2 $tm_dir
sort $tm_dir/phrase-table-weights > $tm_dir/phrase-table
sort $tm_dir/reordering-table-weights > $tm_dir/reordering-table.wbe-msd-bidirectional-fe
gzip $tm_dir/phrase-table
gzip $tm_dir/reordering-table.wbe-msd-bidirectional-fe
generate_configuration_file $tm_dir $language_model