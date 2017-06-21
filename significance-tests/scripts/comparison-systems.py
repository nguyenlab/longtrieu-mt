#!/bin/sh
# comparison systems

import sys
import os
import utils

confidence=0.95

def extract_bleu_score(bleu_file):
	fi_bleu=open(bleu_file,'r')
	for line in fi_bleu:
		if line.find("BLEU =")>-1:
			result=line.strip().split()
			bleu=float(result[2].replace(',','',1))
	fi_bleu.close()
	return bleu

def compare_scores(baseline_file, proposed_file):
	baseline_bleu=extract_bleu_score(baseline_file)
	proposed_bleu=extract_bleu_score(proposed_file)
	
	#print baseline_bleu
	#print proposed_bleu
	
	if proposed_bleu>baseline_bleu:
		return 1
	else:
		return 0

def compare_scores_full(baseline_file, proposed_file, name):
	baseline_bleu=extract_bleu_score(baseline_file)
	proposed_bleu=extract_bleu_score(proposed_file)
	
	#print baseline_bleu
	#print proposed_bleu
	improve=0
	if proposed_bleu>baseline_bleu:
		improve=1
	
	file_result=''.join([name,' ',str(baseline_bleu),' ',str(proposed_bleu),' ',str(improve)])
	return file_result



def compare_one_sample(baseline_dir, proposed_dir):

	file_names=utils.get_immediate_subfiles(baseline_dir)
	
	count_improve=0
	sample_num=0
	for file_name in file_names:
	
		if file_name.find("bleu")>-1:
			baseline_file=os.path.join(baseline_dir, file_name)
			proposed_file=os.path.join(proposed_dir, file_name)
			sample_num+=1
			
			improve=compare_scores(baseline_file, proposed_file)
			if improve:
				count_improve+=1
				
	improved_ratio=float(count_improve)/sample_num
	#print count_improve
	#print sample_num
	#print ("ratio=%.2f"%(improved_ratio))
	if improved_ratio >= confidence:
		return 1
	else:
		return 0
def process_file_result(result):
	return result.split(' ')[3]

def compare_one_sample_full(baseline_dir, proposed_dir, sample_name, sample_size, result_file):
	result_file.write("sample_size: %s; sample_id: %s\n" %(sample_size,sample_name))
	
	file_names=utils.get_immediate_subfiles(baseline_dir)
	
	count_improve=0
	sample_num=0
	for file_name in file_names:
	
		if file_name.find("bleu")>-1:
			baseline_file=os.path.join(baseline_dir, file_name)
			proposed_file=os.path.join(proposed_dir, file_name)
			sample_num+=1
			
			name=file_name.split('.')[0]
			
			file_result=compare_scores_full(baseline_file, proposed_file,name)
			result_file.write("\t%s\n" %(file_result))
			
			#print file_result
			improve=process_file_result(file_result)
			#print improve
			if improve=='1':
				count_improve+=1
				
	improved_ratio=float(count_improve)/sample_num
	#print count_improve
	#print sample_num
	sample_improve='0'
	if improved_ratio >= confidence:
		sample_improve='1'
	#print ("ratio=%.2f"%(improved_ratio))
	sample_result=''.join([sample_name,' ',"%.2f"%(improved_ratio),' ',sample_improve])
	result_file.write("\tsample_result: %s\n\n" %(sample_result))
	
	#else:
	#	return 0
	return sample_improve
	
def compare_one_sample_size_full(work_dir, baseline_setup, proposed_setup, sample_size, result_file):
	
	baseline_dir=os.path.join(work_dir, baseline_setup,sample_size)
	proposed_dir=os.path.join(work_dir, proposed_setup,sample_size)
	
	samples=utils.get_immediate_subdirectories(baseline_dir)
	
	count_improve=0
	sample_num=len(samples)
	
	for sample_id in samples:
		baseline_sample=os.path.join(baseline_dir, sample_id)
		proposed_sample=os.path.join(proposed_dir, sample_id)
		
		sample_improve=compare_one_sample_full(baseline_sample, proposed_sample, sample_id, sample_size, result_file)
		if sample_improve=='1':
			count_improve+=1
	
	result_file.write('\nsample_size_result: sample-size=%s, sample-num=%d, improve-num=%d\n' %(sample_size, sample_num, count_improve))
	result_file.write('For %d samples, we draw the conclusion the proposed system is better than the baseline with at least 95%% statistical significance\n' %(count_improve))

if __name__ == '__main__':
	#baseline_file='decode/ms-vi/baseline/400/0/1.bleu'
	#proposed_file='decode/ms-vi/pos/400/0/1.bleu'
	#improve=compare_scores(baseline_file,proposed_file)
	#print improve
	#result=compare_scores(baseline_file,proposed_file,'1')
	#print result
	
	#baseline_dir='decode/ms-vi/baseline/400/2'
	#proposed_dir='decode/ms-vi/pos/400/2'
	
	work_dir='decode/ms-vi'
	system_1='surface'
	system_2='surface-pos-lemma-(weights)'
	
	result_file=open('surface-surface-pos-lemma-(weights)-400.txt','w')
	result_file.write('[sample_size] ; [sample_id] \n')
	result_file.write('\t[file_id] [baseline_bleu] [proposed_bleu] [improved?(1:improve, 0: no)] \n')
	result_file.write("\tsample_result: [sample_id] [improve_ratio] [improved?(1:improve, 0: no)] \n\n")
	
	#sample=compare_one_sample_full(baseline_dir,proposed_dir,'2','400',result_file)
	#print sample
		
	compare_one_sample_size_full(work_dir, system_1, system_2, '400', result_file)
	
	result_file.close()