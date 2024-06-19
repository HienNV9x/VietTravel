package com.viettravelbk.extra_object.cuisine;

import java.util.List;											

import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepository extends JpaRepository<Cuisine, Long>{
	List<Cuisine> findByCategoryCode(String categoryCode);			//Get Cuisine theo category
}