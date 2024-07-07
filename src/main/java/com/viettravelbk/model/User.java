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
import javax.validation.constraints.AssertTrue;		
import javax.validation.constraints.Email;			
import javax.validation.constraints.Max;			
import javax.validation.constraints.Min;			
import javax.validation.constraints.NotBlank;		
import javax.validation.constraints.NotNull;		
import javax.validation.constraints.Pattern;		
import javax.validation.constraints.Size;			

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
	
	@NotBlank(message = "Name cannot be empty")
    @Size(min = 8, max = 30, message = "Name must be between 8 and 30 characters")
	@Column(name="username")
    private String username;
	
	@NotNull(message = "Age cannot be empty")
    @Min(value = 6, message = "Age should not be less than 6")
    @Max(value = 100, message = "Age should not be more than 100")
	@Column(name="age")
	private Integer age;
	
	@NotBlank(message = "Country cannot be empty")
	@Column(name="country")
	private String country;
	
	@NotBlank(message = "City cannot be empty")
	@Column(name="city")
	private String city;
	
	@NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
	@Column(name="email")
	private String email;
	
	@NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(
		    regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
		    message = "Password must start with an uppercase letter, contain at least one digit, and one special character"
		)
	@Column(name="password")
    private String password;
	
	@NotBlank(message = "Password confirmation cannot be empty")
    @Transient													//Thông báo cho JPA rằng trường đó không nên được lưu trữ trong cơ sở dữ liệu.
    private String passwordConfirm;
    
	@NotBlank(message = "Travel Hobby cannot be empty")
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
