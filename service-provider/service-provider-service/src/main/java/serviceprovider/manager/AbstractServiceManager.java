package serviceprovider.manager;

import java.util.List;

import service.provider.common.exception.DatabaseCorruptedException;
import serviceprovider.dao.AbstractDAO;

public abstract class AbstractServiceManager<T> {

	protected AbstractDAO<T> dao;

	protected final void initializeDAO(AbstractDAO<T> dao) {
		this.dao = dao;
	}

	public final T findModelById(Long id) {
		return dao.findModelById(id);
	}

	public final List<T> getAllModelList() {
		return dao.getAllModelList();
	}

	public final void saveModel(T model) {
		if (model != null)
			dao.saveModel(model);
	}

	public void deleteModel(T model) throws DatabaseCorruptedException {
		dao.deleteModel(model);
	}
}