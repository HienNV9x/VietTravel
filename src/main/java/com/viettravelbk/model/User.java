package com.viettravelbk.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.viettravelbk.model.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor 
@NoArgsConstructor
@Data
@Entity
@Table(name = "info_user")
public class User {
	@Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="username")
    private String username;
	
	@Column(name="age")
	private String age;
	
	@Column(name="country")
	private String country;
	
	@Column(name="city")
	private String city;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
    private String password;
	
    @Transient													//Thông báo cho JPA rằng trường đó không nên được lưu trữ trong cơ sở dữ liệu.
    private String passwordConfirm;
    
    @Column(name="travel_hobby")
    private String travel_hobby;
    
	@OneToMany(mappedBy = "user")								//Table: comment-infouser: n-1		
	private List<Comment> comments = new ArrayList<>();			//comment
    
    //Tạo thêm 1 Table có khóa ngoài là khóa chính tại Table user
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            	joinColumns = @JoinColumn(name = "user_id"),
            	inverseJoinColumns = @JoinColumn(name = "role_id")
        )
    private List<Role> roles = new ArrayList<>();
}
