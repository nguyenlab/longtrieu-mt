#!/bin/sh

# ---------------------------------------------
# Vietnamese word segmentation

in_dir=$1
file_name=$2
size=$3
out_dir=$in_dir/wseg-vi

mkdir -p $out_dir
out_file=$in_dir/$file_name.wseg

#python scripts/jvntextpro/segment-files.py $in_dir $file_name $size $out_dir
#./scripts/jvntextpro/WordSegment.sh inputdir $out_dir
#python scripts/jvntextpro/merge-segmented-files.py $file_name $out_dir $merged_file


