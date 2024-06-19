package com.viettravelbk.dto;

import lombok.Data;

@Data
public class CommentDTO extends AbstractDTO<CommentDTO>{
    private String text;	
    private Long userId; 							//Thêm trường này để chứa id của User
    private Long roomId; 							//Field chứa id của room
}
