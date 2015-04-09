package serviceprovider.authorFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import serviceprovider.dao.FetchedAuthorDAO;

class AuthorParser {
	private static AuthorParser instance = null;
	private Log logger = LogFactory.getLog(getClass());
	private Map<String, Future<Set<Article>>> authorToArticlesFuture;
	private ExecutorService authorExecutors = Executors.newCachedThreadPool();
	private Map<Article, String> articleToContentUrlMap;
	private static final long GET_AUTHORS_TIMEOUT = 300000; // 5min
	private Set<Author> fixedAuthors;
	private final Lock readLock;
	private final Lock writeLock;
	private FetchedAuthorDAO authorDAO;

	private AuthorParser() {
		authorToArticlesFuture = new HashMap<String, Future<Set<Article>>>();
		fixedAuthors = new HashSet<>();
		ReadWriteLock rwLock = new ReentrantReadWriteLock();
		writeLock = rwLock.writeLock();
		readLock = rwLock.readLock();
		articleToContentUrlMap = new HashMap<>();
		authorDAO = FetchedAuthorDAO.getInstance();
		initializeDBData();
	}

	private void initializeDBData() {
		List<FetchedAuthor> fetchedAuthors = authorDAO.getAllModelList();
		if (!CollectionUtils.isEmpty(fetchedAuthors)) {
			for (FetchedAuthor fetchedAuthor : fetchedAuthors) {
				Set<FetchedArticle> articles = fetchedAuthor.getFetchedArticles();
				Set<Article> articleSet = new HashSet<>();
				for (FetchedArticle fetchedArticle : articles) {
					Article article = new Article(fetchedArticle.getContent(), fetchedArticle.getName(), fetchedArticle.getPublishDate());
					articleSet.add(article);
					articleToContentUrlMap.put(article, fetchedArticle.getUrl());
				}
				Author author = new Author(articleSet, fetchedAuthor.getName());
				fixedAuthors.add(author);
			}
		}
	}

	public static synchronized AuthorParser getInstance() {
		if (instance == null)
			instance = new AuthorParser();
		return instance;
	}

