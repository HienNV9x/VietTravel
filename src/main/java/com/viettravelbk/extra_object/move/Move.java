package com.viettravelbk.extra_object.move;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "move")
public class Move {
	@Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="tourist_car")
	private String touristCar;
	
	@Column(name="car")
	private String car;
	
	@Column(name="motorbike")
	private String motorbike;
}
