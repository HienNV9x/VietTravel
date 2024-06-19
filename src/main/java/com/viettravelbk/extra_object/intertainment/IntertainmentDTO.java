package com.viettravelbk.extra_object.intertainment;

import lombok.Data;

@Data
public class IntertainmentDTO {
	private Long id;
	private String titleInter;
	private String contentInter;
	private String locationInter;
	private String thumbnailInter;
    private String categoryCode;
    private String categoryName;						//Thêm field Name trong Table Category
    private Long imageId;								//id Table image
    private String imageUrl;							//Thêm thuộc tính imageUrl để lưu đường dẫn hình ảnh
    private String imageUrls;							//Lưu đường dẫn 5 File ảnh của 1 Object ImageInfo
}
