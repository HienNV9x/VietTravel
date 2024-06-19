package com.viettravelbk.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.viettravelbk.config.JpaAuditingConfig.AuditorAwareImpl;

@Configuration											   //Sử dụng java config
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {						   //Sử dụng cho createdBy, modifiedBy, createdDate, modifiedDate
	@Bean
	public AuditorAware<String> auditorProvider(){
		return new AuditorAwareImpl();
	}
	
	public static class AuditorAwareImpl implements AuditorAware<String>{
		@Override
		public Optional<String> getCurrentAuditor() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	//authentication đánh dấu username để createdBy/modifiedBy biết username nào đã tạo
			if(authentication == null || !authentication.isAuthenticated()) {
				return Optional.empty();
			}
			return Optional.of(authentication.getName());
		}
	}
}
