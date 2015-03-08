package serviceprovider.authorFetcher;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class ArticleParserTest {

	@Test
	public void testArticleParsing() {
		ArticleParser ap = new ArticleParser("http://sozcu.com.tr/kategori/yazarlar/yilmaz-ozdil/");
		Set<Article> articles = ap.getArticles();
		assertTrue(articles != null);
	}

}
