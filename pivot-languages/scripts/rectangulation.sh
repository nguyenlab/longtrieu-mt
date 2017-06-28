#!/bin/sh
# rectangulation


src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4

#-------------------------------------
rectangulation_dir=$PWD/rectangulation/$src_lang-$trg_lang
mkdir -p $rectangulation_dir

# src-pvt1-pvt2
#./scripts/rectangulation/triangulation-1.sh $src_lang $pvt2_lang $pvt1_lang $rectangulation_dir

#-------------------------------------
#src-pvt2-trg
#./scripts/rectangulation/triangulation-2.sh $src_lang $trg_lang $pvt2_lang $pvt1_lang $rectangulation_dir

#-------------------------------------------
# tuning
#./scripts/rectangulation/tuning-mira-rectangulation.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang 
#-------------------------------------------
# decode
#./scripts/rectangulation/decode-rectangulation.sh $src_lang $trg_lang $pvt1_lang $pvt2_lang

