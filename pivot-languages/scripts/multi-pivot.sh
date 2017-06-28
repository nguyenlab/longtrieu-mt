#!/bin/sh
# interpolation


src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4
pvt3_lang=$5


#-------------------------------------
#./scripts/interpolation/translation-model-tuning.sh $src_lang $trg_lang

#-------------------------------------
# multi-pivot-weights
#./scripts/multi-pivot/multi-pivot-weights.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang

#-------------------------------------------
# tuning
#./scripts/multi-pivot/tuning-mira-multi-pivot.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang

#-------------------------------------------
# decode
./scripts/multi-pivot/decode-multi-pivot.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang $pvt3_lang