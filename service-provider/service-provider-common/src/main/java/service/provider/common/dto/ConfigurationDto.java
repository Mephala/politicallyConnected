package service.provider.common.dto;

public class ConfigurationDto {

	private String key;
	private Long id;
	private String value;

	public ConfigurationDto() {

	}

	public String getKey() {
		return key;
	}

	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ConfigurationDto other = (ConfigurationDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public ConfigurationDto(String key, Long id, String value) {
		super();
		this.key = key;
		this.id = id;
		this.value = value;
	}

	@Override
	public String toString() {
		return "ConfigurationDto [key=" + key + ", id=" + id + ", value=" + value + "]";
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
