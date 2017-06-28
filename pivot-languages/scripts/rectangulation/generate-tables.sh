#!/bin/sh
# triangulation

MOSES_DIR=/work/$USER/moses/mosesdecoder
export JAVA_HOME=~/jdk1.8.0_40
export PATH=~/jdk1.8.0_40/bin:$PATH
JAVA_DIR=scripts/triangulation/pivot

src_lang=$1
trg_lang=$2
pvt_lang=$3
out_dir=$4
language_model=$PWD/language_model/lm.$trg_lang.bin

#--------------------------------------
# extract reordering table
gunzip -k $out_dir/reordering/train/model/reordering-table.wbe-msd-bidirectional-fe.gz
java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Pivot reordering \
	$out_dir/pivot/phrase-table.model $out_dir/reordering/train/model/reordering-table.wbe-msd-bidirectional-fe \
	$out_dir/model phrase-table reordering-table.wbe-msd-bidirectional-fe

#--------------------------------------
gzip $out_dir/model/phrase-table
gzip $out_dir/model/reordering-table.wbe-msd-bidirectional-fe

#--------------------------------------
# configuration file
java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Pivot moses-ini \
	$out_dir/model phrase-table.gz reordering-table.wbe-msd-bidirectional-fe.gz $language_model