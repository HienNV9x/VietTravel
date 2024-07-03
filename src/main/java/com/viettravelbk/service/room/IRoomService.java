package com.viettravelbk.service.room;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.viettravelbk.dto.RoomDTO;
import com.viettravelbk.pageable.RoomOutput;

public interface IRoomService {
	RoomDTO save(RoomDTO roomDTO);												//post và put 
	//NewDTO update(NewDTO newDTO);
	void delete(long[] ids);													//delete
	//List<RoomDTO> findAll(Pageable pageable);									//Lấy các trang cho totalPage
	List<RoomDTO> findAll(); 													//Lấy tất cả, ko phân trang => get all
	int totalItem();
	RoomOutput findByCategoryName(String categoryName, Pageable pageable);		//Tính năng tìm kiếm theo từ khóa
	RoomDTO findById(Long id);													//Lấy room theo id
	List<RoomDTO> checkAndUpdateRooms(List<Long> roomIds);						//Check và update cart
}
