import gensim
#, logging
#import os
import sys
#import numpy


class MySentences(object):
	def __init__(self, fname):
		self.fname = fname
		
	def __iter__(self):
		for line in open(self.fname):
			yield line.split()
			
def w2v(corpus_file, model_file):#save model file, vector file
	sentences = MySentences(corpus_file) # a memory-friendly iterator
	model = gensim.models.Word2Vec(sentences,min_count=5, window=5, size=300, workers=40)
	model.save(model_file)


corpus_file=sys.argv[1]
model_file=sys.argv[2]


#main
w2v(corpus_file, model_file)