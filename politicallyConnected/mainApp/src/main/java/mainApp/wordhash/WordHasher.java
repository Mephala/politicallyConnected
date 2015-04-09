package mainApp.wordhash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public WordHasher(List<String> wordList) {
		super();
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

	private void startPresearch() {
		prepareKeywords();
		completeSearchResults();
	}

	private void completeSearchResults() {
		for (String keyword : allDistinctPossibleSearches) {
			Set<String> resultStringSetForKeyword = new HashSet<String>();
			for (String possibleSearchSet : searchSet) {
				if (possibleSearchSet != null && possibleSearchSet.contains(keyword))
					resultStringSetForKeyword.add(possibleSearchSet);
			}
			keywordToSearchResultMap.put(keyword, resultStringSetForKeyword);
		}
	}

	private void prepareKeywords() {
		for (String keyword : searchSet) {
			Set<String> dividedKeywords = divideKeyword(keyword);
			allDistinctPossibleSearches.addAll(dividedKeywords);
		}
	}

	private Set<String> divideKeyword(String keyword) {
		Set<String> dividedKeyWords = new HashSet<String>();
		if (keyword != null && (keyword.length() != 0)) {
			int startIndex = 0;
			while (startIndex <= keyword.length() - 1) {
				int pointerIndex = startIndex + 1;
				while (pointerIndex <= keyword.length()) {
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
