package com.viettravelbk.converter;

import java.util.Arrays;											

import org.springframework.beans.factory.annotation.Autowired;		
import org.springframework.stereotype.Component;

import com.viettravelbk.dto.RoomDTO;
import com.viettravelbk.model.Room;
import com.viettravelbk.upload_image.ImageInfo;							
import com.viettravelbk.upload_image.ImageRepository;				

@Component
public class RoomConverter {							//post put delete get
	
    @Autowired
    private ImageRepository imageRepository;
	
	public Room toEntity(RoomDTO dto) {					//Sử dụng overloading (ghi chồng trường hợp sử dụng thêm update)
		Room entity = new Room();
		entity.setTitle(dto.getTitle());
		entity.setContent(dto.getContent());
		entity.setShortDescription(dto.getShortDescription());
		entity.setThumbnail(dto.getThumbnail());
		entity.setRoomService(dto.getRoomService());
		entity.setRoomPoint(dto.getRoomPoint());
		entity.setUserIdLike(dto.getUserIdLike());
		entity.setPrice(dto.getPrice());
		return entity;
	}
	
	public RoomDTO toDTO(Room entity) {					//Khi thêm mới 1 entity
		RoomDTO dto = new RoomDTO();
		if(entity.getId() != null) {					//response trả về có id
			dto.setId(entity.getId());
		}
	    if (entity.getCategory() != null) {
	        dto.setCategoryName(entity.getCategory().getName());	//response trả về name trong category
	    }
		dto.setTitle(entity.getTitle());				//response trả về có title
		dto.setContent(entity.getContent());			//response trả về có content
		dto.setShortDescription(entity.getShortDescription());		//response trả về có shortdescription
		dto.setThumbnail(entity.getThumbnail());		//response trả về có thumbnail
		dto.setPrice(entity.getPrice());
		dto.setRoomService(entity.getRoomService());
		dto.setRoomPoint(entity.getRoomPoint());
		dto.setUserIdLike(entity.getUserIdLike());
		dto.setCreatedDate(entity.getCreatedDate());	//Phải thêm 4 thuộc tính dưới để khi response trả về sẽ có
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setModifiedDate(entity.getModifiedDate());
		dto.setModifiedBy(entity.getModifiedBy());
        if (entity.getImageId() != null) {
            dto.setImageId(entity.getImageId().getId());       // Chỉ lấy ID của ImageInfo
            dto.setImageUrls(Arrays.asList(entity.getImageId().getImageUrls().split(", "))); // Chia chuỗi URL thành danh sách				
        }													   // Lấy URL chứa chuỗi "main" hoặc URL đầu tiên nếu không có
        dto.setImageUrl(getMainImageUrl(entity.getImageId())); // Sử dụng phương thức mới để lấy đường dẫn URL của file ảnh chứa chuỗi "main"
		return dto;
	}
	
    private String getMainImageUrl(ImageInfo imageInfo) {
        if (imageInfo != null) {     
            String[] urls = imageInfo.getImageUrls().split(", ");	// Tách các URL thành mảng 
            String mainImageUrl = Arrays.stream(urls)				// Lấy URL của file ảnh chứa chuỗi "main" (nếu có)
                                        .filter(url -> url.contains("main"))
                                        .findFirst()
                                        .orElse(null);
            if (mainImageUrl != null) {								// Nếu tìm thấy URL chứa chuỗi "main", trả về nó
                return mainImageUrl;
            }
        }
        return null; 												// Trả về null nếu không tìm thấy URL chứa chuỗi "main"
    }
	
	public Room toEntity(RoomDTO dto, Room entity) {				//Dùng trong update
		entity.setTitle(dto.getTitle());							//categoryCode làm ở service
		entity.setContent(dto.getContent());
		entity.setShortDescription(dto.getShortDescription());
		entity.setThumbnail(dto.getThumbnail());
		entity.setRoomService(dto.getRoomService());
		entity.setRoomPoint(dto.getRoomPoint());
		entity.setUserIdLike(dto.getUserIdLike());
		entity.setPrice(dto.getPrice());
		return entity;
	}
}
