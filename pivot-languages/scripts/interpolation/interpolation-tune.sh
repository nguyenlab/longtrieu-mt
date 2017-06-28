#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder

interpolation_tune_3(){
	src_lang=$1
	trg_lang=$2
	pvt1_lang=$3
	pvt2_lang=$4
	pvt3_lang=$5
	tm_dir=$6
	
	model_0=translation_models/$src_lang-$trg_lang/train
	model_1=triangulation/$src_lang-$trg_lang/$pvt1_lang
	model_2=triangulation/$src_lang-$trg_lang/$pvt2_lang
	model_3=triangulation/$src_lang-$trg_lang/$pvt3_lang
	
	model_tune=translation_models/tune/$src_lang-$trg_lang/train
	
	
	python $MOSES_DIR/contrib/tmcombine/tmcombine.py combine_given_tuning_set \
	$model_0 $model_1 $model_2 $model_3 \
	-o $tm_dir/phrase-table-tune \
	-r $model_tune/model/phrase-table.gz
	
	
	python $MOSES_DIR/contrib/tmcombine/tmcombine.py combine_reordering_tables \
	$model_0 $model_1 $model_2 $model_3 \
	--number_of_features 6 \
	-o $tm_dir/reordering-table-tune \
	-r $model_tune/model/reordering-table.wbe-msd-bidirectional-fe.gz
	
	
}


generate_configuration_file(){
	tm_dir=$1
	language_model=$2
	
	python scripts/interpolation/generate-moses-ini.py $tm_dir/phrase-table.gz $tm_dir/reordering-table.wbe-msd-bidirectional-fe.gz $language_model $tm_dir
}

# --------------------------------
src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4
pvt3_lang=$5

# --------------------------------
tm_dir=$PWD/interpolation/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang-$pvt3_lang/tune/train/model
mkdir -p $tm_dir
language_model=$PWD/language_model/lm.$trg_lang.bin
# --------------------------------

interpolation_tune_3 $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang $tm_dir
# --------------------------------

sort $tm_dir/phrase-table-tune > $tm_dir/phrase-table
sort $tm_dir/reordering-table-tune > $tm_dir/reordering-table.wbe-msd-bidirectional-fe

# --------------------------------
gzip $tm_dir/phrase-table
gzip $tm_dir/reordering-table.wbe-msd-bidirectional-fe
# --------------------------------
generate_configuration_file $tm_dir $language_model