import sys, os
import shutil

def distribute_dirs(in_dir,size, out_dir):
	print in_dir
	make_dir(out_dir)

	files=get_immediate_subfiles(in_dir)
	
	for file in files:
		file_name=get_file_name(file)
		#print file_name
		div=(int(file_name)-1)/size*size+1
		#print div
		out_sub_dir=os.path.join(out_dir, str(div))
		make_dir(out_sub_dir)
		in_file=os.path.join(in_dir, file)
		out_file=os.path.join(out_sub_dir, file)
		shutil.copy(in_file, out_file)

def make_dir(dir):
	if not os.path.exists(dir):
		os.makedirs(dir)
	
def get_file_name(file_name):
	return file_name.split('_')[0]
	
	
def get_immediate_subfiles(a_dir):
    return [name for name in os.listdir(a_dir)
            if os.path.isfile(os.path.join(a_dir, name))]

if __name__ == '__main__':
	
	in_dir=sys.argv[1]
	size=int(sys.argv[2])
	out_dir=sys.argv[3]
	
	distribute_dirs(in_dir, size, out_dir)