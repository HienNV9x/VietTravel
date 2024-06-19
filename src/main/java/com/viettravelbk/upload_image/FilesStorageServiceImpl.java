package com.viettravelbk.upload_image;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.viettravelbk.upload_image.ImageRepository;
import com.viettravelbk.upload_image.FilesStorageService;

@Service
public class FilesStorageServiceImpl implements FilesStorageService{
	@Autowired 
	private ImageRepository imageRepository;

	private final Path root = Paths.get("src/main/resources/static/uploads");   //Lưu file ảnh vào folder tĩnh

	@Override
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}
	
	@Override
	public String save(MultipartFile file) {
	    try {
	        String originalFileName = file.getOriginalFilename();
	        Path destinationFilePath = this.root.resolve(originalFileName);
	        Files.copy(file.getInputStream(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

	        String fileUrl = "/uploads/" + originalFileName; 					//Tạo URL tương đối
	        return fileUrl;
	    } catch (Exception e) {
	        throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	    }
	}
	
	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}
	@Override													//Xóa 1 file ảnh trong folder
	public void delete(String filename) {
	    try {
	        Path file = root.resolve(filename);
	        Files.deleteIfExists(file);
	    } catch (IOException e) {
	        throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
	    }
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}
	
    @Override
    public String getFileUrl(String fileName) {
    	return "/uploads/" + fileName;
    }
}
