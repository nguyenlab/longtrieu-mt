#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder


src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4


tm_dir=$PWD/rectangulation/$src_lang-$trg_lang/triangulation/$src_lang-$trg_lang/$pvt2_lang/model
tune_dir=$PWD/tuning/rectangulation/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang

./scripts/tuning-mira.sh $src_lang $trg_lang $tm_dir $tune_dir
