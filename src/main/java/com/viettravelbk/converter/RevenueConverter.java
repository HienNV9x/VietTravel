package com.viettravelbk.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viettravelbk.dto.RevenueDTO;
import com.viettravelbk.model.Revenue;
import com.viettravelbk.repository.UserRepository;

@Component
public class RevenueConverter {
    @Autowired
    private UserRepository userRepository; 					
	
	public Revenue toEntityRevenue(RevenueDTO dto) {		//Sử dụng overloading (ghi chồng trường hợp sử dụng thêm update)		
		Revenue entity = new Revenue();
		entity.setTitle_room(dto.getTitle_room()); 
		entity.setCategory_room(dto.getCategory_room());
		entity.setIncome(dto.getIncome());
		entity.setQuantity(dto.getQuantity());
		entity.setCuisineLocal(dto.getCuisineLocal());
		entity.setIntertainmentLocal(dto.getIntertainmentLocal());
		entity.setMoveLocal(dto.getMoveLocal());
		return entity;
	}
	
	public RevenueDTO toDTORevenue(Revenue entity) {		//Khi thêm mới 1 entity
		RevenueDTO dto = new RevenueDTO();
		if(entity.getId() != null) {						//response trả về có id
			dto.setId(entity.getId());
		}
		dto.setTitle_room(entity.getTitle_room());			//response trả về có title
		dto.setCategory_room(entity.getCategory_room());
		dto.setIncome(entity.getIncome());
		dto.setQuantity(entity.getQuantity());
		dto.setCreatedDate(entity.getCreatedDate());		//Phải thêm 4 thuộc tính dưới để khi response trả về sẽ có
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setModifiedDate(entity.getModifiedDate());
		dto.setModifiedBy(entity.getModifiedBy());
		dto.setCuisineLocal(entity.getCuisineLocal());
		dto.setIntertainmentLocal(entity.getIntertainmentLocal());
		dto.setMoveLocal(entity.getMoveLocal());
		return dto;
	}
	
	public Revenue toEntityRevenue(RevenueDTO dto, Revenue entity) {			//Dùng trong update
		entity.setTitle_room(dto.getTitle_room());								//categoryCode làm ở service
		entity.setCategory_room(dto.getCategory_room());
		entity.setIncome(dto.getIncome());
		entity.setQuantity(dto.getQuantity());
		entity.setCuisineLocal(dto.getCuisineLocal());
		entity.setIntertainmentLocal(dto.getIntertainmentLocal());
		entity.setMoveLocal(dto.getMoveLocal());
		return entity;
	}
}
