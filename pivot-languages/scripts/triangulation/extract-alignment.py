import os
import sys

workdir=sys.argv[1]
phrasetable=sys.argv[2]
srclang=sys.argv[3]
trglang=sys.argv[4]

srcfile='phrases.'+srclang
trgfile='phrases.'+trglang
alignfile='aligned.grow-diag-final-and'


outdir=os.path.join(workdir,'train/model')
#outdir=workdir
if not os.path.exists(outdir):
    os.makedirs(outdir)


fi=open(phrasetable,'r')
fo1=open(os.path.join(outdir,srcfile),'w')
fo2=open(os.path.join(outdir,trgfile),'w')
fo3=open(os.path.join(outdir,alignfile),'w')

for line in fi:
    #print (line)
    st=line.split('|||')
    #print st[0].strip()
    #print st[1].strip()
    fo1.write("%s\n"%(st[0].strip()))
    fo2.write("%s\n"%(st[1].strip()))
    fo3.write("%s\n"%(st[3].strip()))

fi.close()
fo1.close()
fo2.close()
fo3.close()