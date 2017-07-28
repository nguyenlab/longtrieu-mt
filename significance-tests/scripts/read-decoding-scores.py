#!/bin/sh
import os, sys
import utils
import numpy as np

def read_decoding_score(in_file):
	fi=open(in_file)
	path=os.path.dirname(os.path.abspath(in_file))
	#print path
	for line in fi:
		bleu=line[7:][:5]
		#score=''.join([path,'\t',bleu])
		#score=''.join([utils.get_base_name(in_file),'\t',bleu])
		score=bleu
		#print score
	fi.close()
	
	#print score
	return score

def n_lagest_list(arr, n):
	temp = np.partition(-arr, n)
	result = -temp[:n]
	return result
	
def n_smallest_list(arr, n):
	temp = np.partition(arr, n)
	result = temp[:n]
	return result


def confidence_intervals(arr, level):
	arr_len=len(arr)
	interval_1=len(arr)-level/2
	interval_2=interval_1-level/2
	#print interval_1
	#print interval_2
	
	bottom_arr=n_smallest_list(arr, interval_1)
	top_arr=n_lagest_list(bottom_arr, interval_2)
	
	
	return top_arr
	

def read_decoding_score_dir1(in_dir):
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

def bleu_intervals(in_dir, level, single_bleu_dir):
	st_builder=[]
	
	#test = np.array([9,1,3,4,8,7,2,5,6,0])
	
	for sample_dir in utils.sub_dir_path(in_dir):
		#print sample_dir
		
		st_builder.append(sample_dir)
		#test = np.array([9,1,3,4,8,7,2,5,6,0])
		#scores=np.zeros(1)
		bleu_arr=[]
		#min_score=100
		#max_score=0
		for file in utils.get_immediate_subfiles(sample_dir):
			#print file
			if file.endswith(".bleu"):				
				path=os.path.join(sample_dir, file)
				score=read_decoding_score(path)
				bleu=float(score)
				#np.append(scores, bleu)
				bleu_arr.append(bleu)
				
		#		if bleu<min_score:
		#			min_score=bleu
		#		if bleu>max_score:
		#			max_score=bleu
						
				#st_builder.append(score)
		scores=np.array(bleu_arr)
		interval=confidence_intervals(scores, level)
		#print scores
		st_builder.append("input scores")
		#st_builder.append(scores)
		#for score in scores:
		#	st_builder.append("%f "%(score))		
		
		#st_builder.append("\n")
		st_builder.append("min=%f max=%f"%(min(scores), max(scores)))
		
		st_builder.append("level: %d"%(level))
		st_builder.append("bleu intervals:")
		#st_builder.append(interval)
		#for score in interval:
		#	st_builder.append("%f "%(score))
		#st_builder.append("\n")
		st_builder.append("interval: %f %f"%(min(interval), max(interval)))
		#st_builder.append('\n')
		
		# single bleu score
		single_bleu_file_name=''.join(['sample-',utils.get_base_name(sample_dir),'.bleu'])
		single_bleu_path=os.path.join(single_bleu_dir, single_bleu_file_name)
		true_bleu=read_decoding_score(single_bleu_path)
		st_builder.append("true bleu: %s\n"%(true_bleu))
		
	return st_builder

def save_decoding_scores(in_dir, level, single_bleu_dir, out_file):
	fo=open(out_file,'w')
	scores=bleu_intervals(in_dir, level, single_bleu_dir)
	for score in scores:
		fo.write(score)
		fo.write('\n')
	fo.write('\n')	
	fo.close()
	
	
if __name__ == '__main__':
	decoding_dir=sys.argv[1]
	level=int(sys.argv[2])
	single_bleu_dir=sys.argv[3]
	out_dir=sys.argv[4]
	out_file_name=sys.argv[5]
	
	if not os.path.exists(out_dir):
		os.makedirs(out_dir)
	
	out_decoding_file=os.path.join(out_dir, out_file_name)
	save_decoding_scores(decoding_dir, level, single_bleu_dir, out_decoding_file)

	#test = np.array([9,1,3,4,8,7,2,5,6,0])
	#result=n_lagest_values(test)
	#print result
	#result=n_smallest_values(test)
	#print result
	
	#result=confidence_intervals(test, 5)
	#print result
	