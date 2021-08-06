package my.selph.domain.ai;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class POSProcessor {

    @ConfigProperty(name = "app.ai.tokeniser")
    String tokeniserModel;

    @ConfigProperty(name = "app.ai.pos-tagger")
    String postModel;

    public static final List<String> ALLOWEDPOS = List.of("NN","NNS","VB","VBG","VBN","JJ","JJS","PRP","PRP$");

    public List<TokenPOS> extractPOS(String sentence) throws IOException {
        // tokenise sentence
        var tokenModelIn = getClass().getClassLoader().getResourceAsStream(tokeniserModel);
        var tokenModel = new TokenizerModel(tokenModelIn);
        var tokeniser = new TokenizerME(tokenModel);
        String tokens[] = tokeniser.tokenize(sentence);

        // create POST tagger
        var posModelIn = getClass().getClassLoader().getResourceAsStream(postModel);
        var posModel = new POSModel(posModelIn);
        var posTagger = new POSTaggerME(posModel);

        // tag sentence
        String tags[] = posTagger.tag(tokens);
        // ignore probability
        // double probs[] = posTagger.probs();

        // filter tags
        var tokenPOS = new ArrayList<TokenPOS>();
        System.out.println("--------");
        for(var i = 0;i < tags.length; i++) {
            var tag = tags[i];
            if(ALLOWEDPOS.contains(tag))
                tokenPOS.add(new TokenPOS(tokens[i], tag));
            else System.out.println(tokens[i]+" - "+tags[i]);
        }

        return  tokenPOS;
    }

    public List<List<TokenPOS>> extractPOS(List<String> sentences) throws IOException {
        // tokenise sentence
        var tokenModelIn = getClass().getClassLoader().getResourceAsStream(tokeniserModel);
        var tokenModel = new TokenizerModel(tokenModelIn);
        var tokeniser = new TokenizerME(tokenModel);

        // create POST tagger
        var posModelIn = getClass().getClassLoader().getResourceAsStream(postModel);
        var posModel = new POSModel(posModelIn);
        var posTagger = new POSTaggerME(posModel);

        var tokensList = sentences.stream().map(tokeniser::tokenize).toList();

        return tokensList.stream().map(tokens -> {
            // tag sentence
            String tags[] = posTagger.tag(tokens);
            // ignore probability
            // double probs[] = posTagger.probs();

            // filter tags
            List<TokenPOS> tokenPOS = new ArrayList<>();
            System.out.println("--------");
            for(var i = 0;i < tags.length; i++) {
                var tag = tags[i];
                if(ALLOWEDPOS.contains(tag))
                    tokenPOS.add(new TokenPOS(tokens[i], tag));
                else System.out.println(tokens[i]+" - "+tags[i]);
            }
            return  tokenPOS;
        })
        .toList();
    }

}
