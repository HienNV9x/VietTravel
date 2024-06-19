package com.viettravelbk.extra_object.cuisine;

import java.util.List;

//Đại diện cho yêu cầu kiểm tra giỏ hàng từ phía client. 
//Class chứa danh sách các ID của Cuisine mà người dùng muốn kiểm tra.
public class CuisineCheckRequest {
    private List<Long> cuisineIds; 					//Danh sách các ID của Cuisine

    // Getters và Setters
    public List<Long> getCuisineIds() {
        return cuisineIds;
    }

    public void setCuisineIds(List<Long> cuisineIds) {
        this.cuisineIds = cuisineIds;
    }
}
