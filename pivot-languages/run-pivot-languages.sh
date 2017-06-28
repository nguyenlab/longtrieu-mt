#!/bin/sh

#-------------------------------------------
# distribute: train, tune, test

#./scripts/distribute-sets.sh

#-------------------------------------------
# train lm
#./scripts/language-model.sh

#-------------------------------------------
# train tm
# ms-vi
#./scripts/translation-model.sh ms vi
#./scripts/translation-model.sh ms en
#./scripts/translation-model.sh en vi
#./scripts/translation-model.sh ms id
#./scripts/translation-model.sh id vi
#./scripts/translation-model.sh ms fil
#./scripts/translation-model.sh fil vi

# id-vi
#./scripts/translation-model.sh id en
#./scripts/translation-model.sh id fil
#./scripts/translation-model.sh id ms


# fil-vi
#./scripts/translation-model.sh fil en
#./scripts/translation-model.sh fil ms
#./scripts/translation-model.sh fil id

#
#./scripts/translation-model.sh en id

#-------------------------------------------
# tuning
#./scripts/tuning-mira.sh ms vi
#./scripts/tuning-mira.sh id vi
#./scripts/tuning-mira.sh fil vi

#-------------------------------------------
# decode
#./scripts/decode.sh ms vi
#./scripts/decode.sh id vi
#./scripts/decode.sh fil vi

#-------------------------------------------
# triangulation
# ms-vi
#./scripts/triangulation.sh ms vi en
#./scripts/triangulation.sh ms vi id
#./scripts/triangulation.sh ms vi fil

# tuning
#./scripts/triangulation/tuning-mira-pivot.sh ms vi en
#./scripts/triangulation/tuning-mira-pivot.sh ms vi id
#./scripts/triangulation/tuning-mira-pivot.sh ms vi fil

# decode
#./scripts/decode-pivot.sh ms vi en
#./scripts/decode-pivot.sh ms vi id
#./scripts/decode-pivot.sh ms vi fil

# id-vi
#./scripts/triangulation.sh id vi en
#./scripts/triangulation.sh id vi ms
#./scripts/triangulation.sh id vi fil
# tuning
#./scripts/triangulation/tuning-mira-pivot.sh id vi en
#./scripts/triangulation/tuning-mira-pivot.sh id vi ms
#./scripts/triangulation/tuning-mira-pivot.sh id vi fil
# decode
#./scripts/decode-pivot.sh id vi en
#./scripts/decode-pivot.sh id vi ms
#./scripts/decode-pivot.sh id vi fil

# fil-vi
#./scripts/triangulation.sh fil vi en
#./scripts/triangulation.sh fil vi ms
#./scripts/triangulation.sh fil vi id
# tuning
#./scripts/triangulation/tuning-mira-pivot.sh fil vi en
#./scripts/triangulation/tuning-mira-pivot.sh fil vi ms
#./scripts/triangulation/tuning-mira-pivot.sh fil vi id
# decode
#./scripts/decode-pivot.sh fil vi en
#./scripts/decode-pivot.sh fil vi id
#./scripts/decode-pivot.sh fil vi ms
#-------------------------------------------
# interpolation: source target pivot1 pivot2 pivot3
# ms-vi
#./scripts/interpolation.sh ms vi en fil id
#./scripts/interpolation.sh id vi en fil ms

#-------------------------------------------
# multi-pivot
#./scripts/multi-pivot.sh ms vi en fil id
#./scripts/multi-pivot.sh id vi en fil ms
#./scripts/multi-pivot.sh fil vi en id ms

#-------------------------------------------
# rectangulation
#./scripts/rectangulation.sh ms vi id en
#./scripts/rectangulation.sh id vi ms en
#./scripts/rectangulation.sh fil vi id en

#-------------------------------------------
# interpolation-rectangulation
#./scripts/interpolation-rectangulation.sh ms vi id en en
#./scripts/interpolation-rectangulation.sh ms vi id en id
#./scripts/interpolation-rectangulation.sh id vi ms en en
#./scripts/interpolation-rectangulation.sh id vi ms en ms
#./scripts/interpolation-rectangulation.sh fil vi id en id
#./scripts/interpolation-rectangulation.sh fil vi id en en

#-------------------------------------------
# scores
# python scripts/read-scores.py tuning decode