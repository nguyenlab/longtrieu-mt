#!/bin/sh
# interpolation


src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4
pvt3_lang=$5

#-------------------------------------------
# train tm tune

#./scripts/interpolation/translation-model-tuning.sh $src_lang $trg_lang

#-------------------------------------------
# wait until the previous step finished
# interpolation tune
#./scripts/interpolation/interpolation-tune.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang
#-------------------------------------------
# tuning
#./scripts/interpolation/tuning-mira-interpolation.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang
#-------------------------------------------
# decode
./scripts/interpolation/decode-interpolation.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang
