package com.viettravelbk.upload_image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image")
public class ImageInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@Column(name = "image_name")
	private String name;

	//@Column(name = "image_url")
	private String url;
	
	@Column(name = "image_name")								//Gộp nhiều ảnh
    private String imageNames; 									//Để lưu tên của tất cả ảnh
    
	@Column(name = "image_url")									//Gộp nhiều ảnh
    private String imageUrls; 									//Để lưu URL của tất cả ảnh

	public ImageInfo() {

	}
	public ImageInfo(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getImageNames() {								//Gộp nhiều ảnh
		return this.imageNames;	
	}
	public void setImageNames(String imageNames) {				//Gộp nhiều ảnh
		this.imageNames = imageNames;
	}
	
	public String getImageUrls() {								//Gộp nhiều ảnh
		return this.imageUrls;
	}
	public void setImageUrls(String imageUrls) {				//Gộp nhiều ảnh
		this.imageUrls = imageUrls;
	}
}
