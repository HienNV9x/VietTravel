package com.viettravelbk.extra_object.move;

import java.util.List;

//Đại diện cho yêu cầu kiểm tra giỏ hàng từ phía client. 
//Class chứa danh sách các ID của Move mà người dùng muốn kiểm tra.
public class MoveCheckRequest {
    private List<Long> moveIds; 					//Danh sách các ID của Move

    //Getters và Setters
    public List<Long> getMoveIds() {
        return moveIds;
    }

    public void setMoveIds(List<Long> moveIds) {
        this.moveIds = moveIds;
    }
}
