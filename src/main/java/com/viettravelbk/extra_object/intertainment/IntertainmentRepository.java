package com.viettravelbk.extra_object.intertainment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IntertainmentRepository extends JpaRepository<Intertainment, Long>{
	List<Intertainment> findByCategoryCode(String categoryCode);			//Get Intertainment theo category
}
