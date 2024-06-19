package com.viettravelbk.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viettravelbk.dto.CommentDTO;
import com.viettravelbk.model.Comment;
import com.viettravelbk.model.Room;
import com.viettravelbk.model.User;
import com.viettravelbk.repository.RoomRepository;
import com.viettravelbk.repository.UserRepository;

@Component
public class CommentConverter {
    @Autowired
    private UserRepository userRepository; 					// Tiêm UserRepository
    
    @Autowired
    private RoomRepository roomRepository;
	
	public Comment toEntityComment(CommentDTO dto) {		//Sử dụng overloading (ghi chồng trường hợp sử dụng thêm update)		
		Comment entity = new Comment();
		entity.setText(dto.getText());       
        if(dto.getUserId() != null) {						//Xử lý thiết lập User
            User user = userRepository.findById(dto.getUserId()).orElseThrow(
                    () -> new RuntimeException("User not found with id: " + dto.getUserId())
                );
                entity.setUser(user);
        }
        if(dto.getRoomId() != null) {						//Xử lý thiết lập User
            Room room = roomRepository.findById(dto.getRoomId()).orElseThrow(
                    () -> new RuntimeException("Room not found with id: " + dto.getRoomId())
                );
                entity.setRoom(room);
        }
		return entity;
	}
	
	public CommentDTO toDTOComment(Comment entity) {		//Khi thêm mới 1 entity
		CommentDTO dto = new CommentDTO();
		if(entity.getId() != null) {						//response trả về có id
			dto.setId(entity.getId());
		}
		dto.setText(entity.getText());						//response trả về có title
		dto.setCreatedDate(entity.getCreatedDate());		//Phải thêm 4 thuộc tính dưới để khi response trả về sẽ có
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setModifiedDate(entity.getModifiedDate());
		dto.setModifiedBy(entity.getModifiedBy());
		return dto;
	}
	
	public Comment toEntityComment(CommentDTO dto, Comment entity) {  //Dùng trong update
		entity.setText(dto.getText());								  //categoryCode làm ở service
		return entity;
	}
}
