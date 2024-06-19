package com.viettravelbk.extra_object.intertainment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.viettravelbk.model.Category;
import com.viettravelbk.upload_image.ImageInfo;

import lombok.Data;

@Data
@Entity
@Table(name = "intertainment")
public class Intertainment {
	@Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="title_inter")
	private String titleInter;
	
	@Column(name="content_inter")
	private String contentInter;
	
	@Column(name="location_inter")
	private String locationInter;
	
	@Column(name="thumbnail_inter")
	private String thumbnailInter;
	
    @ManyToOne										//Table intertainments - category: n-1					
    @JoinColumn(name = "category_id") 				
    private Category category;
    
    @OneToOne										//Table entertainment - image: 1-1
    @JoinColumn(name = "image_id")
    private ImageInfo imageId;
}
