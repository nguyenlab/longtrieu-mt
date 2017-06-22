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
	count_opposite=0
	
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
			if improve=='0':
				count_opposite+=1
				
	improved_ratio=float(count_improve)/sample_num
	opposite_ratio=float(count_opposite)/sample_num
	#print count_improve
	#print sample_num
	sample_improve='0'
	if improved_ratio >= confidence:
		sample_improve='1'
	
	sample_opposite='0'
	if opposite_ratio >= confidence:
		sample_opposite='1'
		
	#print ("ratio=%.2f"%(improved_ratio))
	sample_result=''.join([sample_name,' ',sample_improve, ' ',"%.2f"%(improved_ratio), ' ', sample_opposite,' ',"%.2f"%(opposite_ratio)])
	result_file.write("\tsample_result: %s\n" %(sample_result))
	result_file.write('-------------------------------\n\n')
	
	#else:
	#	return 0
	if sample_improve=='1':
		return '1'
	elif sample_opposite=='1':
		return '0'
	else:
		return '-1'
	
def compare_one_sample_size_full(work_dir, baseline_setup, proposed_setup, sample_size, result_file):
	
	baseline_dir=os.path.join(work_dir, baseline_setup,sample_size)
	proposed_dir=os.path.join(work_dir, proposed_setup,sample_size)
	
	samples=utils.get_immediate_subdirectories(baseline_dir)
	
	count_improve=0
	count_opposite=0
	sample_num=len(samples)
	
	for sample_id in samples:
		baseline_sample=os.path.join(baseline_dir, sample_id)
		proposed_sample=os.path.join(proposed_dir, sample_id)
		
		sample_improve=compare_one_sample_full(baseline_sample, proposed_sample, sample_id, sample_size, result_file)
		if sample_improve=='1':
			count_improve+=1
		elif sample_improve=='0':
			count_opposite+=1
	
	result_file.write('\nsample_size_result: sample-size=%s, sample-num=%d, improve-num=%d, wrong-num=%d, ratio that %.0f%% statistical significance is made = %.0f%%, wrong ratio = %.0f%%\n' %(sample_size, sample_num, count_improve, count_opposite, confidence*100, float(count_improve)/sample_num*100, float(count_opposite)/sample_num*100))
	result_file.write('For %d samples in total %d samples, we draw the conclusion the proposed system is better than the baseline with at least %.0f%% statistical significance\n' %(count_improve, sample_num, confidence*100))
	
	result_file.close()
	
def init(baseline_setup, proposed_setup, sample_size, out_dir, result_file):
	
	
	result_file.write('Paired Bootstrap Resampling:\n')
	result_file.write('System 1: %s\n' %(baseline_setup))
	result_file.write('System 2: %s\n' %(proposed_setup))
	result_file.write('Sample size: %s\n\n' %(sample_size))
	
	result_file.write('[sample_size] ; [sample_id] \n')
	result_file.write('\t[file_id] [baseline_bleu] [proposed_bleu] [improved?(1:improve, 0: no)] \n')
	result_file.write("\tsample_result: [sample_id] [improved?(1:improve, 0: no)] [improve_ratio] [wrong?(1:wrong, 0: no)] [wrong_ratio] \n\n")
	result_file.write('-------------------------------\n')
	return

if __name__ == '__main__':
	
	#work_dir='old/decode/ms-vi'
	#baseline_setup='pos'
	#proposed_setup='surface-pos-lemma-(weights)'
	#sample_size='400'
	#out_dir='old'
	
	work_dir=sys.argv[1]
	baseline_setup=sys.argv[2]
	proposed_setup=sys.argv[3]
	sample_size=sys.argv[4]
	out_dir=sys.argv[5]
	
	
	file_name=''.join([baseline_setup,'-VS-',proposed_setup,'-',sample_size,'.txt'])
	result_file_path=os.path.join(out_dir, 'results', file_name)
	result_file=open(result_file_path,'w')
	
	init(baseline_setup, proposed_setup, sample_size, out_dir, result_file)
		
	compare_one_sample_size_full(work_dir, baseline_setup, proposed_setup, sample_size, result_file)
	