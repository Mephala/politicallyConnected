package mainApp.wordhash;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Hashes list of strings for all possible search keywords. High memory usage, fastest possible string search for low threshold constructions.
 * 
 * @author Gokhanozg
 *
 */
public class WordHasher {

	private final Set<String> searchSet;
	private final Map<String, Set<String>> keywordToSearchResultMap;
	private final Set<String> allDistinctPossibleSearches;
	private Log logger = LogFactory.getLog(getClass());
	private final BigDecimal HUNDRED = new BigDecimal(100);
	private final int PERCENTAGE_PRECISION = 3;
	private final static int DEFAULT_HASH_LIMIT = 5;
	private final int hashLimit;

	/**
	 * Constructs hasher for given list. Expensive operation. For instance ( List = {"Gokhan Ozgozen" , "Sabanci University"} , threshold = 2 ) construction hashes all keywords with 2 character at
	 * most. Like : "Go" , "ok", "ha" , "an"
	 * 
	 * @param wordList
	 *            List of strings containing words in which you want to search through later.
	 * @param threshold
	 *            Number of characters in which you want to look through all strings if it contains or not.
	 * 
	 * 
	 */
	public WordHasher(List<String> wordList, int hashLimit) {
		super();
		this.hashLimit = hashLimit;
		this.keywordToSearchResultMap = new HashMap<String, Set<String>>();
		this.allDistinctPossibleSearches = new HashSet<String>();
		this.searchSet = new HashSet<String>();
		if (wordList != null) {
			for (String word : wordList) {
				searchSet.add(word);
			}
			startPresearch();
		}
	}

	/**
	 * Call {@link #WordHasher(wordList,hashLimit) constructor with hashlimit } if you need to configure its search speed. Be careful, this class is very expensive in terms of memory usage.
	 */
	public WordHasher(List<String> wordList) {
		this(wordList, DEFAULT_HASH_LIMIT);
	}

	private void startPresearch() {
		logger.info("Preparing keywords...");
		prepareKeywords();
		logger.info("Preparing keywords job is finished. Now completing pre-search hashing.");
		completeSearchResults();
	}

	private void completeSearchResults() {
		BigDecimal totalSearches = new BigDecimal(allDistinctPossibleSearches.size());
		int completedPresearch = 0;
		for (String keyword : allDistinctPossibleSearches) {
			Set<String> resultStringSetForKeyword = new HashSet<String>();
			for (String possibleSearchSet : searchSet) {
				if (possibleSearchSet != null && possibleSearchSet.contains(keyword))
					resultStringSetForKeyword.add(possibleSearchSet);
			}
			keywordToSearchResultMap.put(keyword, resultStringSetForKeyword);
			completedPresearch++;
			BigDecimal percentage = new BigDecimal(completedPresearch).multiply(HUNDRED).divide(totalSearches, PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_UP);
			logger.info("Completed pre-search hashing of " + percentage.toPlainString() + " %.");
		}
	}

	private void prepareKeywords() {
		BigDecimal totalValue = new BigDecimal(searchSet.size());
		int completedKeywords = 0;
		for (String keyword : searchSet) {
			Set<String> dividedKeywords = divideKeyword(keyword);
			allDistinctPossibleSearches.addAll(dividedKeywords);
			completedKeywords++;
			BigDecimal percentage = new BigDecimal(completedKeywords).multiply(HUNDRED).divide(totalValue, PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_UP);
			logger.info("Completed keyword hashing of " + percentage.toPlainString() + " %.");
		}
	}

	private Set<String> divideKeyword(String keyword) {
		Set<String> dividedKeyWords = new HashSet<String>();
		if (keyword != null && (keyword.length() != 0)) {
			int startIndex = 0;
			while (startIndex <= keyword.length() - 1) {
				int pointerIndex = startIndex + 1;
				while (pointerIndex <= keyword.length() && (pointerIndex - startIndex <= hashLimit)) {
					String searchKeyWord = keyword.substring(startIndex, pointerIndex);
					dividedKeyWords.add(searchKeyWord);
					pointerIndex++;
				}
				startIndex++;
			}
		}
		return dividedKeyWords;
	}

	public Set<String> search(String keyword) {
		Set<String> clonedSet = new HashSet<String>();
		Set<String> answerSet = keywordToSearchResultMap.get(keyword);
		if (answerSet != null) {
			for (String answer : answerSet) {
				if (answer != null)
					clonedSet.add(answer);
			}
		}
		return clonedSet;
	}

}
