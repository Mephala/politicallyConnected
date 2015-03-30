package service.provider.common.request;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;
import service.provider.common.dto.PcaPersonDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SavePcaPersonListRequestDto extends AbstractRequestDto {

	private List<PcaPersonDto> personListToSave;

	public SavePcaPersonListRequestDto() {

	}

	public SavePcaPersonListRequestDto(String savePcaPersonListRequestUri) {
		setRequestUri(savePcaPersonListRequestUri);
	}

	public boolean isValid() {
		return true;
	}

	public synchronized List<PcaPersonDto> getPersonListToSave() {
		return personListToSave;
	}

	public synchronized void setPersonListToSave(List<PcaPersonDto> personListToSave) {
		this.personListToSave = personListToSave;
	}

}
