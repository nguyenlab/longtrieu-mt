#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

bleu_ratio(){
	src_lang=$1
	trg_lang=$2
	pvt1_lang=$3
	pvt2_lang=$4
	pvt3_lang=$5
	out_dir=$6

	file_1=tuning/triangulation/$src_lang-$trg_lang/$pvt1_lang/mert-work/moses.ini
	file_2=tuning/triangulation/$src_lang-$trg_lang/$pvt2_lang/mert-work/moses.ini
	file_3=tuning/triangulation/$src_lang-$trg_lang/$pvt3_lang/mert-work/moses.ini

	out_file=$out_dir/ratios.bleu
	python scripts/multi-pivot/bleu-ratios.py $file_1 $file_2 $file_3 $out_file
}

multi_pivot_weights_3(){
	src_lang=$1
	trg_lang=$2
	pvt1_lang=$3
	pvt2_lang=$4
	pvt3_lang=$5
	tm_dir=$6
	
	out_file=$tm_dir/ratios.bleu
	
	ratio_1=1
	ratio_2=1
	ratio_3=1
	
	if [[ -f "$out_file" ]]
	then
		while IFS= read -r line
		do
			#echo "$line"
			ratio_1="$(cut -d' ' -f1 <<<"$line")"
			ratio_2="$(cut -d' ' -f2 <<<"$line")"
			ratio_3="$(cut -d' ' -f3 <<<"$line")"
		done < "$out_file"
	fi
	
	#echo "$ratio_1"
	#echo "$ratio_2"
	#echo "$ratio_3"
	
	model_1=triangulation/$src_lang-$trg_lang/$pvt1_lang
	model_2=triangulation/$src_lang-$trg_lang/$pvt2_lang
	model_3=triangulation/$src_lang-$trg_lang/$pvt3_lang
	
	python $MOSES_DIR/contrib/tmcombine/tmcombine.py combine_given_weights \
	$model_1 $model_2 $model_3 \
	-w "$ratio_1, $ratio_2, $ratio_3" \
	-o $tm_dir/phrase-table-weights
	
	python $MOSES_DIR/contrib/tmcombine/tmcombine.py combine_reordering_tables \
	$model_1 $model_2 $model_3 \
	--weights "$ratio_1, $ratio_2, $ratio_3" \
	--number_of_features 6 \
	-o $tm_dir/reordering-table-weights
}

generate_configuration_file(){
	tm_dir=$1
	language_model=$2
	
	python scripts/interpolation/generate-moses-ini.py $tm_dir/phrase-table.gz $tm_dir/reordering-table.wbe-msd-bidirectional-fe.gz $language_model $tm_dir
}

src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4
pvt3_lang=$5
tm_dir=$PWD/multi-pivot/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang-$pvt3_lang/weights/train/model
mkdir -p $tm_dir
language_model=$PWD/language_model/lm.$trg_lang.bin

#-------------------------------------
# bleu ratio
bleu_ratio $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang $tm_dir

#----------------------------------
multi_pivot_weights_3 $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang $tm_dir

#----------------------------------
sort $tm_dir/phrase-table-weights > $tm_dir/phrase-table
sort $tm_dir/reordering-table-weights > $tm_dir/reordering-table.wbe-msd-bidirectional-fe

# --------------------------------
gzip $tm_dir/phrase-table
gzip $tm_dir/reordering-table.wbe-msd-bidirectional-fe
# --------------------------------
generate_configuration_file $tm_dir $language_model