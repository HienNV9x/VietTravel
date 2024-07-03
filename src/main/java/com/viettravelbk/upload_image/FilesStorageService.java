package com.viettravelbk.upload_image;

import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
	  public void init();

	  public String save(MultipartFile file);
	  
	  String getFileUrl(String fileName);				//Gộp ảnh
	  
	  void delete(String filename);						//Xóa 1 file ảnh trong folder
}
