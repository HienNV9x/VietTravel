package com.viettravelbk.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity{
	
	@Column(name = "text")
	private String text;
	
    @ManyToOne										//Table comments - infouser: n-1					
    @JoinColumn(name = "user_id") 					//comment		
    private User user;								//comment
    
    @ManyToOne										//Table comments - room: n-1
    @JoinColumn(name="room_id")
    private Room room;
}
