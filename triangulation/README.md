The scripts are to train pivot translation, which induces a source-target phrase table given a source-pivot phrase table and a pivot-target phrase tables.

# data:
- phrase-table.src-pvt, phrase-table.pvt-trg: input phrase tables
- test.src, test.trg: the test set to filter input phrase tables

# Training
The script to train a triangulation model

        ./triangulation.sh
    
# Training with filtered phrase tables by a test set 
Input phrase tables are filtered by a test set, in which the filtered phrase tables contain only phrases included in the test set to speed up the training time.

        ./triangulation-filter.sh

Edit files .sh: to set paths in your systems:
1. mosesdecoder
2. language-model file


# References

[1] Wu, Hua and Wang, Haifeng (2007): Pivot Language Approach for Phrase-Based Statistical Machine Translation, Proceedings of the 45th Annual Meeting of the Association of Computational Linguistics

[2] Cohn, Trevor and Lapata, Mirella (2007): Machine Translation by Triangulation: Making Effective Use of Multi-Parallel Corpora, Proceedings of the 45th Annual Meeting of the Association of Computational Linguistics

[3] Utiyama, Masao and Isahara, Hitoshi (2007): A Comparison of Pivot Methods for Phrase-Based Statistical Machine Translation, Human Language Technologies 2007: The Conference of the North American Chapter of the Association for Computational Linguistics; Proceedings of the Main Conference
