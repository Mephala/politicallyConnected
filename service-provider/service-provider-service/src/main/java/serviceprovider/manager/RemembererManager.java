package serviceprovider.manager;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.provider.common.dto.RemembererDto;
import service.provider.common.exception.InvalidRequestException;
import serviceprovider.dao.RemembererDAO;
import serviceprovider.model.Rememberer;

@Service
public class RemembererManager extends AbstractServiceManager<Rememberer> {

	@Autowired
	private RemembererDAO remembererDao;

	@PostConstruct
	public void initialize() {
		initializeDAO(remembererDao);
	}

	public List<Rememberer> getAllRememberesByUsername(String username) {
		return remembererDao.getAllRememberesByUsername(username);
	}

	public Rememberer createOrFindRememberer(RemembererDto remembererDto) throws InvalidRequestException {
		Rememberer rememberer = null;
		if (remembererDto != null) {
			if (remembererDto.getId() != null) {
				rememberer = findModelById(remembererDto.getId());
				if (rememberer == null)
					throw new InvalidRequestException();
				setRemembererValues(remembererDto, rememberer);
			} else {
				rememberer = new Rememberer();
				setRemembererValues(remembererDto, rememberer);
			}
		}
		return rememberer;
	}

	protected void setRemembererValues(RemembererDto remembererDto, Rememberer rememberer) {
		rememberer.setKey(remembererDto.getKey());
		rememberer.setValue(remembererDto.getValue());
	}

	public RemembererDto createRemembererDto(Rememberer rememberer) {
		RemembererDto remembererDto = null;
		if (rememberer != null) {
			remembererDto = new RemembererDto();
			remembererDto.setKey(rememberer.getKey());
			remembererDto.setId(rememberer.getId());
			remembererDto.setValue(rememberer.getValue());
		}
		return remembererDto;
	}

	public List<RemembererDto> createRemembererDtoList(List<Rememberer> remembererList) {
		List<RemembererDto> remembererDtoList = null;
		if (remembererList != null) {
			remembererDtoList = new ArrayList<>();
			for (Rememberer rememberer : remembererList) {
				remembererDtoList.add(createRemembererDto(rememberer));
			}
		}
		return remembererDtoList;
	}

	public Rememberer getSavedRememberer(RemembererDto remembererWithId) {
		Rememberer r = null;
		if (remembererWithId != null) {
			if (remembererWithId.getId() == null)
				throw new InvalidParameterException();
			r = findModelById(remembererWithId.getId());
		}
		return r;
	}
}
