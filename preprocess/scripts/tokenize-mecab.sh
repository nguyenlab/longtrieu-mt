#!/bin/sh

#wget https://wit3.fbk.eu/mt.php?release=2012-02-plain
mecab=/work/$USER/mecab/mecab-0.996/bin/mecab

in_file=$1
out_file=$2

$mecab $in_file -O wakati -o $out_file

#$mecab data/train.ja-en.ja -O wakati -o data/train.ja-en.tok.ja