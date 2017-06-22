# Preprocess data sets

  ./preprocess.sh

# Configurations

   Edit the file: config.py
  
# Training

  ./train.sh

# Decoding

  ./translate.sh

# Postprocess the translation output

  ./postprocess-test.sh < data/kyoto-test.hyp.en > data/kyoto-test.hyp.en.postprocessed
