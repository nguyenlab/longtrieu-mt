#!/bin/sh
import os, sys

def read_tuning_score(in_file):
	fi=open(in_file)
	path=os.path.dirname(os.path.abspath(in_file))
	#print path
	score=''
	for line in fi:
		id=line.find("# BLEU")
		if id>-1:
			bleu=line[7:][:6]
			#print bleu
			score=''.join([path,'\t',bleu])
			break
	fi.close()
	
	#print score
	return score

def read_decoding_score(in_file):
	fi=open(in_file)
	path=os.path.dirname(os.path.abspath(in_file))
	#print path
	for line in fi:
		bleu=line[7:][:5]
		score=''.join([path,'\t',bleu])
		#print score
	fi.close()
	
	#print score
	return score

def read_tuning_score_dir(in_dir):
	scores=[]
	for root, dirs, files in os.walk(in_dir):
		for file in files:
			if file==("moses.ini"):
				#print(os.path.join(root, file))
				path=os.path.join(root, file)
				score=read_tuning_score(path)
				#print score
				scores.append(score)
	return scores

def read_decoding_score_dir(in_dir):
	scores=[]
	for root, dirs, files in os.walk(in_dir):
		for file in files:
			if file.endswith(".bleu"):
				#print(os.path.join(root, file))
				path=os.path.join(root, file)
				score=read_decoding_score(path)
				#print score
				scores.append(score)
	return scores

def save_tuning_scores(in_dir, out_file):
	fo=open(out_file,'w')
	scores=read_tuning_score_dir(in_dir)
	for score in scores:
		fo.write(score)
		fo.write('\n')
	fo.close()

def save_decoding_scores(in_dir, out_file):
	fo=open(out_file,'w')
	scores=read_decoding_score_dir(in_dir)
	for score in scores:
		fo.write(score)
		fo.write('\n\n')
	fo.close()
	
	
if __name__ == '__main__':
	tuning_dir=sys.argv[1]
	decoding_dir=sys.argv[2]
	out_dir='scores'
	if not os.path.exists(out_dir):
		os.makedirs(out_dir)
	out_tuning_file=os.path.join(out_dir, 'tuning-scores.bleu')
	out_decoding_file=os.path.join(out_dir, 'decoding-scores.bleu')
	
	save_tuning_scores(tuning_dir, out_tuning_file)
	save_decoding_scores(decoding_dir, out_decoding_file)
	
	#file='tuning/ms-vi/mert-work/moses.ini'
	#file='decode/fil-vi/fil-vi.bleu'
	#read_tuning_score(file)
	#read_decoding_score(file)
	#dir='tuning'
	#if not os.path.exists('scores'):
	#	os.makedirs('scores')
	#out_file='scores/tuning-scores.bleu'
	#read_tuning_score_dir(dir)
	#save_tuning_scores(dir, out_file)
	#dir = 'decode'
	#out_file='scores/decoding-scores.bleu'
	#save_decoding_scores(dir, out_file)
	