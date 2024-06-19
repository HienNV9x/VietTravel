package com.viettravelbk.extra_object.cuisine;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.viettravelbk.upload_image.ImageInfo;

@Component
public class CuisineConverter {
	public Cuisine toEntity(CuisineDTO dto) {
		Cuisine entity = new Cuisine();
		entity.setTitleCuisine(dto.getTitleCuisine());
		entity.setContentCuisine(dto.getContentCuisine());
		entity.setLocationCuisine(dto.getLocationCuisine());
		entity.setThumbnailCuisine(dto.getThumbnailCuisine());
		return entity;
	}
	
	public CuisineDTO toDTO(Cuisine entity) {
		CuisineDTO dto = new CuisineDTO();
		if(entity.getId() != null) {								//response trả về có id
			dto.setId(entity.getId());
		}
		dto.setTitleCuisine(entity.getTitleCuisine());
		dto.setContentCuisine(entity.getContentCuisine());
		dto.setLocationCuisine(entity.getLocationCuisine());
		dto.setThumbnailCuisine(entity.getThumbnailCuisine());
        if (entity.getCategory() != null) {
            dto.setCategoryCode(entity.getCategory().getCode());	//Giả sử đối tượng Category có thuộc tính code
            dto.setCategoryName(entity.getCategory().getName());	//Và thuộc tính name
        }
        if (entity.getImageId() != null) {
            dto.setImageId(entity.getImageId().getId());  			//Chỉ lấy ID của ImageInfo
            dto.setImageUrls(entity.getImageId().getImageUrls());
        }
        dto.setImageUrl(getMainImageUrl(entity.getImageId())); 		//Sử dụng phương thức mới để lấy đường dẫn URL của file ảnh chứa chuỗi "main"
		return dto;
	}
	
    private String getMainImageUrl(ImageInfo imageInfo) {
        if (imageInfo != null) {           
            String[] urls = imageInfo.getImageUrls().split(", ");	//Tách các URL thành mảng           
            String mainImageUrl = Arrays.stream(urls)				//Lấy URL của file ảnh chứa chuỗi "main" (nếu có)
                                        .filter(url -> url.contains("main"))
                                        .findFirst()
                                        .orElse(null);            
            if (mainImageUrl != null) {								//Nếu tìm thấy URL chứa chuỗi "main", trả về nó
                return mainImageUrl;
            }
        }
        return null; 												//Trả về null nếu không tìm thấy URL chứa chuỗi "main"
    }
	
	public Cuisine toEntity(CuisineDTO dto, Cuisine entity) {
		entity.setTitleCuisine(dto.getTitleCuisine());
		entity.setContentCuisine(dto.getContentCuisine());
		entity.setLocationCuisine(dto.getLocationCuisine());
		entity.setThumbnailCuisine(dto.getThumbnailCuisine());
		return entity;
	}
}