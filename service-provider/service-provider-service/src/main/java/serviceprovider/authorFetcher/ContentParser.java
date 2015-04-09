package serviceprovider.authorFetcher;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContentParser {
	private final Log logger = LogFactory.getLog(getClass());
	private final String url;

	protected ContentParser(String url) {
		super();
		this.url = url;
	}

	public String parseArticleContent() {
		logger.info("Parsing content for url:" + url);
		String articleContent = "";
		try {
			String responseContent = getHtmlContent();
			int contentStartIndex = responseContent.indexOf("<h2></h2>");
			if (contentStartIndex != -1) {
				responseContent = responseContent.substring(contentStartIndex + 9);
				int contentEndIndex = responseContent.indexOf("<!-- content bitiş-->");
				articleContent = responseContent.substring(0, contentEndIndex);
			}
		} catch (Exception e) {
			logger.error("Error getting article contents...", e);
		}
		return articleContent;
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

}
