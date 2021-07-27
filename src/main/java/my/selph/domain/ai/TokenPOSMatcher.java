package my.selph.domain.ai;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TokenPOSMatcher {

    public int match(List<TokenPOS> input, List<List<TokenPOS>> knowledge) {
        var inputTokens = input.stream().map(TokenPOS::token).toList();
        var scores = knowledge.stream().map(known -> {
            var knownTokens = known.stream().map(TokenPOS::token).toList();
            return countContainsList(inputTokens, knownTokens);
        }).toList();

        var highestScoreIndex = 0;
        for(var i = 0; i < scores.size(); i++) {
            if (scores.get(highestScoreIndex) < scores.get(i))
                highestScoreIndex = i;
        }
        return highestScoreIndex;
    }

    public long countContainsList(List<String> list1, List<String> list2) {
        return list1.stream().filter(l -> list2.contains(l)).count();
    }

}
