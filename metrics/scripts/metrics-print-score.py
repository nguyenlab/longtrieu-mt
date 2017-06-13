import sys
import string
import math


bleu_file=sys.argv[1]
ter_file=sys.argv[2]
meteor_file=sys.argv[3]
scores_file=sys.argv[4]

fi_bleu=open(bleu_file,'r')
fi_ter=open(ter_file,'r')
fi_meteor=open(meteor_file,'r')

fo=open(scores_file,'w')

#------------------------------------
# bleu

for line in fi_bleu:
	if line.find("BLEU =")>-1:
		result=line.strip().split()
		bleu=result[2].replace(',','',1)
		fo.write("BLEU = %s\n"%(bleu))
fi_bleu.close()
#------------------------------------
# ter
for line in fi_ter:
	if line.find("Total TER")>-1:
		result=line.strip().split()
		ter=float(result[2])
		ter_round=math.ceil(ter*1000)/1000
		fo.write("TER = %s\n"%(ter_round))
fi_ter.close()
#------------------------------------
# meteor
for line in fi_meteor:
	if line.find("Final score")>-1:
		result=line.strip().split()
		meteor=float(result[2])
		meteor_round=math.ceil(meteor*1000)/1000
		fo.write("METEOR = %s\n"%(meteor_round))
fi_meteor.close()


fo.close()
