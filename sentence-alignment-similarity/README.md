Extending word alignment for sentence alignment using word similarity

# data
    Sample data sets for Japanese-Vietnamse sentence alignment
    (ja: Japanese, en: English, vi: Vietnamese)
    
    1. input: input file pairs for alignment (format: filename_lang.snt)
    2. mono: monolingual data sets to train word2vec
    3. bilingual: ja-en, en-vi (to train word similarity in bilingual contexts); 
    ja-vi (to enlarge the training data of word alignment)

# scripts
1. Sentence alignment by length-based phase

    ./length-based.sh


2. Train a word alignment model using IBM Model 1

    ./ibm1-train.sh

3. Extract n-best word pairs of the word alignment model

    ./ibm1-nbest.sh

4. Train word2vec models

    ./w2v-train.sh
    
5. Extract word similarity in monolingual contexts (from word2vec)

    ./w2v-most-similar.sh

6. Extract word similarity in bilingual contexts

    ./paraphrase-train.sh

7. Extending the word alignment by word similarity in word2vec models and bilingual contexts

    ./ibm1-w2v.sh

8. Produce final alignment

    ./alignment.sh

# References

[1] Moore, Robert C. "Fast and accurate sentence alignment of bilingual corpora." Conference of the Association for Machine Translation in the Americas. Springer Berlin Heidelberg, 2002.

[2] Mikolov, Tomas, et al. "Distributed representations of words and phrases and their compositionality." Advances in neural information processing systems. 2013.
