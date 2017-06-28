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

def bleu_ratio_2(file_1, file_2, out_file):
	result_1=read_tuning_score(file_1)
	result_2=read_tuning_score(file_2)
	
	score_1=float(result_1.split('\t')[1])
	score_2=float(result_2.split('\t')[1])
	
	
	total=score_1+score_2
	
	ratios=[]
	ratio_1=float(score_1/total)
	ratio_2=float(score_2/total)
	
	ratios.append(ratio_1)
	ratios.append(ratio_2)
	
	#print total
	#print ratios
	fo=open(out_file,'w')
	#fo.write(result_1)
	#fo.write(result_2)
	#fo.write(result_3)
	fo.write("%s %s\n"%(ratio_1, ratio_2))
	fo.close()
	return ratios
	
if __name__ == '__main__':
	file_1=sys.argv[1]
	file_2=sys.argv[2]
	out_file=sys.argv[3]
	
	bleu_ratio_2(file_1, file_2, out_file)