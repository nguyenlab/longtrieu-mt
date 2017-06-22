#!/bin/sh
#scripts for training triangulation with input phrase tables filtered by a test set

JAVA_DIR=pivot
MOSES_DIR=/path-to-moses/mosesdecoder # edit me

#source and target languages
SRCLANG=ja
TRGLANG=vi

#phrase-tables
SRC_PVT_TABLE=data/phrase-table.src-pvt
PVT_TRG_TABLE=data/phrase-table.pvt-trg

#test sets
SRC_TEST=data/test.src
TRG_TEST=data/test.trg

#triangulation
WORKERS=20 # multithreading in computing scores of triangulation
NBEST=10 # nbest: number of candidates output (target phrases) for each source phrase in the phrase-table output

#language-model
LANGUAGE_MODEL_PATH=/path-to-language-model/language-model.5gram.bin # edit me

#triangulation
java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Pivot triangulation-filter \
	$SRC_PVT_TABLE $PVT_TRG_TABLE $SRC_TEST $TRG_TEST $WORKERS $NBEST

#reordering
python extract-alignment.py triangulation/reordering triangulation/pivot/phrase-table.model $SRCLANG $TRGLANG

$MOSES_DIR/scripts/training/train-model.perl -root-dir triangulation/reordering/train \
-first-step 5 -last-step 5 \
-corpus triangulation/reordering/train/model/phrases \
-f $SRCLANG -e $TRGLANG \
-alignment grow-diag-final-and \
-reordering msd-bidirectional-fe \
>& extract-phrases.out

$MOSES_DIR/scripts/training/train-model.perl -root-dir triangulation/reordering/train \
-first-step 7 -last-step 7 \
-f $SRCLANG -e $TRGLANG \
-reordering msd-bidirectional-fe \
>& training-reordering.out

gunzip -k triangulation/reordering/train/model/reordering-table.wbe-msd-bidirectional-fe.gz

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Pivot reordering \
	triangulation/pivot/phrase-table.model triangulation/reordering/train/model/reordering-table.wbe-msd-bidirectional-fe \
	triangulation/model phrase-table reordering-table.wbe-msd-bidirectional-fe

gzip triangulation/model/phrase-table
gzip triangulation/model/reordering-table.wbe-msd-bidirectional-fe

#configuration file
java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Pivot moses-ini \
	triangulation/model phrase-table.gz reordering-table.wbe-msd-bidirectional-fe.gz $LANGUAGE_MODEL_PATH

