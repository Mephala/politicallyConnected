package serviceprovider.service.address;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import service.provider.common.dto.CityDto;
import service.provider.common.dto.IlceDto;

@Entity
public class City implements Cloneable {
	@Transient
	private Set<String> savedIlceNames;

	protected City() {

	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "NAME")
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Ilce> ilceSet;

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + "]";
	}

	protected City(String name) {
		super();
		this.name = name;
		this.ilceSet = new HashSet<>();
		savedIlceNames = new HashSet<>();
	}

	protected void addIlce(Ilce ilce) {
		this.ilceSet.add(ilce);
		savedIlceNames.add(ilce.getName());
	}

	protected boolean containsIlceWithName(String ilceName) {
		return savedIlceNames.contains(ilceName);
	}

	protected Set<Ilce> getIlceSet() {
		return ilceSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		City other = (City) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public City clone() {
		City clonedCity = new City(getName());
		clonedCity.setId(id);
		for (Ilce ilce : ilceSet) {
			clonedCity.addIlce(ilce.clone());
		}
		return clonedCity;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public CityDto convertToDto() {
		CityDto cityDto = new CityDto();
		cityDto.setId(id);
		cityDto.setName(name);
		List<IlceDto> ilceDtoList = new ArrayList<>();
		for (Ilce ilce : ilceSet) {
			ilceDtoList.add(ilce.convertToDto());
		}
		cityDto.setIlceList(ilceDtoList);
		return cityDto;
	}

}
