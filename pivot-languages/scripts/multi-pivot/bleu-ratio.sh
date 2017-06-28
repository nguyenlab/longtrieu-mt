#!/bin/sh

src_lang=$1
trg_lang=$2
pvt1_lang=$3
pvt2_lang=$4
pvt3_lang=$5

file_1=tuning/triangulation/$src_lang-$trg_lang/$pvt1_lang/mert-work/moses.ini
file_2=tuning/triangulation/$src_lang-$trg_lang/$pvt2_lang/mert-work/moses.ini
file_3=tuning/triangulation/$src_lang-$trg_lang/$pvt3_lang/mert-work/moses.ini

out_dir=multi-bleu/$src_lang-$trg_lang/$pvt1_lang-$pvt2_lang-$pvt3_lang/weights
mkdir -p $out_dir
out_file=$out_dir/ratios.bleu

python scripts/multi-pivot/bleu-ratios.py $file_1 $file_2 $file_3 $out_file