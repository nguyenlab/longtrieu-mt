#!/bin/sh
# compute confidence intervals

#-------------------------------------------
# ms-vi

#python scripts/read-decoding-scores.py decode/ms-vi/surface/400 5 decode/wilcoxon/ms-vi/surface/400  decode/confidence-intervals/ms-vi surface.bleu

python scripts/read-decoding-scores.py decode/ms-vi/"surface-pos-lemma-(weights)"/400 5 decode/wilcoxon/ms-vi/"surface-pos-lemma-(weights)"/400  decode/confidence-intervals/ms-vi "surface-pos-lemma-(weights)".bleu