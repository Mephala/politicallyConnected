package serviceprovider.dao;

import java.util.List;

public interface DaoInitializer<Model> {
	public void initializeModel(Model model);

	public void initializeModelList(List<Model> list);
}