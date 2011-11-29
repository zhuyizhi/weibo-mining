package november.second;
import com.aliasi.tokenizer.CharacterTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.tokenizer.WhitespaceNormTokenizerFactory;
import com.aliasi.lm.TokenizedLM;
import com.aliasi.util.Files;
import com.aliasi.util.ScoredObject;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;

import java.util.SortedSet;

public class InterestingPhrases {

    private static int NGRAM = 1;
    private static int MIN_COUNT = 1;
    private static int MAX_NGRAM_REPORTING_LENGTH = 2;
    private static int NGRAM_REPORTING_LENGTH = 2;
    private static int MAX_COUNT = 100;

    private static File BACKGROUND_DIR 
    = new File("E:/programs/weibo4j-20110622/weibo4j/experiment/real_one/sentiment/expression/"); 
//        = new File("../../data/rec.sport.hockey/train");
    		
//    private static File FOREGROUND_DIR 
//        = new File("../../data/rec.sport.hockey/test");


    public static void main(String[] args) throws IOException {
//        TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
    	TokenizerFactory tokenizerFactory = CharacterTokenizerFactory.INSTANCE;
        System.out.println("Training background model");
        TokenizedLM backgroundModel = buildModel(tokenizerFactory,
                                                 NGRAM,
                                                 BACKGROUND_DIR);
        
        backgroundModel.sequenceCounter().prune(3);

        System.out.println("\nAssembling collocations in Training");
        SortedSet<ScoredObject<String[]>> coll 
            = backgroundModel.collocationSet(NGRAM_REPORTING_LENGTH,
                                             MIN_COUNT,MAX_COUNT);

        System.out.println("\nCollocations in Order of Significance:");
        report(coll);

//        System.out.println("Training foreground model");
//        TokenizedLM foregroundModel = buildModel(tokenizerFactory,
//                                                 NGRAM,
//                                                 FOREGROUND_DIR);
//        foregroundModel.sequenceCounter().prune(3);
//
//        System.out.println("\nAssembling New Terms in Test vs. Training");
//        SortedSet<ScoredObject<String[]>> newTerms 
//            = foregroundModel.newTermSet(NGRAM_REPORTING_LENGTH,
//                                       MIN_COUNT,
//                                       MAX_COUNT,
//                                       backgroundModel);
//
//        System.out.println("\nNew Terms in Order of Signficance:");
//        report(newTerms);
//                
//
//        System.out.println("\nDone.");
    } 

    private static TokenizedLM buildModel(TokenizerFactory tokenizerFactory,
                                          int ngram,
                                          File directory) 
        throws IOException {

        String[] trainingFiles = directory.list();
        TokenizedLM model = 
            new TokenizedLM(tokenizerFactory,
                            ngram);
        System.out.println("Training on "+directory);
                    
        for (int j = 0; j < trainingFiles.length; ++j) {
            String text = Files.readFromFile(new File(directory,
                                                      trainingFiles[j]),
                                             "UTF-8");
            model.handle(text);
        }
        return model;
    }

    private static void report(SortedSet<ScoredObject<String[]>> nGrams) {
        for (ScoredObject<String[]> nGram : nGrams) {
            double score = nGram.score();
            String[] toks = nGram.getObject();
            report_filter(score,toks);
        }
    }
    
    private static void report_filter(double score, String[] toks) {
        String accum = "";
        for (int j=0; j<toks.length; ++j) {
            if (nonCapWord(toks[j])) return;
            accum += " "+toks[j];
        }
        System.out.println("Score: "+score+" with :"+accum);
    }

    private static boolean nonCapWord(String tok) {
        if (!Character.isUpperCase(tok.charAt(0)))
            return true;
        for (int i = 1; i < tok.length(); ++i) 
            if (!Character.isLowerCase(tok.charAt(i))) 
                return true;
        return false;
    }

}
