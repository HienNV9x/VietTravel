package com.viettravelbk.upload_image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;												
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;							
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettravelbk.upload_image.ImageController;
import com.viettravelbk.upload_image.ImageInfo;
import com.viettravelbk.upload_image.ImageRepository;
import com.viettravelbk.upload_image.FilesStorageService;

@Controller
public class ImageController {
    @Autowired
    ImageRepository imageRepository;
	
	@Autowired
	FilesStorageService filesStorageService;
		
	@PostMapping("/images/upload")
	@ResponseBody
	public ResponseEntity<?> uploadImage(@RequestParam("files") List<MultipartFile> files) {
	    if (files.size() != 5) {
	        return ResponseEntity.badRequest().body("Bạn cần upload đúng 5 ảnh.");
	    }    
	    try {
		    String message = "";
		    List<String> fileNames = new ArrayList<>();
		    List<String> fileUrls = new ArrayList<>();
	        for (MultipartFile file : files) {
	            String fileName = file.getOriginalFilename();
	            String fileUrl = filesStorageService.save(file);
	            fileNames.add(fileName);
	            fileUrls.add(fileUrl);
	        }

	        //Chuyển danh sách tên và URL thành chuỗi
	        String joinedNames = String.join(", ", fileNames);
	        String joinedUrls = String.join(", ", fileUrls);

	        //Lưu vào cơ sở dữ liệu
	        ImageInfo imageModel = new ImageInfo();
	        imageModel.setImageNames(joinedNames);
	        imageModel.setImageUrls(joinedUrls);
	        ImageInfo savedImage = imageRepository.save(imageModel);
	        
            //Trả về ID và thông điệp thành công
            return ResponseEntity.ok(new ImageUploadResponse(savedImage.getId(), "Uploaded information successfully!"));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the images. Error: " + e.getMessage());
	    }
	}
	
	static class ImageUploadResponse {
	    private Long imageId;
	    private String message;

	    public ImageUploadResponse(Long imageId, String message) {
	        this.imageId = imageId;
	        this.message = message;
	    }

	    public Long getImageId() {
	        return imageId;
	    }
	    public void setImageId(Long imageId) {
	        this.imageId = imageId;
	    }
	    public String getMessage() {
	        return message;
	    }
	    public void setMessage(String message) {
	        this.message = message;
	    }
	}

	@GetMapping("/images")
	public String getListImages(Model model) {
		List<ImageInfo> imageInfos = filesStorageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(ImageController.class, "getImage", path.getFileName().toString()).build()
					.toString();

			return new ImageInfo(filename, url);
		}).collect(Collectors.toList());
		model.addAttribute("images", imageInfos);
		return "images";
	}
    
	@GetMapping("/images/{filename:.+}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
		Resource file = filesStorageService.load(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@GetMapping("/images/delete/{filename}")							//Xóa 1 file trong folder
	public String deleteImage(@PathVariable String filename, RedirectAttributes redirectAttributes) {
	    try {
	        filesStorageService.delete(filename);
	        redirectAttributes.addFlashAttribute("message", "File " + filename + " has been deleted successfully");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
	    }
	    return "redirect:/images";
	}
}
