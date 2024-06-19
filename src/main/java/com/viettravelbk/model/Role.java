package com.viettravelbk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor 
@NoArgsConstructor
@Data
@Entity
@Table(name = "role")
public class Role {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    private String name;
	    
		 //Tạo thêm 1 Table có khóa ngoài là khóa chính tại Table role
		 @ManyToMany(mappedBy = "roles")
		 private List<User> users = new ArrayList<>();
		 
		 @ManyToMany(mappedBy = "roles")
		 private List<User_OAuth2> userss = new ArrayList<>();
}
