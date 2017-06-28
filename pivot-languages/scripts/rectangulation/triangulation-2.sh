#!/bin/sh
# triangulation

export JAVA_HOME=~/jdk1.8.0_40
export PATH=~/jdk1.8.0_40/bin:$PATH

JAVA_DIR=scripts/triangulation/pivot
MOSES_DIR=/work/$USER/moses/mosesdecoder

triangulation(){
	src_lang=$1
	trg_lang=$2
	pvt2_lang=$3
	pvt1_lang=$4
	workers=$5
	n_best=$6
	out_dir=$7
	
	src_pvt_table_zip=$PWD/rectangulation/$src_lang-$trg_lang/triangulation/$src_lang-$pvt2_lang/$pvt1_lang/model/phrase-table.gz
	pvt_trg_table_zip=$PWD/translation_models/$pvt2_lang-$trg_lang/train/model/phrase-table.gz
	src_pvt_table=$PWD/rectangulation/$src_lang-$trg_lang/triangulation/$src_lang-$pvt2_lang/$pvt1_lang/model/phrase-table
	pvt_trg_table=$PWD/translation_models/$pvt2_lang-$trg_lang/train/model/phrase-table
	gunzip -c $src_pvt_table_zip > $src_pvt_table
	gunzip -c $pvt_trg_table_zip > $pvt_trg_table
	src_test=data/test.$src_lang

	java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Pivot triangulation-filter \
	$src_pvt_table $pvt_trg_table $src_test $workers $n_best $out_dir
}

#---------------------------------------

src_lang=$1
trg_lang=$2
pvt2_lang=$3
pvt1_lang=$4
rectangulation_dir=$5

workers=20
n_best=10

out_dir=$rectangulation_dir/triangulation/$src_lang-$trg_lang/$pvt2_lang
mkdir -p $out_dir


#---------------------------------------
# triangulation
triangulation $src_lang $trg_lang $pvt2_lang $pvt1_lang $workers $n_best $out_dir

#---------------------------------------
# reordering
./scripts/rectangulation/reordering.sh $src_lang $trg_lang $pvt2_lang $out_dir

#---------------------------------------
# genrate tables
./scripts/rectangulation/generate-tables.sh $src_lang $trg_lang $pvt2_lang $out_dir