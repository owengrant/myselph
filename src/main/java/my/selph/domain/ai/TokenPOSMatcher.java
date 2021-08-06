package my.selph.domain.ai;


import org.apache.commons.text.similarity.JaroWinklerDistance;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TokenPOSMatcher {

    public List<Long> match(List<TokenPOS> input, List<List<TokenPOS>> knowledge) {
        return knowledge.stream().map(known -> countContainsList(input, known)).toList();
    }

    public long countContainsList(List<TokenPOS> input, List<TokenPOS> knowledge) {
        return input.stream().filter(in -> {
            var tp1 = in.token();
            var pt1 = in.pos();
            var tp2 = "";
            for (var tokenPOS : knowledge) {
                tp2 = tokenPOS.token();
                var pt2 = tokenPOS.pos();
                if(tp1.equals(tp2))
                    return true;
                if(percentageMatcher(tp1, tp2) >= 0.8)
                    return true;
            }
            return false;
        }).count();
    }

    private double percentageMatcher(String word1, String word2) {
        var bonus = 0d;
        var bonusMetric = (Math.abs(word1.length() - word2.length())/10d);
        if(bonusMetric <= 0.3)
            if(word1.contains(word2) || word2.contains(word1))
                bonus =+ 0.8;

        var score = new JaroWinklerDistance().apply(word1, word2);
//        System.out.println();
//        System.out.println(word1);
//        System.out.println(word2);
//        System.out.println(bonusMetric);
//        System.out.println(bonus);
//        System.out.println(score + bonus);
        return score + bonus;
    }

}
