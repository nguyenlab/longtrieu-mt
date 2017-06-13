import sys

text_file=sys.argv[1]
sgm_file=sys.argv[2]

fi=open(text_file,'r')
fo=open(sgm_file,'w')


id=0

for line in fi:
	id=id+1
	line=line.strip()
	fo.write("%s (%d)\n"%(line,id))



fo.close()
fi.close()