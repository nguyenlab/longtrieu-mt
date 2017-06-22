# Preprocess data sets

  ./preprocess.sh

# Configurations

   Edit the file: config.py
  
# Training

  ./train.sh

# Decoding

  ./translate.sh

# Postprocess the translation output

  ./postprocess-test.sh < data/IWSLT15.TED.tst2015.cs-en.truecase.hyp.en > data/IWSLT15.TED.tst2015.cs-en.truecase.hyp.en.postprocessed
