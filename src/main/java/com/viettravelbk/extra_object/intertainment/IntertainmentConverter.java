package com.viettravelbk.extra_object.intertainment;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viettravelbk.upload_image.ImageInfo;
import com.viettravelbk.upload_image.ImageRepository;

@Component
public class IntertainmentConverter {
	
    @Autowired
    private ImageRepository imageRepository;
	
	public Intertainment toEntity(IntertainmentDTO dto) {
		Intertainment entity = new Intertainment();
		entity.setTitleInter(dto.getTitleInter());
		entity.setContentInter(dto.getContentInter());
		entity.setLocationInter(dto.getLocationInter());
		entity.setThumbnailInter(dto.getThumbnailInter());
		return entity;
	}
	
	public IntertainmentDTO toDTO(Intertainment entity) {
		IntertainmentDTO dto = new IntertainmentDTO();
		if(entity.getId() != null) {									//response trả về có id
			dto.setId(entity.getId());
		}
		dto.setTitleInter(entity.getTitleInter());
		dto.setContentInter(entity.getContentInter());
		dto.setLocationInter(entity.getLocationInter());
		dto.setThumbnailInter(entity.getThumbnailInter());
        if (entity.getCategory() != null) {
            dto.setCategoryCode(entity.getCategory().getCode()); 		//Giả sử đối tượng Category có thuộc tính code
            dto.setCategoryName(entity.getCategory().getName()); 		//Và thuộc tính name
        }
        if (entity.getImageId() != null) {
            dto.setImageId(entity.getImageId().getId());  				//Chỉ lấy ID của ImageInfo
            dto.setImageUrls(entity.getImageId().getImageUrls());
        }
        dto.setImageUrl(getMainImageUrl(entity.getImageId())); 			//Sử dụng phương thức mới để lấy đường dẫn URL của file ảnh chứa chuỗi "main"
		return dto;
	}
	
    private String getMainImageUrl(ImageInfo imageInfo) {
        if (imageInfo != null) {           
            String[] urls = imageInfo.getImageUrls().split(", ");		//Tách các URL thành mảng           
            String mainImageUrl = Arrays.stream(urls)					//Lấy URL của file ảnh chứa chuỗi "main" (nếu có)
                                        .filter(url -> url.contains("main"))
                                        .findFirst()
                                        .orElse(null);            
            if (mainImageUrl != null) {									//Nếu tìm thấy URL chứa chuỗi "main", trả về nó
                return mainImageUrl;
            }
        }
        return null; 													//Trả về null nếu không tìm thấy URL chứa chuỗi "main"
    }
	
	public Intertainment toEntity(IntertainmentDTO dto, Intertainment entity) {
		entity.setTitleInter(dto.getTitleInter());
		entity.setContentInter(dto.getContentInter());
		entity.setLocationInter(dto.getLocationInter());
		entity.setThumbnailInter(dto.getThumbnailInter());
		return entity;
	}
}
