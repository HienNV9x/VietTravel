package com.viettravelbk.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.viettravelbk.dto.AbstractDTO;

import lombok.Data;

@Data
public class AbstractDTO<T> {							//post put delete get
    private Long id;
    private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private List<T> listResult = new ArrayList<>();		//Tạo danh sách 4 thuộc tính trên
}
