package serviceprovider.authorFetcher;

import static org.junit.Assert.assertTrue;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class ContentParserTest {

	@Test
	public void testParsingContent() {
		String url = "http://sozcu.com.tr/2015/yazarlar/yilmaz-ozdil/fors-710537/";
		ContentParser cp = new ContentParser(url);
		String articleContents = cp.parseArticleContent();
		assertTrue(articleContents != null && articleContents.length() > 300);
	}
}
