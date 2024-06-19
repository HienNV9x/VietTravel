package com.viettravelbk.extra_object.move;

import java.util.List;

public interface IMoveService {
	MoveDTO save(MoveDTO cuisineDTO);						//post + put
	void delete(long[] ids);								//delete
	List<MoveDTO> findAll();								//get
	MoveDTO findById(Long id);								//get from id
	List<MoveDTO> checkAndUpdateMoves(List<Long> moveIds);	//check và update cart
}
