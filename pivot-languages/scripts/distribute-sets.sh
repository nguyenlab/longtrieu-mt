#!/bin/sh

# distribute: train, tune, test

mkdir -p data
head -n 18000 corpus/alt/data_alt.true.en > data/train.en
head -n 18000 corpus/alt/data_alt.true.vi > data/train.vi
head -n 18000 corpus/alt/data_alt.true.fil > data/train.fil
head -n 18000 corpus/alt/data_alt.true.id > data/train.id
head -n 18000 corpus/alt/data_alt.true.ms > data/train.ms

sed -n '18001,19000p' corpus/alt/data_alt.true.en > data/tune.en
sed -n '18001,19000p' corpus/alt/data_alt.true.vi > data/tune.vi
sed -n '18001,19000p' corpus/alt/data_alt.true.fil > data/tune.fil
sed -n '18001,19000p' corpus/alt/data_alt.true.id > data/tune.id
sed -n '18001,19000p' corpus/alt/data_alt.true.ms > data/tune.ms

sed -n '19001,20000p' corpus/alt/data_alt.true.en > data/test.en
sed -n '19001,20000p' corpus/alt/data_alt.true.vi > data/test.vi
sed -n '19001,20000p' corpus/alt/data_alt.true.fil > data/test.fil
sed -n '19001,20000p' corpus/alt/data_alt.true.id > data/test.id
sed -n '19001,20000p' corpus/alt/data_alt.true.ms > data/test.ms

head -n 19000 corpus/alt/data_alt.true.en > data/train-lm.en
head -n 19000 corpus/alt/data_alt.true.vi > data/train-lm.vi
head -n 19000 corpus/alt/data_alt.true.fil > data/train-lm.fil
head -n 19000 corpus/alt/data_alt.true.id > data/train-lm.id
head -n 19000 corpus/alt/data_alt.true.ms > data/train-lm.ms