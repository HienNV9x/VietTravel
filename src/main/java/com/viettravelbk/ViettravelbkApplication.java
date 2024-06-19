package com.viettravelbk;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.viettravelbk.upload_image.FilesStorageService;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ViettravelbkApplication implements CommandLineRunner{
	@Resource
	FilesStorageService filesStorageService;

	public static void main(String[] args) {
		SpringApplication.run(ViettravelbkApplication.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception {
	    //filesStorageService.deleteAll();							  //Chỉ áp dụng cho Upload File Image vào folder động
		filesStorageService.init();
	}

}
