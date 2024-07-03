package com.viettravelbk.extra_object.cuisine;

import javax.persistence.CascadeType;
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
@Table(name = "cuisine")
public class Cuisine {
	@Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="title_cuisine")
	private String titleCuisine;
	
	@Column(name="content_cuisine")
	private String contentCuisine;
	
	@Column(name="location_cuisine")
	private String locationCuisine;
	
	@Column(name="thumbnail_cuisine")
	private String thumbnailCuisine;
	
    @ManyToOne										//Table cuisines - category: n-1					
    @JoinColumn(name = "category_id") 				
    private Category category;
    
    @OneToOne(cascade = CascadeType.REMOVE)			//Bản ghi ImageInfo sẽ xóa cùng bản ghi Cuisine								
    @JoinColumn(name = "image_id")					//Table cuisine - image: 1-1
    private ImageInfo imageId;
}
