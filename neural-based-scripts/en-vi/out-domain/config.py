import numpy
import os
import sys

SRC_VOCAB_SIZE = 72251
TRG_VOCAB_SIZE = 43992

SRC = "en"
TGT = "vi"
DATA_DIR = "data"
TRAIN_FILE="train.tags.en-vi.out-domain.truecase"
DEV_FILE="IWSLT15.TED.tst2012.en-vi.truecase"

NEMATUS_DIR='~/git/nematus'

sys.path.insert(0, NEMATUS_DIR)

from nematus.nmt import train


if __name__ == '__main__':
    validerr = train(saveto='model/model.npz',
                    finish_after=300000,
                    reload_=True,
                    dim_word=500,
                    dim=1024,
                    n_words=TRG_VOCAB_SIZE,
                    n_words_src=SRC_VOCAB_SIZE,
                    decay_c=0.,
                    clip_c=1.,
                    lrate=0.02,
                    optimizer='adadelta',
                    maxlen=50,
                    batch_size=60,
                    valid_batch_size=60,                    
                    datasets=[DATA_DIR + '/' + TRAIN_FILE + '.bpe.' + SRC, DATA_DIR + '/' + TRAIN_FILE + '.bpe.' + TGT],                    
                    valid_datasets=[DATA_DIR + '/' + DEV_FILE + '.bpe.' + SRC, DATA_DIR + '/' + DEV_FILE + '.bpe.' + TGT],
                    dictionaries=[DATA_DIR + '/' + TRAIN_FILE + '.bpe.' + SRC + '.json',DATA_DIR + '/' + TRAIN_FILE + '.bpe.' + TGT + '.json'],
                    validFreq=3000,
                    dispFreq=500,
                    saveFreq=6000,
                    sampleFreq=3000,
                    use_dropout=False,
                    dropout_embedding=0.2, # dropout for input embeddings (0: no dropout)
                    dropout_hidden=0.2, # dropout for hidden layers (0: no dropout)
                    dropout_source=0.1, # dropout source words (0: no dropout)
                    dropout_target=0.1, # dropout target words (0: no dropout)
                    overwrite=False,
                    external_validation_script='./validate.sh')
    print validerr
