package com.viettravelbk.extra_object.move;

import org.springframework.stereotype.Component;

@Component
public class MoveConverter {
	public Move toEntity(MoveDTO dto) {
		Move entity = new Move();
		entity.setTouristCar(dto.getTouristCar());
		entity.setCar(dto.getCar());
		entity.setMotorbike(dto.getMotorbike());
		return entity;
	}
	
	public MoveDTO toDTO(Move entity) {
		MoveDTO dto = new MoveDTO();
		if(entity.getId() != null) {							//response trả về có id
			dto.setId(entity.getId());
		}
		dto.setTouristCar(entity.getTouristCar());
		dto.setCar(entity.getCar());
		dto.setMotorbike(entity.getMotorbike());
		return dto;
	}
	
	public Move toEntity(MoveDTO dto, Move entity) {
		entity.setTouristCar(dto.getTouristCar());
		entity.setCar(dto.getCar());
		entity.setMotorbike(dto.getMotorbike());
		return entity;
	}
}
