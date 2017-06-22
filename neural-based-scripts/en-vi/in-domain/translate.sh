#!/bin/sh

# theano device, in case you do not want to compute on gpu, change it to cpu
device=gpu

# path to nematus ( https://www.github.com/rsennrich/nematus )
nematus=~/git/nematus

dev=data/IWSLT15.TED.tst2015.en-vi.truecase.bpe.en
out=data/IWSLT15.TED.tst2015.en-vi.truecase.hyp.vi

THEANO_FLAGS=mode=FAST_RUN,floatX=float32,device=$device,on_unused_input=warn python $nematus/nematus/translate.py \
     -m model/model.npz \
     -i $dev \
     -o $out \
     -k 12 -n -p 1
