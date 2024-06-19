package com.viettravelbk.extra_object.cuisine;

import java.util.List;

public interface ICuisineService {
	CuisineDTO save(CuisineDTO cuisineDTO);								//post + put
	void delete(long[] ids);											//delete
	List<CuisineDTO> findAll();											//get
	CuisineDTO findById(Long id);										//get from id
	List<CuisineDTO> checkAndUpdateCuisines(List<Long> cuisineIds);		//check và update Cuisine
}
