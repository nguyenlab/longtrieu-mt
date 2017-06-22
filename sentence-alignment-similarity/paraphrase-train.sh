#!/bin/sh

JAVA_DIR=sentence_alignment


mkdir -p paraphrase

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner ibm1 \
	data/bilingual/ja-en.en data/bilingual/ja-en.ja 5 paraphrase en-ja.ibm1

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner nbest-ibm1 \
	paraphrase/en-ja.ibm1 5 0.1 paraphrase en-ja.ibm1.nbest en-ja.en.words en-ja.ja.words

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner ibm1-paraphrase \
	paraphrase/en-ja.ibm1.nbest paraphrase para.ja.bin para.ja.txt

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner filter-w2v-similarity-paraphrase \
	paraphrase/para.ja.bin ibm1-similarity/most-similar.ja ibm1-similarity word-similarity.w2v.para.ja

	

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner ibm1 \
	data/bilingual/en-vi.en data/bilingual/en-vi.vi 5 paraphrase en-vi.ibm1

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner nbest-ibm1 \
	paraphrase/en-vi.ibm1 5 0.1 paraphrase en-vi.ibm1.nbest en-vi.en.words en-vi.vi.words 

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner ibm1-paraphrase \
	paraphrase/en-vi.ibm1.nbest paraphrase para.vi.bin para.vi.txt

java -cp $JAVA_DIR/target/classes:$JAVA_DIR/target/lib/* Sentence_Aligner filter-w2v-similarity-paraphrase \
	paraphrase/para.vi.bin ibm1-similarity/most-similar.vi ibm1-similarity word-similarity.w2v.para.vi