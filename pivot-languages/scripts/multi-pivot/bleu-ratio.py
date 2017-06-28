import sys

bleu_arg=sys.argv[1]

bleus=bleu_arg.split(' ')

total=0
for score in bleus:
	total=total+float(score)

ratio=[]
for score in bleus:
	ratio.append(float(score)/total)

i=0
for score in bleus:
	print ('model %d, bleu=%s'%(i,score))
	i=i+1
print
i=0
for r in ratio:
	print ('ratio%d=%f'%(i,r))
	i=i+1