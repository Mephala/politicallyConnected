package serviceprovider.service.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import service.provider.common.dto.MahalleDto;

@Entity
public class Mahalle implements Cloneable {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "NAME")
	private String name;

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
		return "Mahalle [id=" + id + ", name=" + name + "]";
	}

	protected Mahalle(String name) {
		super();
		this.name = name;
	}

	@Override
	public Mahalle clone() {
		Mahalle clonedMahalle = new Mahalle(getName());
		clonedMahalle.setId(id);
		return clonedMahalle;
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
		Mahalle other = (Mahalle) obj;
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

	protected void setId(Long id) {
		this.id = id;
	}

	public MahalleDto convertToDto() {
		MahalleDto mahalleDto = new MahalleDto();
		mahalleDto.setId(id);
		mahalleDto.setName(name);
		return mahalleDto;
	}

	protected Mahalle() {

	}

}
