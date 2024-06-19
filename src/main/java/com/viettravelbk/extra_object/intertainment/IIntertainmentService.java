package com.viettravelbk.extra_object.intertainment;

import java.util.List;

public interface IIntertainmentService {
	IntertainmentDTO save(IntertainmentDTO intertainmentDTO);							//post + put
	void delete(long[] ids);															//delete
	List<IntertainmentDTO> findAll();													//get
	IntertainmentDTO findById(Long id);													//get from id
	List<IntertainmentDTO> checkAndUpdateIntertainments(List<Long> intertainmentIds);	//check và update intertainment
}
