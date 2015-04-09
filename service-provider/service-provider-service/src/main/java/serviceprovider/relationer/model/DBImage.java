package serviceprovider.relationer.model;

import java.sql.Blob;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import serviceprovider.Application;

@Entity
public class DBImage {

	@Id
	@GeneratedValue
	@Column(name = "IMAGE_ID")
	private Long id;

	@Column(name = "IMAGE_KEY", nullable = false)
	private final String imageKey;

	@Column(name = "APPLICATION", nullable = false)
	private Application application;

	@Column(columnDefinition = "LONGBLOB", name = "IMAGE_DATA", nullable = false)
	private Blob imageBlob;

	public DBImage() {
		this.imageKey = UUID.randomUUID().toString();
	}

	public String getImageKey() {
		return imageKey;
	}

	public Blob getImageBlob() {
		return imageBlob;
	}

	public void setImageBlob(Blob imageBlob) {
		this.imageBlob = imageBlob;
	}

	public Long getId() {
		return id;
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
		DBImage other = (DBImage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

}
