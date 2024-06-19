package com.viettravelbk.extra_object.intertainment;

import java.util.List;

//Đại diện cho yêu cầu kiểm tra giỏ hàng từ phía client. 
//Class chứa danh sách các ID của Intertainment mà người dùng muốn kiểm tra.
public class IntertainmentCheckRequest {
    private List<Long> intertainmentIds; 					//Danh sách các ID của Intertainment

    // Getters và Setters
    public List<Long> getIntertainmentIds() {
        return intertainmentIds;
    }

    public void setIntertainmentIds(List<Long> intertainmentIds) {
        this.intertainmentIds = intertainmentIds;
    }
}
