#!/bin/sh

#-------------------------------------------
# system scores
work_dir="decode/ms-vi"
baseline_setup="surface"
proposed_setup="surface-pos-lemma-(weights)"
sample_size="400"
out_dir=$PWD

python scripts/comparison-systems.py $work_dir $baseline_setup $proposed_setup $sample_size $out_dir

