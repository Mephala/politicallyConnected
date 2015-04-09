package serviceprovider.authorFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import serviceprovider.authorFetcher.AuthorParser.Author;

class FailedArticleFetcher implements Runnable {

	private final Lock writeLock;
	private final Set<Author> cachedAuthors;
	private final Log logger = LogFactory.getLog(getClass());
	private List<Article> toBeFixedArticles;
	private static final long WAIT_TIME_BETWEEN_ARTICLE_REQUEST_IN_MS = 100;
	private final Map<Article, String> articleToContentUrlMap;
	private AtomicBoolean fafCompletionBoolean;

	protected FailedArticleFetcher(Lock writeLock, Set<Author> cachedAuthors, Map<Article, String> articleToContentUrlMap) {
		super();
		this.writeLock = writeLock;
		this.cachedAuthors = cachedAuthors;
		this.articleToContentUrlMap = articleToContentUrlMap;
		toBeFixedArticles = new ArrayList<>();
		fafCompletionBoolean = new AtomicBoolean(false);
	}

	@Override
	public void run() {
		logger.info("Starting failed article fixing. Waiting time between article request is :" + WAIT_TIME_BETWEEN_ARTICLE_REQUEST_IN_MS);
		for (Article toBeFixedArticle : toBeFixedArticles) {
			String reFetchedContent = refetchContent(toBeFixedArticle);
			if (StringUtils.hasText(reFetchedContent)) {
				logger.info("Successfully fixed failed article:" + toBeFixedArticle + ", updating data accordingly.");
				updateContent(reFetchedContent, toBeFixedArticle);
			}
			sleep(WAIT_TIME_BETWEEN_ARTICLE_REQUEST_IN_MS);
		}
		fafCompletionBoolean.set(true);
	}

	private void sleep(long waitTimeBetweenArticleRequestInMs) {
		try {
			Thread.sleep(waitTimeBetweenArticleRequestInMs);
		} catch (InterruptedException e) {
			logger.error("Thread sleep encountered an error", e);
		}
	}

	private void updateContent(String reFetchedContent, Article toBeFixedArticle) {
		try {
			writeLock.lock();
			logger.info("WriteLock activated for the cachedAuthors object.");
			for (Author cachedAuthor : cachedAuthors) {
				Set<Article> cachedArticles = cachedAuthor.getArticles();
				for (Article article : cachedArticles) {
					if (article.equals(toBeFixedArticle)) {
						logger.info("Found failed article in the cached data. Replacing it with the fixed value.");
						logger.info("Asserting article is failed by checking its content length once again.");
						String cachedArticleContent = article.getContent();
						boolean cachedArticleHasText = StringUtils.hasText(cachedArticleContent);
						boolean cachedArticleHasMissingText = false;
						if (cachedArticleHasText) {
							if (reFetchedContent.length() > cachedArticleContent.length())
								cachedArticleHasMissingText = true;
						}
						if (!cachedArticleHasText || cachedArticleHasMissingText) {
							logger.info("Assertion was success. Previously cached article content has missing data:" + cachedArticleHasMissingText + " , previouslyCachedArticle has no content :" + !cachedArticleHasText
									+ ". CachedArticle is:" + article);
							article.setContent(reFetchedContent);
						} else {
							logger.info("Assertion failed for previously cached content check. Not updating article. Bug fixing is required. Article:" + article + " , and url :" + articleToContentUrlMap.get(article));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Encountered and handled error during article content update.", e);
		} finally {
			writeLock.unlock();
		}
	}

	private String refetchContent(Article toBeFixedArticle) {
		String newContent = null;
		try {
			ContentParser cp = new ContentParser(articleToContentUrlMap.get(toBeFixedArticle));
			newContent = cp.parseArticleContent();
		} catch (Exception e) {
			logger.error("Error handled fixing failed article content for article:" + toBeFixedArticle, e);
		}
		return newContent;
	}

	protected void addArticleToFixList(Article article) {
		toBeFixedArticles.add(article);
	}

	public void reset() {
		toBeFixedArticles.clear();
		fafCompletionBoolean.set(false);
	}

	public void waitToComplete() {
		while (!fafCompletionBoolean.get())
			;
	}

}
