# utils
import os

def get_lines(in_file):
	fi=open(in_file,'r')
	lines=[]
	for line in fi:
		lines.append(line)
	fi.close()
	return lines

def get_immediate_subdirectories(a_dir):
    return [name for name in os.listdir(a_dir)
            if os.path.isdir(os.path.join(a_dir, name))]

def sub_dir_path (d):
    return filter(os.path.isdir, [os.path.join(d,f) for f in os.listdir(d)])

def get_base_name (file):
	return os.path.basename(file)

def get_immediate_subfiles(a_dir):
    return [name for name in os.listdir(a_dir)
            if os.path.isfile(os.path.join(a_dir, name))]