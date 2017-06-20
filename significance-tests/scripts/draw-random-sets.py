# broad samples
import sys
import os
import random
import utils
from sets import Set



def get_set_id(set_name):
	return set_name.split('.')[0].split('-')[1]


def create_random_set_single(src_lang, trg_lang, src_in_file, trg_in_file, set_num, out_dir):
	set_name=os.path.basename(src_in_file)
	set_id=get_set_id(set_name)
	s_lines=utils.get_lines(src_in_file)
	t_lines=utils.get_lines(trg_in_file)
	input_size=len(s_lines)
	
	set_dir=os.path.join(out_dir, set_id)
	if not os.path.exists(set_dir):
		os.makedirs(set_dir)
	
	for set_id in range(0, set_num):
		fos=open(os.path.join(set_dir,"%d.%s"%(set_id,src_lang)),'w')
		fot=open(os.path.join(set_dir,"%d.%s"%(set_id,trg_lang)),'w')
		for i in range(0,input_size):
			id=random.randint(0,input_size-1)
			fos.write(s_lines[id])
			fot.write(t_lines[id])
		fos.close()
		fot.close()

def create_random_set_multi(src_lang, trg_lang, work_dir, set_num):
	broad_samples_dir=os.path.join(work_dir, "broad-samples")
	#sample_sizes=utils.get_immediate_subdirectories(broad_samples_dir)
	sample_dirs=utils.sub_dir_path(broad_samples_dir)
	
	random_dir=os.path.join(work_dir, "random-sets")
	
	for sample_dir in sample_dirs:
		
		sample_size=utils.get_base_name(sample_dir)
		sample_out_dir=os.path.join(random_dir,sample_size)
		
		samples_name=utils.get_immediate_subfiles(sample_dir)
		name_list=Set()
		
		for sample in samples_name:
			name_list.add(sample.split('.')[0])
		for name in name_list:
		
			src_in_file=os.path.join(work_dir, "broad-samples",sample_size,''.join([name,'.',src_lang]))
			trg_in_file=os.path.join(work_dir, "broad-samples",sample_size,''.join([name,'.',trg_lang]))
			
			create_random_set_single(src_lang, trg_lang, src_in_file, trg_in_file, set_num, sample_out_dir)
		
	

if __name__ == '__main__':
	src_lang=sys.argv[1]
	trg_lang=sys.argv[2]
	work_dir=sys.argv[3]
	
	set_num=10
	create_random_set_multi(src_lang, trg_lang, work_dir, set_num)
	