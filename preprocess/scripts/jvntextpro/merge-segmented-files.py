import sys, os

def merge_segmented_files(file_name, out_dir, out_file):
	id=0
	for file in os.listdir(out_dir):
		if file.endswith(".wseg"):
			id+=1
	
	fo=open(out_file, 'w')
	nline=0
	for file_id in range(1,id+1):
		sub_file_name=''.join([file_name,'-',str(file_id),'.tkn','.wseg'])
		sub_file_path=os.path.join(out_dir, sub_file_name)
		
		fi=open(sub_file_path, 'r')
		for line in fi:
			fo.write(line)
			nline+=1
		fo.write('\n')
		fi.close()
	
	fo.close()
	print ('merge lines: %d'%(nline))

if __name__ == '__main__':
	file_name=sys.argv[1]
	out_dir=sys.argv[2]
	out_file=sys.argv[3]
	
	merge_segmented_files(file_name, out_dir, out_file)
	