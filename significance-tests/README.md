significance tests [Koehn 2004]

./run-significance-tests.sh


# Create broad samples
    python scripts/broad-samples.py $SRC_LANG $TRG_LANG $SRC_FILE $TRG_FILE $OUT_DIR
    
    1. source lang: Malay
    2. target lang: Vietnamese
    3. bilingual files: data/test.ms, data/tes.vi
    4. output directory

- From the bilingual files (size M), create broad sample with different sample size (N=100, 200, 400)
- For each N, create (S=M/N) sets
- For each set: choose sentences in the bilingual files: 1, 1+S, 1+2*S, ... (broad sample)

# Generate random sets
    python scripts/draw-random-sets.py $SRC_LANG $TRG_LANG $OUT_DIR

- For each set in the broad samples, generate R random sets with replacement (R=1000)

For example:
- The input bilingual files: size M=2000 sentences
- Sample size N=400
- S=5 samples
- For each sample: generate 1000 sets
- Totally: generate 5 x 1000 sets with size=400 sentences

# Moses decoder: with multiple test sets

        ./decode-multi.sh

# Compare BLEU scores between two systems: baseline and proposed

        ./run-system-scores.sh
