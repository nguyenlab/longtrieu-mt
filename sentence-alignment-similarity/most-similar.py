import gensim
import sys, os


modelFile=sys.argv[1]
ibmFileName=sys.argv[2]
outDir=sys.argv[3]
outFileName=sys.argv[4]


ibmSet=[]
ibmFile=open(ibmFileName,'r')
for word in ibmFile:
	ibmSet.append(word.strip())
ibmFile.close()


def mkdir(dir):
	if not os.path.exists(dir):
		os.makedirs(dir)

mkdir(outDir)
fileName=os.path.join(outDir,outFileName)
outFile=open(fileName,'w')		

model=gensim.models.Word2Vec.load(modelFile)
vocabs=model.vocab

for word in ibmSet:
	if word in vocabs:
		similars=model.most_similar(word)
		if len(similars)>0:			
			for sim in similars:
				simWord=sim[0]
				score=sim[1]			
				outFile.write("%s %s %s\n"%(word, simWord,score))

outFile.close()
print 'finish!'