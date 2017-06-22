#!/bin/sh

mkdir w2v-model

python w2v.py data/mono/monolingual.ja w2v-model/ja-w2v.model
python w2v.py data/mono/monolingual.vi w2v-model/vi-w2v.model
