package mainApp.test.wordhasher;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mainApp.wordhash.WordHasher;
import mockit.Deencapsulation;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class WordHasherTestCases {

	@Test
	public void testDividingWords() {
		String keyword = "abc";
		List<String> words = new ArrayList<String>();
		words.add(keyword);
		Set<String> dividedWords = Deencapsulation.invoke(new WordHasher(words), "divideKeyword", "abc");
		assertTrue(dividedWords.contains("a"));
		assertTrue(dividedWords.contains("b"));
		assertTrue(dividedWords.contains("c"));
		assertTrue(dividedWords.contains("ab"));
		assertTrue(dividedWords.contains("abc"));
		assertTrue(dividedWords.contains("bc"));
		dividedWords = Deencapsulation.invoke(new WordHasher(words, 8), "divideKeyword", "qwertyuo");
		assertTrue(dividedWords.contains("qwe"));
		assertTrue(dividedWords.contains("ert"));
		assertTrue(dividedWords.contains("yuo"));
		assertTrue(!dividedWords.contains("ouy"));
		assertTrue(dividedWords.contains("wer"));
		assertTrue(dividedWords.contains("rty"));
		assertTrue(!dividedWords.contains("rte"));
		assertTrue(dividedWords.contains("qwertyuo"));
		assertTrue(!dividedWords.contains(""));
		dividedWords = Deencapsulation.invoke(new WordHasher(words), "divideKeyword", "qwertyuo");
		assertTrue(dividedWords.contains("qwe"));
		assertTrue(dividedWords.contains("ert"));
		assertTrue(dividedWords.contains("rtyuo"));
		dividedWords = Deencapsulation.invoke(new WordHasher(words), "divideKeyword", "qwertyuo");
		assertTrue(dividedWords.contains("qwe"));
		assertTrue(dividedWords.contains("ert"));
		assertTrue(dividedWords.contains("rtyuo"));
	}

}
