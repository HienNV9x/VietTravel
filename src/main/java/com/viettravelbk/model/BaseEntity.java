package com.viettravelbk.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@MappedSuperclass								//Để các class con kế thừa tù class cha sẽ có được các thuộc tính như class cha
@EntityListeners(AuditingEntityListener.class)	//Tự tạo createdBy, modifiedBy, createdDate, modifiedDate
public abstract class BaseEntity {				//và các thuộc tính này sẽ được generate vào db
    @Id											// ~ PRIMARY KEY và NOT NULL trong SQL
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	@CreatedDate								//dependency của createdDate
    private Date createdDate;
	
	@Column
	@LastModifiedDate							//dependency của modifiedDate
	private Date modifiedDate;
	
	@Column		
	@CreatedBy									//dependency của createdBy
	private String createdBy;					//username thêm mới
	
	@Column
	@LastModifiedBy								//dependency của modifiedBy
	private String modifiedBy;					//username cập nhật
}
