package serviceprovider.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import serviceprovider.authorFetcher.FetchedArticle;
import serviceprovider.authorFetcher.FetchedAuthor;

public class FetchedAuthorDAO extends AbstractDAO<FetchedAuthor> {

	private static FetchedAuthorDAO instance;

	private FetchedAuthorDAO() {

	}

	public static synchronized FetchedAuthorDAO getInstance() {
		if (instance == null)
			instance = new FetchedAuthorDAO();
		return instance;
	}

	@Override
	public void initializeModel(FetchedAuthor model) {
		Hibernate.initialize(model.getFetchedArticles());
		Hibernate.initialize(model.getFetchedArticles().size());
		Set<FetchedArticle> articles = model.getFetchedArticles();
		for (FetchedArticle fetchedArticle : articles) {
			fetchedArticle.getContent();
			fetchedArticle.getId();
			fetchedArticle.getPublishDate();
			fetchedArticle.getName();			
		}
	}

	@Override
	public FetchedAuthor findModelById(Long id) {
		Session session = createSessionAndBeginTransaction();
		FetchedAuthor author = (FetchedAuthor) session.get(FetchedAuthor.class, id);
		commitTransactionAndCloseSession(session);
		return author;
	}

	@Override
	public List<FetchedAuthor> getAllModelList() {
		List<FetchedAuthor> authors;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(FetchedAuthor.class);
		authors = criteria.list();
		for (FetchedAuthor fetchedAuthor : authors) {
			initializeModel(fetchedAuthor);
		}
		commitTransactionAndCloseSession(session);
		return authors;
	}

	@Override
	public void saveModel(FetchedAuthor model) {
		Session session = createSessionAndBeginTransaction();
		session.saveOrUpdate(model);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(FetchedAuthor model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);
	}

	public void saveArticle(FetchedArticle persistedArticle) {
		Session session = createSessionAndBeginTransaction();
		session.saveOrUpdate(persistedArticle);
		commitTransactionAndCloseSession(session);
	}

}
