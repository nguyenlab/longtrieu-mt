import sys

text_file=sys.argv[1]
sgm_file=sys.argv[2]

fi=open(text_file,'r')
fo=open(sgm_file,'w')

fo.write("<DOC>\n")
fo.write("<p>\n")

id=0

for line in fi:
	id=id+1
	line=line.strip()
	fo.write("<seg id=\"%d\">%s </seg>\n"%(id,line))

fo.write("</p>\n")
fo.write("</DOC>\n")


fo.close()
fi.close()