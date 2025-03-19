package com.viettravelbk.service.user;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.viettravelbk.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails{
	   User user;

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        //Mặc định sẽ để tất cả là ROLE_USER.
	        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
	    }

	    @Override
	    public String getPassword() {
	        return user.getPassword();
	    }

	    @Override
	    public String getUsername() {
	        return user.getUsername();
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }
	    
	    //Phương thức để truy cập userId đưa vào giỏ hàng
	    public Long getUserId() {
	        return user.getId();
	    }
}
