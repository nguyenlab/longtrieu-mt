import sys, os

def segment_file(in_dir, file_name, size, out_dir):
	in_file=os.path.join(in_dir, file_name)
	fi=open(in_file, 'r')
	
	id=0
	file_id=1
	sub_file_name=''.join([file_name,'-',str(file_id),'.tkn'])
	sub_file_path=os.path.join(out_dir, sub_file_name)
	fo=open(sub_file_path, 'w')
	
	for line in fi:
		id+=1
		fo.write(line)
		if id==size:
			fo.close()
			file_id+=1
			sub_file_name=''.join([file_name,'-',str(file_id),'.tkn'])
			sub_file_path=os.path.join(out_dir, sub_file_name)
			fo=open(sub_file_path, 'w')
			id=0
	fo.close()
	fi.close()
	

if __name__ == '__main__':
	in_dir=sys.argv[1]
	file_name=sys.argv[2]
	size=int(sys.argv[3])
	out_dir=sys.argv[4]
	
	segment_file(in_dir, file_name, size, out_dir)
	
	