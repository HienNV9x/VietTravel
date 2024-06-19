package com.viettravelbk.service.room;

import java.util.List;

//Đại diện cho yêu cầu kiểm tra giỏ hàng từ phía client. 
//Class chứa danh sách các ID của Room mà người dùng muốn kiểm tra.
public class CartCheckRequest {
    private List<Long> roomIds; 					//Danh sách các ID của Room

    // Getters và Setters
    public List<Long> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<Long> roomIds) {
        this.roomIds = roomIds;
    }
}
