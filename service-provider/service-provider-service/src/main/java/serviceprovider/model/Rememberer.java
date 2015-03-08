package serviceprovider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Rememberer {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String remembererKey;

	@Column(length = 5000)
	private String remembererValue;

	@Column
	private String remembererUsername;

	public String getKey() {
		return remembererKey;
	}

	public void setKey(String key) {
		this.remembererKey = key;
	}

	public String getValue() {
		return remembererValue;
	}

	public void setValue(String value) {
		this.remembererValue = value;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Rememberer [id=" + id + ", remembererKey=" + remembererKey + ", remembererValue=" + remembererValue + ", remembererUsername=" + remembererUsername + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((remembererKey == null) ? 0 : remembererKey.hashCode());
		result = prime * result + ((remembererUsername == null) ? 0 : remembererUsername.hashCode());
		result = prime * result + ((remembererValue == null) ? 0 : remembererValue.hashCode());
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
		Rememberer other = (Rememberer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (remembererKey == null) {
			if (other.remembererKey != null)
				return false;
		} else if (!remembererKey.equals(other.remembererKey))
			return false;
		if (remembererUsername == null) {
			if (other.remembererUsername != null)
				return false;
		} else if (!remembererUsername.equals(other.remembererUsername))
			return false;
		if (remembererValue == null) {
			if (other.remembererValue != null)
				return false;
		} else if (!remembererValue.equals(other.remembererValue))
			return false;
		return true;
	}

}
