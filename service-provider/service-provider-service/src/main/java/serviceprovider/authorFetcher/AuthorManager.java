package serviceprovider.authorFetcher;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import service.provider.common.dto.ArticleDto;
import service.provider.common.dto.AuthorDto;
import serviceprovider.authorFetcher.AuthorParser.Author;

public class AuthorManager {

	private static AuthorManager instance = new AuthorManager();
	private static final int INITIALIZATION_DELAY_IN_SECONDS = 30;
	private static final int CACHE_UPDATE_SECONDS = 60 * 60 * 4; // 4 hours.
	private AuthorParser authorParser;
	private volatile Set<Author> authors;
	private final Log logger = LogFactory.getLog(getClass());

	private AuthorManager() {
		logger.info("Initializing AuthorManager....");
		authorParser = AuthorParser.getInstance();
		logger.info("Initializing Author update thread with initial-delay of " + INITIALIZATION_DELAY_IN_SECONDS + " seconds , refresh-rate of " + CACHE_UPDATE_SECONDS + " seconds.");
		Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				logger.info("Updating authors...");
				updateAuthors();
			}
		}, INITIALIZATION_DELAY_IN_SECONDS, CACHE_UPDATE_SECONDS, TimeUnit.SECONDS);
	}

	public Set<AuthorDto> getAuthors() {
		logger.info("Returning all authors.");
		Set<AuthorDto> authorDtoSet = new HashSet<>();
		for (Author author : authors) {
			authorDtoSet.add(convertAuthorToDto(author));
		}
		return authorDtoSet;
	}

	private AuthorDto convertAuthorToDto(Author author) {
		String name = author.getName();
		Set<ArticleDto> articleDtos = new HashSet<>();
		Set<Article> articles = author.getArticles();
		for (Article article : articles) {
			articleDtos.add(convertToArticleDto(article));
		}
		AuthorDto authorDto = new AuthorDto();
		authorDto.setArticles(articleDtos);
		authorDto.setName(name);
		return authorDto;
	}

	private ArticleDto convertToArticleDto(Article article) {
		String content = article.getContent();
		String name = article.getName();
		Date date = article.getDate();
		ArticleDto retVal = new ArticleDto();
		retVal.setContent(content);
		retVal.setDate(date);
		retVal.setName(name);
		return retVal;
	}

	private void updateAuthors() {
		authors = authorParser.getAuthors();
	}

	public static AuthorManager getInstance() {
		return instance;
	}

}
