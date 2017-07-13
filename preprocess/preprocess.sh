#!/bin/sh

#-------------------------------------------
#distribute dirs
python scripts/distribute-dirs.py data/wiki-javi 10000 data/parts

#-------------------------------------------
#word segment
./scripts/jvntextpro/jvntextpro.sh $PWD/data/.. file_name.vi 15000

