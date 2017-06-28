#!/bin/sh
# triangulation

export JAVA_HOME=~/jdk1.8.0_40
export PATH=~/jdk1.8.0_40/bin:$PATH

JAVA_DIR=scripts/triangulation/pivot
MOSES_DIR=/work/$USER/moses/mosesdecoder

triangulation(){
	src_lang=$1
	trg_lang=$2
	pvt_lang=$3
	workers=$4
	n_best=$5
	out_dir=$6
	
	src_pvt_table_zip=$PWD/translation_models/$src_lang-$pvt_lang/train/model/phrase-table.gz
	pvt_trg_table_zip=$PWD/translation_models/$pvt_lang-$trg_lang/train/model/phrase-table.gz
	src_pvt_table=$PWD/translation_models/$src_lang-$pvt_lang/train/model/phrase-table
	pvt_trg_table=$PWD/translation_models/$pvt_lang-$trg_lang/train/model/phrase-table
	gunzip -c $src_pvt_table_zip > $src_pvt_table
	gunzip -c $pvt_trg_table_zip > $pvt_trg_table
	src_test=data/test.$src_lang

	java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Pivot triangulation-filter \
	$src_pvt_table $pvt_trg_table $src_test $workers $n_best $out_dir
}

#---------------------------------------

src_lang=$1
trg_lang=$2
pvt_lang=$3
rectangulation_dir=$4

workers=20
n_best=10

out_dir=$rectangulation_dir/triangulation/$src_lang-$trg_lang/$pvt_lang
mkdir -p $out_dir

#---------------------------------------
# triangulation
triangulation $src_lang $trg_lang $pvt_lang $workers $n_best $out_dir

#---------------------------------------
# reordering
./scripts/rectangulation/reordering.sh $src_lang $trg_lang $pvt_lang $out_dir

#---------------------------------------
# genrate tables
./scripts/rectangulation/generate-tables.sh $src_lang $trg_lang $pvt_lang $out_dir