#!/bin/sh

# ----------------------------------------------
#test
mkdir -p decode

#./scripts/decode.sh $PWD/data/alt.test.tc.ja decode/alt.test.ja.hyp mert-work $PWD/data/alt.test.tc.vi decode/alt.test.ja.bleu

#./scripts/decode.sh $PWD/data/test.manual.ja decode/test.manual.ja.hyp mert-work $PWD/data/test.manual.vi decode/test.manual.ja.bleu

./scripts/decode.sh $PWD/data/tune.manual.ja decode/tune.manual.ja.hyp mert-work $PWD/data/tune.manual.vi decode/tune.manual.ja.bleu