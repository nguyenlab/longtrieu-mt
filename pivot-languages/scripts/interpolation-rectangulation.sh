#!/bin/sh
# rectangulation


src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4
pvt_lang=$5


bleu_1=tuning/triangulation/$src_lang-$trg_lang/$pvt_lang/mert-work/moses.ini
bleu_2=tuning/rectangulation/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang/mert-work/moses.ini
model_1=triangulation/$src_lang-$trg_lang/$pvt_lang
model_2=rectangulation/$src_lang-$trg_lang/triangulation/$src_lang-$trg_lang/$pvt2_lang
language_model=$PWD/language_model/lm.$trg_lang.bin
tm_dir=$PWD/interpolation-rectangulation/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang/$pvt_lang
tune_dir=$PWD/tuning/interpolation-rectangulation/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang/$pvt_lang
# ---------------------------------------
./scripts/interpolation/weights/multi-pivot-weights-2.sh $bleu_1 $bleu_2 $model_1 $model_2 $language_model $tm_dir

# ---------------------------------------

./scripts/tuning-mira.sh $src_lang $trg_lang $tm_dir $tune_dir
