package com.viettravelbk.model;

import java.util.ArrayList;					
import java.util.Arrays;					
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.viettravelbk.upload_image.ImageInfo;

import lombok.Data;

@Data
@Entity
@Table(name = "room")
public class Room extends BaseEntity{ 						//Nhận dữ liệu dưới dạng json từ DTO và truyền xuống db
	@Column(name="title")
	private String title;
	
	@Column(name="thumbnail")
	private String thumbnail;
	
	@Column(name="shortdescription")
	private String shortDescription;
	
	@Column(name="content")
	private String content;
	
	@Column(name="price")
	private double price;
	
	@Column(name="roomservice")
	private String roomService;
	
	@Column(name="roompoint")
	private Double roomPoint;
	
	@Column(name="userIdLike")
	private String userIdLike;
	
    @ManyToOne												//Table rooms - category: n-1
    @JoinColumn(name = "category_id") 				
    private Category category;
    
    public void setRoomPoint(Double roomPoint) {			//Bổ sung nếu roomPoint là Null thì gán giá trị là 3.95
        if (roomPoint == null) {
            this.roomPoint = 3.95;
        } else {
            this.roomPoint = roomPoint;
        }
    }
    
    @OneToOne(cascade = CascadeType.REMOVE)					//Bản ghi ImageInfo sẽ xóa cùng bản ghi Room	
    @JoinColumn(name = "image_id")							//Table room - image: 1-1
    private ImageInfo imageId;
    
    //Tạo nhiều userId Like Room thành chuỗi
    public void addUserIdLike(String userId) {
        if (this.userIdLike == null || this.userIdLike.isEmpty()) {
            this.userIdLike = userId;
        } else {
            if (!this.userIdLike.contains(userId)) {
                this.userIdLike += ", " + userId;
            }
        }
    }
    public void removeUserIdLike(String userId) {
        if (this.userIdLike != null && !this.userIdLike.isEmpty()) {
            List<String> userIds = new ArrayList<>(Arrays.asList(this.userIdLike.split(", ")));
            userIds.remove(userId);
            this.userIdLike = String.join(", ", userIds);
        }
    }
    //Kiểm tra User đã Like Room
    public boolean isLikedByUser(String userId) {
        if (this.userIdLike != null && !this.userIdLike.isEmpty()) {
            List<String> userIds = Arrays.asList(this.userIdLike.split(", "));
            return userIds.contains(userId);
        }
        return false;
    }
}
