package serviceprovider.relationer.manager;

import serviceprovider.manager.AbstractServiceManager;
import serviceprovider.relationer.dao.DBImageDAO;
import serviceprovider.relationer.model.DBImage;

public class ImageManager extends AbstractServiceManager<DBImage> {

	private static ImageManager instance;
	private DBImageDAO dao;

	private ImageManager() {
		dao = DBImageDAO.getInstance();
		initializeDAO(dao);
	}

	public static synchronized ImageManager getInstance() {
		if (instance == null)
			instance = new ImageManager();
		return instance;
	}

}
