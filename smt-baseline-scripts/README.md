Scripts to train SMT baseline models using Moses.

1. Tokenize, truecase, clean (1..80)

        ./tokenize.sh

2. Train language model

        ./language-model.sh
    
3. Train translation model

        ./translation-model.sh
    
4. Tuning parameters: using a tuning set
    
        ./tuning-mira.sh
    
5. Decode: translate a test set and compute BLEU score given a reference set

        ./decode.sh

data: bilingual corpora (sample data sets in English-Vietnamese)
  - training: train.en, train.vi
  - tuning: tune.en, tune.vi
  - test: test.en, test.vi
  
  
