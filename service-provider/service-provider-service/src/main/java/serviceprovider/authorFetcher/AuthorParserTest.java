//package serviceprovider.authorFetcher;
//
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.Set;
//
//import mockit.integration.junit4.JMockit;
//
//import org.apache.commons.httpclient.HttpException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.util.CollectionUtils;
//
//import serviceprovider.authorFetcher.AuthorParser.Author;
//
//@RunWith(JMockit.class)
//public class AuthorParserTest {
//
//	@Test
//	public void testGettingAuthorsAsString() throws HttpException, IOException {
//		AuthorParser ap = AuthorParser.getInstance();
//		Set<Author> authors = ap.getAuthors();
//		for (Author author : authors) {
//			assertTrue(!CollectionUtils.isEmpty(author.getArticles()));
//		}
//		System.err.println(authors);
//	}
//
//	@Test
//	public void testParsingDate() throws HttpException, IOException {
//		Locale TR = new Locale("TR", "tr");
//		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", TR);
//		System.err.println(sdf.format(new Date()));
//	}
//
// }
