#!/bin/sh

MOSES_DIR=/work/$USER/moses/mosesdecoder


src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4
pvt3_lang=$5

tm_dir=$PWD/interpolation/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang-$pvt3_lang/tune/train/model
tune_dir=$PWD/tuning/interpolation/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang-$pvt3_lang/tune

./scripts/tuning-mira.sh $src_lang $trg_lang $tm_dir $tune_dir
