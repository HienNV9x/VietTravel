package com.viettravelbk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "revenue")
public class Revenue extends BaseEntity{
	@Column(name="title_room")
	private String title_room;
	
	@Column(name="category_room")
	private String category_room;
	
	@Column(name="income")
	private double income;
	
    @Column(name="quantity") 					//Cột biểu diễn số lượng title_room
    private int quantity; 
    
    @Column(name="cuisine_local")
    private String cuisineLocal;
    
    @Column(name="intertainment_local")
    private String intertainmentLocal;
    
    @Column(name="move_local")
    private String moveLocal;
}
