import sys, os

phrase_table_path=sys.argv[1]
reordering_table_path=sys.argv[2]
lm_path=sys.argv[3]
out_dir=sys.argv[4]

file=open(os.path.join(out_dir,'moses.ini'),'w')

file.write('%s\n'%'#########################')
file.write('%s\n'%'### MOSES CONFIG FILE ###')
file.write('%s\n\n'%'#########################')

file.write('%s\n'%'# input factors')
file.write('%s\n'%'[input-factors]')
file.write('%s\n\n'%'0')

file.write('%s\n'%'# mapping steps')
file.write('%s\n'%'[mapping]')
file.write('%s\n\n'%'0 T 0')

file.write('%s\n'%'[distortion-limit]')
file.write('%s\n\n'%'6')

file.write('%s\n'%'# feature functions')
file.write('%s\n'%'[feature]')
file.write('%s\n'%'UnknownWordPenalty')
file.write('%s\n'%'WordPenalty')
file.write('%s\n'%'PhrasePenalty')
file.write('PhraseDictionaryMemory name=TranslationModel0 num-features=4 path=%s input-factor=0 output-factor=0\n'%phrase_table_path)
file.write('LexicalReordering name=LexicalReordering0 num-features=6 type=wbe-msd-bidirectional-fe-allff input-factor=0 output-factor=0 path=%s\n'%reordering_table_path)
file.write('%s\n'%'Distortion')
file.write('KENLM name=LM0 factor=0 path=%s order=5\n\n'%lm_path)

file.write('%s\n'%'# dense weights for feature functions')
file.write('%s\n'%'[weight]')
file.write('%s\n'%'# The default weights are NOT optimized for translation quality. You MUST tune the weights.')
file.write('%s\n'%'# Documentation for tuning is here: http://www.statmt.org/moses/?n=FactoredTraining.Tuning')
file.write('%s\n'%'UnknownWordPenalty0= 1')
file.write('%s\n'%'WordPenalty0= -1')
file.write('%s\n'%'PhrasePenalty0= 0.2')
file.write('%s\n'%'TranslationModel0= 0.2 0.2 0.2 0.2')
file.write('%s\n'%'LexicalReordering0= 0.3 0.3 0.3 0.3 0.3 0.3')
file.write('%s\n'%'Distortion0= 0.3')
file.write('%s\n'%'LM0= 0.5')

file.close()