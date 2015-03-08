package serviceprovider.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import service.provider.common.dto.SchedulerDto;
import service.provider.common.exception.InvalidRequestException;
import serviceprovider.dao.SchedulerDAO;
import serviceprovider.model.Scheduler;

@Service
public class SchedulerManager extends AbstractServiceManager<Scheduler> {

	@Autowired
	private SchedulerDAO schedulerDao;

	@PostConstruct
	public void initialize() {
		initializeDAO(schedulerDao);
	}

	public Scheduler createOrFindRememberer(SchedulerDto schedulerDto) throws InvalidRequestException {
		Scheduler scheduler = null;
		if (schedulerDto != null) {
			if (schedulerDto.getId() != null) {
				scheduler = findModelById(schedulerDto.getId());
				if (scheduler == null)
					throw new InvalidRequestException();
				setRemembererValues(schedulerDto, scheduler);
			} else {
				scheduler = new Scheduler();
				setRemembererValues(schedulerDto, scheduler);
			}
		}
		return scheduler;
	}

	private void setRemembererValues(SchedulerDto schedulerDto, Scheduler scheduler) {
		scheduler.setDate(schedulerDto.getDate());
		scheduler.setDone(schedulerDto.getDone());
		scheduler.setId(scheduler.getId());
		scheduler.setNote(schedulerDto.getNote());
		scheduler.setPriority(schedulerDto.getPriority());

	}

	public SchedulerDto createSchedulerDto(Scheduler scheduler) {
		SchedulerDto schedulerDto = new SchedulerDto();
		schedulerDto.setDate(scheduler.getDate());
		schedulerDto.setDone(scheduler.getDone());
		schedulerDto.setId(scheduler.getId());
		schedulerDto.setNote(scheduler.getNote());
		schedulerDto.setPriority(scheduler.getPriority());
		return schedulerDto;
	}

	public Set<SchedulerDto> createSchedulerDtoSet(List<Scheduler> allSchedulers) {
		Set<SchedulerDto> allSchedulersSet = new HashSet<>();
		if (!CollectionUtils.isEmpty(allSchedulers)) {
			for (Scheduler scheduler : allSchedulers) {
				allSchedulersSet.add(createSchedulerDto(scheduler));
			}
		}
		return allSchedulersSet;
	}

}