	public Set<Author> getAuthors() {
		long startAuthor = System.currentTimeMillis();
		logger.info("Getting author list...");
		Set<Author> authors = new HashSet<>();
		FailedArticleFetcher faf = new FailedArticleFetcher(writeLock, fixedAuthors, articleToContentUrlMap);
		try {
			String responseContent = getHtmlContent();
			if (StringUtils.hasText(responseContent)) {
				logger.info("Parsing data...");
				int authorIndex = 0;
				while (authorIndex != -1) {
					authorIndex = responseContent.indexOf("yazarlaric yazic");
					if (authorIndex != -1) {
						responseContent = responseContent.substring(authorIndex);
						int endOfFirstATag = responseContent.indexOf("/a>");
						responseContent = responseContent.substring(endOfFirstATag);
						int aTagIndex = responseContent.indexOf("<a href=");
						responseContent = responseContent.substring(aTagIndex);
						int endOfAIndex = responseContent.indexOf(">");
						responseContent = responseContent.substring(endOfAIndex + 1);
						int endOfAuthorATag = responseContent.indexOf("</a>");
						final String authorName = responseContent.substring(0, endOfAuthorATag);
						responseContent = responseContent.substring(endOfAuthorATag + 5);
						endOfFirstATag = responseContent.indexOf("/a>");
						responseContent = responseContent.substring(endOfFirstATag);
						aTagIndex = responseContent.indexOf("<a href=");
						responseContent = responseContent.substring(aTagIndex);
						int hrefIndex = responseContent.indexOf("href=\"");
						responseContent = responseContent.substring(hrefIndex + 6);
						int endOfHrefIndex = responseContent.indexOf("\"");
						final String articleUrl = responseContent.substring(0, endOfHrefIndex);
						Future<Set<Article>> articlesFuture = authorExecutors.submit(new Callable<Set<Article>>() {
							@Override
							public Set<Article> call() throws Exception {
								ArticleParser ap = new ArticleParser(articleUrl);
								Map<String, String> articleNameToUrlMap = ap.getArticleNameToUrlMap();
								Set<Article> parsedArticles = ap.getArticles();
								for (Article parsedArticle : parsedArticles) {
									String url = articleNameToUrlMap.get(parsedArticle.getName());
									articleToContentUrlMap.put(parsedArticle, url);
								}
								return parsedArticles;
							}
						});
						authorToArticlesFuture.put(authorName, articlesFuture);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error occured during author parsing process...", e);
		} finally {
			processResults(authors);
		}
		handleCacheUpdate(authors);
		fixFailedContent(authors, faf); // For newly fetched data.
		faf.waitToComplete();
		faf.reset();
		fixFailedContent(fixedAuthors, faf);
		faf.waitToComplete();
		Set<Author> copiedAuthors = copyCachedData();
		authorExecutors.submit(new Runnable() {
			@Override
			public void run() {
				logger.info("Creating async job to persist freshly analyzed author data.");
				persistFixedData();
			}
		});
		long authorParseEnd = System.currentTimeMillis();
		long differ = authorParseEnd - startAuthor;
		logger.info("AUTHORS ARE PARSED WITH ALL CONTENT IN " + differ + " MS.");
		return copiedAuthors;
	}

	private void persistFixedData() {
		logger.info("Persisting authors if they are not already persisted.");
		Set<FetchedAuthor> authorListToBeSaved = new HashSet<>();
		List<FetchedAuthor> freshAuthorList = convertAuthorObjectsToDAO();
		List<FetchedAuthor> persistedAuthorList = authorDAO.getAllModelList();
		if (CollectionUtils.isEmpty(persistedAuthorList)) {
			for (FetchedAuthor fetchedAuthor : freshAuthorList) {
				authorListToBeSaved.add(fetchedAuthor);
			}
		} else {
			for (FetchedAuthor persistedAuthor : persistedAuthorList) {
				String name = persistedAuthor.getName();
				for (FetchedAuthor freshAuthor : freshAuthorList) {
					if (freshAuthor.getName().equals(name)) {
						Set<FetchedArticle> freshArticles = freshAuthor.getFetchedArticles();
						Set<FetchedArticle> persistedArticles = persistedAuthor.getFetchedArticles();
						for (FetchedArticle persistedArticle : persistedArticles) {
							String persistedArticleName = persistedArticle.getName();
							for (FetchedArticle freshArticle : freshArticles) {
								String freshArticleName = freshArticle.getName();
								if (freshArticleName.equals(persistedArticleName)) {
									if (freshArticle.getContent().length() > persistedArticle.getContent().length()) {
										logger.info("Difference in content is found between newly fetched article and persisted article. Article name :" + freshArticleName);
										logger.info("Changing db article's content...");
										persistedArticle.setContent(freshArticle.getContent());
										authorListToBeSaved.add(persistedAuthor);
									}
								}
							}
						}
					} else {
						boolean isFetchedAuthorNew = true;
						String freshAuthorName = freshAuthor.getName();
						for (FetchedAuthor persistedAuthor2 : persistedAuthorList) {
							if (persistedAuthor2.getName().equals(freshAuthorName))
								isFetchedAuthorNew = false;
						}
						if (isFetchedAuthorNew)
							authorListToBeSaved.add(freshAuthor);
					}
				}
			}
		}
		for (FetchedAuthor fetchedAuthor : authorListToBeSaved) {
			logger.info("Author is being saved to DB....");
			authorDAO.saveModel(fetchedAuthor);
		}
	}

	private List<FetchedAuthor> convertAuthorObjectsToDAO() {
		List<FetchedAuthor> authorSetToBePersisted = new ArrayList<>();
		for (Author author : fixedAuthors) {
			Set<Article> articles = author.getArticles();
			Set<FetchedArticle> fetchedArticles = new HashSet<>();
			for (Article article : articles) {
				FetchedArticle fArticle = new FetchedArticle();
				fArticle.setContent(article.getContent());
				fArticle.setName(article.getName());
				fArticle.setPublishDate(article.getDate());
				fArticle.setUrl(articleToContentUrlMap.get(article));
				fetchedArticles.add(fArticle);
			}
			FetchedAuthor fAuthor = new FetchedAuthor();
			fAuthor.setName(author.getName());
			fAuthor.setFetchedArticles(fetchedArticles);
			authorSetToBePersisted.add(fAuthor);
		}
		return authorSetToBePersisted;
	}

	private Set<Author> copyCachedData() {
		logger.info("Copying cached data.");
		Set<Author> copyOfCache = new HashSet<>();
		try {
			readLock.lock();
			for (Author author : fixedAuthors) {
				Set<Article> articles = author.getArticles();
				Set<Article> copyOfArticles = new HashSet<>();
				for (Article article : articles) {
					Article copiedArticle = new Article(article.getContent(), article.getName(), article.getDate());
					copyOfArticles.add(copiedArticle);
				}
				Author copiedAuthor = new Author(copyOfArticles, author.getName());
				copyOfCache.add(copiedAuthor);
			}
		} catch (Exception e) {
			logger.info("Error occured during data copying.", e);
		} finally {
			readLock.unlock();
		}
		return copyOfCache;
	}

	private void fixFailedContent(Set<Author> authors, FailedArticleFetcher faf) {
		logger.info("Creating fixer object for fix job.");
		for (Author author : authors) {
			Set<Article> fetchedArticles = author.getArticles();
			for (Article article : fetchedArticles) {
				if (!StringUtils.hasText(article.getContent())) {
					logger.info("No content found for article:" + article + " . Starting fixer thread to re-fetch corresponding data.");
					faf.addArticleToFixList(article);
				}
			}
		}
		logger.info("Starting fixer job.");
		authorExecutors.submit(faf);
	}

	private boolean isCachedAuthorsEmpty() {
		logger.info("Checking cached author emptyness..");
		boolean isEmpty = true;
		try {
			logger.info("Locking cached authors(rl).");
			readLock.lock();
			isEmpty = fixedAuthors.size() == 0;
		} catch (Exception e) {
			logger.info("Error handled during cachedAuther size check. Error:" + e.getLocalizedMessage());
		} finally {
			readLock.unlock();
		}
		return isEmpty;
	}

	private void handleCacheUpdate(Set<Author> authors) {
		if (isCachedAuthorsEmpty()) {
			setUpAuthorsToCachedAuthors(authors);
		} else {
			logger.info("Calculating differences in newly fetched authors and previously cached ones.");
			try {
				logger.info("WriteLock is locked for updating cached authors.");
				writeLock.lock();
				for (Author newlyFetchedAuthor : authors) {
					if (fixedAuthors.contains(newlyFetchedAuthor)) {
						for (Author cachedAuthor : fixedAuthors) {
							if (cachedAuthor.equals(newlyFetchedAuthor)) {
								// Authors might be equal yet articles are tend to differ!
								Set<Article> cachedArticles = cachedAuthor.getArticles();
								Set<Article> fetchedArticles = newlyFetchedAuthor.getArticles();
								for (Article fetchedArticle : fetchedArticles) {
									replaceContentIfNecessary(cachedArticles, fetchedArticle);
								}
							}
						}
					} else {
						fixedAuthors.add(newlyFetchedAuthor);
					}
				}
				logger.info("Cache update is completed successfully.");
			} catch (Exception e) {
				logger.error("Handled error during cache update. This is not good because without cache update people will see previously cached data.", e);
			} finally {
				logger.info("Unlocking write lock.");
				writeLock.unlock();
			}
		}
	}

	private void replaceContentIfNecessary(Set<Article> cachedArticles, Article fetchedArticle) {
		if (cachedArticles.contains(fetchedArticle)) {
			for (Article cachedArticle : cachedArticles) {
				if (fetchedArticle.equals(cachedArticle)) {
					// Name of the article might be same but contents may vary. Bigger content is what we are after.
					if (fetchedArticle.getContent().length() > cachedArticle.getContent().length()) {
						logger.info("Content difference is detected. Replacing old content with the new and BIGGER one! Changing article with name:" + cachedArticle.getName());
						cachedArticle.setContent(fetchedArticle.getContent());
					}
				}
			}
		} else {
			cachedArticles.add(fetchedArticle);
		}
	}

	private void setUpAuthorsToCachedAuthors(Set<Author> authors) {
		logger.info("Implementing cached authors for the first time.");
		try {
			logger.info("Creating write lock for author caching.");
			writeLock.lock();
			for (Author author : authors) {
				fixedAuthors.add(author);
			}
		} catch (Exception e) {
			logger.info("Error occured during setting calculated authors into cached authors. Error: " + e.getLocalizedMessage());
		} finally {
			writeLock.unlock();
		}
	}

	private void processResults(Set<Author> authors) {
		long start = System.currentTimeMillis();
		long waitToleranceThreshold = start + GET_AUTHORS_TIMEOUT;
		while (authorToArticlesFuture.size() > 0) {
			if (System.currentTimeMillis() > waitToleranceThreshold) {
				break; // Timeout break;
			}
			Set<String> authorKeys = authorToArticlesFuture.keySet();
			Iterator<String> authorIterator = authorKeys.iterator();
			while (authorIterator.hasNext()) {
				String authorName = authorIterator.next();
				Future<Set<Article>> futureArticles = authorToArticlesFuture.get(authorName);
				if (futureArticles.isDone()) {
					try {
						logger.info("Future job is done for author:" + authorName);
						Set<Article> articles = futureArticles.get();
						authors.add(new Author(articles, authorName));
						authorIterator.remove();
					} catch (InterruptedException e) {
						logger.error("Error during fetching future job.", e);
					} catch (ExecutionException e) {
						logger.error("Error during fetching future job.", e);
					}
				}
			}
		}
	}

	private String getHtmlContent() throws IOException, HttpException {
		logger.info("Connecting remote Url for authors...");
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://sozcu.com.tr/kategori/yazarlar/");
		httpClient.executeMethod(getMethod);
		String responseContent = getMethod.getResponseBodyAsString();
		logger.info("Remote site responded succesfully...");
		return responseContent;
	}

	public class Author {

		private final Set<Article> articles;
		private final String name;

		private Author(Set<Article> articles, String name) {
			super();
			this.articles = articles;
			this.name = name;
		}

		public Set<Article> getArticles() {
			return articles;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "Author [articles=" + articles + ", name=" + name + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Author other = (Author) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private AuthorParser getOuterType() {
			return AuthorParser.this;
		}
	}

}
