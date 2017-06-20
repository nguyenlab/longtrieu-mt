# broad samples
import sys
import os
import argparse
from utils import get_lines


def get_sizes(sample_sizes):
	return sample_sizes.split(' ')

def broad_sample(sample_size,input_size, out_dir,src_lang,trg_lang):
	walk=input_size/int(sample_size)
	sample_dir=os.path.join(out_dir,"broad-samples",sample_size)
	if not os.path.exists(sample_dir):
		os.makedirs(sample_dir)
	
	for sample_id in range (0,walk):
		#print "sample: %d" %(sample_id)
		fos=open(os.path.join(sample_dir,"sample-%d.%s"%(sample_id,src_lang)),'w')
		fot=open(os.path.join(sample_dir,"sample-%d.%s"%(sample_id,trg_lang)),'w')
		for i in range (0,int(sample_size)):
			id=sample_id + i*walk
			#print id
			#line=lines[id]
			fos.write(s_lines[id])
			fot.write(t_lines[id])
	#print "end\n"
		fos.close()
		fot.close()

if __name__ == '__main__':
	
	src_lang=sys.argv[1]
	trg_lang=sys.argv[2]
	src_in_file=sys.argv[3]
	trg_in_file=sys.argv[4]
	out_dir=sys.argv[5]
	sample_sizes='100 200 400'
	
	s_lines=get_lines(src_in_file)
	t_lines=get_lines(trg_in_file)
	input_size=len(s_lines)
	sizes=get_sizes(sample_sizes)
	for sample_size in sizes:
		broad_sample(sample_size,input_size, out_dir,src_lang,trg_lang)