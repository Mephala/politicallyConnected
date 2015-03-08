package serviceprovider.authorFetcher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

class ArticleParser {

	private final Log logger = LogFactory.getLog(getClass());
	private ExecutorService articleExecutors = Executors.newCachedThreadPool();
	private Map<String, Future<String>> articleToContentFuture;
	private Map<String, Date> articleNameToDateMap;
	private Map<String, String> articleNameToUrlMap;
	private static final Locale TR = new Locale("TR", "tr");
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd MMMM yyyy", TR);
	private static final long GET_ARTICLES_TIMEOUT = 60000; // 1min

	private final String url;

	protected ArticleParser(String url) {
		super();
		this.url = url;
		logger.info("Article Parser has been initialized with url:" + url);
		articleToContentFuture = new HashMap<>();
		articleNameToDateMap = new HashMap<>();
		articleNameToUrlMap = new HashMap<>();
	}

	public Set<Article> getArticles() {
		logger.info("Parsing articles for url:" + url);
		Set<Article> allArticles = new HashSet<>();
		try {
			String responseContent = getHtmlContent();
			if (StringUtils.hasText(responseContent)) {
				logger.info("Parsing data for articles...");
				int articleIndex = 0;
				while (articleIndex != -1) {
					articleIndex = responseContent.indexOf("color:#F00;");
					if (articleIndex != -1) {
						responseContent = responseContent.substring(articleIndex);
						int nextTagEnd = responseContent.indexOf(">");
						responseContent = responseContent.substring(nextTagEnd + 1);
						int dateEnd = responseContent.indexOf("</p>");
						String dateAsString = responseContent.substring(0, dateEnd);
						Date articleDate = SDF.parse(dateAsString);
						responseContent = responseContent.substring(dateEnd);
						int articleNameTag = responseContent.indexOf("color:#000;");
						responseContent = responseContent.substring(articleNameTag);
						nextTagEnd = responseContent.indexOf(">");
						responseContent = responseContent.substring(nextTagEnd + 1);
						int articleContentHtmlHrefIndex = responseContent.indexOf("href=\"");
						responseContent = responseContent.substring(articleContentHtmlHrefIndex + 6);
						int quoteEnd = responseContent.indexOf("\"");
						final String articleContentUrl = responseContent.substring(0, quoteEnd);
						nextTagEnd = responseContent.indexOf(">");
						responseContent = responseContent.substring(nextTagEnd + 1);
						int articleNameEndIndex = responseContent.indexOf("</a>");
						String articleName = responseContent.substring(0, articleNameEndIndex);
						articleNameToDateMap.put(articleName, articleDate);
						articleNameToUrlMap.put(articleName, articleContentUrl);
						Future<String> futureArticleContent = articleExecutors.submit(new Callable<String>() {
							@Override
							public String call() throws Exception {
								ContentParser cp = new ContentParser(articleContentUrl);
								return cp.parseArticleContent();
							}
						});
						articleToContentFuture.put(articleName, futureArticleContent);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error occured during fetching article content...", e);
		} finally {
			processResults(allArticles);
		}
		return allArticles;
	}

	private void processResults(Set<Article> allArticles) {
		long start = System.currentTimeMillis();
		long waitToleranceThreshold = start + GET_ARTICLES_TIMEOUT;
		while (articleToContentFuture.size() > 0) {
			if (System.currentTimeMillis() > waitToleranceThreshold) {
				break; // Timeout break;
			}
			Set<String> articleKeys = articleToContentFuture.keySet();
			Iterator<String> articleIterator = articleKeys.iterator();
			while (articleIterator.hasNext()) {
				String articleName = articleIterator.next();
				Future<String> futureArticleContents = articleToContentFuture.get(articleName);
				if (futureArticleContents.isDone()) {
					try {
						logger.info("Future job is done for article:" + articleName);
						String articleContent = futureArticleContents.get();
						Date articleDate = articleNameToDateMap.get(articleName);
						Article article = new Article(articleContent, articleName, articleDate);
						allArticles.add(article);
						articleIterator.remove();
					} catch (InterruptedException e) {
						logger.error("Error during fetching future job.", e);
					} catch (ExecutionException e) {
						logger.error("Error during fetching future job.", e);
					}
				}
			}
		}
	}

	private String getHtmlContent() throws HttpException, IOException {
		logger.info("Connecting remote Url for articles...");
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		httpClient.executeMethod(getMethod);
		String responseContent = getMethod.getResponseBodyAsString();
		logger.info("Remote site responded succesfully for article parsing...");
		return responseContent;
	}

	protected Map<String, String> getArticleNameToUrlMap() {
		return articleNameToUrlMap;
	}
}
