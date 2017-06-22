#/bin/sh

# path to moses decoder: https://github.com/moses-smt/mosesdecoder
mosesdecoder=~/git/mosesdecoder

# suffix of target language files
lng=vi

sed 's/\@\@ //g' | \
$mosesdecoder/scripts/recaser/detruecase.perl
