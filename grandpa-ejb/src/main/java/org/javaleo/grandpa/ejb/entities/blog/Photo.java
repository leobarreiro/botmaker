package org.javaleo.grandpa.ejb.entities.blog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.javaleo.grandpa.ejb.entities.EntityUtils;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "photo")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "photo_sq", sequenceName = "photo_seq", initialValue = 1, allocationSize = 1)
public class Photo implements IEntityBasic {

	private static final long serialVersionUID = 5679551260780321462L;

	private Long id;
	private String uuid;
	private String mimeType;
	private String name;
	private String description;
	private String hash;
	private Date created;
	private Date lastUploaded;
	private User uploader;
	private Gallery gallery;
	private byte[] content;

	@Override
	@Id
	@Column(name = "photo_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_sq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "uuid", length = 32)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "mime_type", length = 30)
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Column(name = "name", length = 60)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", columnDefinition = "text", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "hash", length = 32)
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_uploaded")
	public Date getLastUploaded() {
		return lastUploaded;
	}

	public void setLastUploaded(Date lastUploaded) {
		this.lastUploaded = lastUploaded;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uploader_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "photo_fk_user"))
	public User getUploader() {
		return uploader;
	}

	public void setUploader(User uploader) {
		this.uploader = uploader;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "gallery_id", referencedColumnName = "gallery_id", foreignKey = @ForeignKey(name = "photo_fk_gallery"))
	public Gallery getGallery() {
		return gallery;
	}

	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

	@Transient
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((gallery == null) ? 0 : gallery.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastUploaded == null) ? 0 : lastUploaded.hashCode());
		result = prime * result
				+ ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((uploader == null) ? 0 : uploader.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Photo other = (Photo) obj;
		if (created == null) {
			if (other.created != null) return false;
		} else if (!created.equals(other.created)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description)) return false;
		if (gallery == null) {
			if (other.gallery != null) return false;
		} else if (!gallery.equals(other.gallery)) return false;
		if (hash == null) {
			if (other.hash != null) return false;
		} else if (!hash.equals(other.hash)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (lastUploaded == null) {
			if (other.lastUploaded != null) return false;
		} else if (!lastUploaded.equals(other.lastUploaded)) return false;
		if (mimeType == null) {
			if (other.mimeType != null) return false;
		} else if (!mimeType.equals(other.mimeType)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (uploader == null) {
			if (other.uploader != null) return false;
		} else if (!uploader.equals(other.uploader)) return false;
		if (uuid == null) {
			if (other.uuid != null) return false;
		} else if (!uuid.equals(other.uuid)) return false;
		return true;
	}

}
