#!/bin/sh

#-------------------------------------------
#distribute dirs
python scripts/distribute-dirs.py data/wiki-javi 10000 data/parts

#-------------------------------------------
#word segment
./scripts/jvntextpro/jvntextpro.sh $PWD/data/.. file_name.vi 15000


#!/bin/sh

#-------------------------------------------
#tokenize japanese
#./scripts/tokenize-mecab.sh data/train.ja-en.ja data/train.ja-en.tok.ja

#-------------------------------------------
#tokenize, truecase english
#./scripts/tokenize-truecase.sh en data/train.ja-en.en

#-------------------------------------------
#clean 1..80
#mv data/train.ja-en.en.tc data/train.ja-en.tc.en
#mv data/train.ja-en.tok.ja data/train.ja-en.tc.ja
#./scripts/clean.sh data/train.ja-en.tc ja en data/train.ja-en.clean 1 80

#-------------------------------------------
#ted-alt
cat data/train.ja-en.clean.ja data/alt.train.tc.ja > data/ted-alt.train.ja
cat data/train.ja-en.clean.en data/alt.train.tc.en > data/ted-alt.train.en


