import sys

ter_file=sys.argv[1]
score_file=sys.argv[2]

fi=open(ter_file,'r')
fo=open(score_file,'w')


for line in fi:
	if line.find("Final score")>-1:
		fo.write("%s\n"%(line))

fo.close()
fi.close()