#!/bin/sh

HYP=data/filename.hyp
REF=data/filename.ref

export JAVA_HOME=/path/jdk1.8.0_40
export PATH=/path/jdk1.8.0_40/bin:$PATH

BLEU_PATH=/path-to-moses/mosesdecoder/scripts/generic
TER_PATH=/path-to-ter/tercom-0.7.25
METEOR_PATH=/path-to-meteor/meteor-1.5

mkdir -p scores

# ------------------------------------------
# BLEU
perl $BLEU_PATH/multi-bleu.perl -lc $REF < $HYP > scores/result.bleu

# ------------------------------------------
# TER
python scripts/convert-text2tran.py $HYP data/hyp.tran
python scripts/convert-text2tran.py $REF data/ref.tran
java -jar $TER_PATH/tercom.7.25.jar -r data/ref.tran -h data/hyp.tran -o ter > scores/result.ter

# ------------------------------------------
# METEOR
python scripts/convert-text2sgm.py $HYP data/hyp.sgm
python scripts/convert-text2sgm.py $REF data/ref.sgm
java -Xmx2G -jar $METEOR_PATH/meteor-1.5.jar data/hyp.sgm data/ref.sgm > scores/result.meteor

# ------------------------------------------
# extract scores
python scripts/metrics-print-score.py scores/result.bleu scores/result.ter scores/result.meteor scores/mt.scores
