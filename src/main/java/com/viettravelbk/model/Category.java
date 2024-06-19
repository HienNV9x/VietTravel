package com.viettravelbk.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.viettravelbk.extra_object.cuisine.Cuisine;
import com.viettravelbk.extra_object.intertainment.Intertainment;

import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category extends BaseEntity {
	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@OneToMany(mappedBy = "category")									//Table news-category: n-1
	private List<Room> news = new ArrayList<>();
	
	@OneToMany(mappedBy = "category")									//Table cuisines-category: n-1
	private List<Cuisine> cuisines = new ArrayList<>();
		
	@OneToMany(mappedBy = "category")									//Table intertainments-category: n-1
	private List<Intertainment> intertainments = new ArrayList<>();		
}
