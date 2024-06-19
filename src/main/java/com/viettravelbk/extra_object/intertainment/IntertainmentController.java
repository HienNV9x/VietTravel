package com.viettravelbk.extra_object.intertainment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viettravelbk.model.Category;
import com.viettravelbk.repository.CategoryRepository;
import com.viettravelbk.upload_image.ImageInfo;
import com.viettravelbk.upload_image.ImageRepository;

@RestController
public class IntertainmentController {
	@Autowired
	private IntertainmentService intertainmentService;
	
	@Autowired
	private IntertainmentRepository intertainmentRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private IntertainmentConverter intertainmentConverter;
	
	//Lấy toàn bộ các Intertainment
    @GetMapping(value = "/intertainment")
    public ResponseEntity<Map<String, Object>> showIntertainment() {
        List<IntertainmentDTO> intertainments = intertainmentService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("listResult", intertainments);  									//Đóng gói danh sách intertainments vào một map với khóa là "listResult"
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    //Lấy thông tin của intertainment theo id
    @GetMapping(value = "/intertainment/{id}")							
    public ResponseEntity<IntertainmentDTO> getIntertainmentById(@PathVariable("id") Long id) {
        IntertainmentDTO intertainmentDTO = intertainmentService.findById(id);
        if(intertainmentDTO != null) {
            return new ResponseEntity<>(intertainmentDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Lấy các intertainment theo category
    @GetMapping(value = "/intertainment/byCategory")
    public ResponseEntity<Map<String, Object>> getIntertainmentByCategory(@RequestParam String categoryCode) {
        List<IntertainmentDTO> intertainments = intertainmentService.findByCategory(categoryCode);
        Map<String, Object> response = new HashMap<>();
        response.put("listResult", intertainments);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
	/*
    @PostMapping("/intertainment")														//Khi thêm 1 bản ghi mới thì id sẽ tự động tạo ra
    public IntertainmentDTO createIntertainment(@RequestBody IntertainmentDTO model) {
        return intertainmentService.save(model);
    }*/
    //Post Entertainment kèm ImageId
    @PostMapping("/intertainment")
    public ResponseEntity<IntertainmentDTO> createIntertainment(@RequestBody IntertainmentDTO model) {
        ImageInfo image = imageRepository.findById(model.getImageId())
                          .orElseThrow(() -> new RuntimeException("Image not found with id: " + model.getImageId()));
        Category category = categoryRepository.findOneByCode(model.getCategoryCode());
        if (category == null) {
            throw new RuntimeException("Category not found with code: " + model.getCategoryCode());
        }
        
        Intertainment intertainment = intertainmentConverter.toEntity(model);  			//Chuyển đổi DTO sang Entity
        intertainment.setImageId(image);  												//Set ImageInfo cho Intertainment entity
        intertainment.setCategory(category); 											//Set Category cho Intertainment entity
        intertainment = intertainmentRepository.save(intertainment);  					//Lưu Intertainment entity
        IntertainmentDTO savedIntertainment = intertainmentConverter.toDTO(intertainment); //Chuyển đổi entity trở lại thành DTO

        return ResponseEntity.ok(savedIntertainment);
    }
    
    @PutMapping(value = "/intertainment/{id}")											//Khi thay đổi 1 bản ghi thì phải có id bản ghi muốn thay đổi
    public IntertainmentDTO updateIntertainment(@RequestBody IntertainmentDTO model, @PathVariable("id") long id) {
    	model.setId(id);
    	return intertainmentService.save(model);
    }
    
    @DeleteMapping(value = "/intertainment/{id}")
    public void deleteIntertainmentById(@PathVariable("id") long id) {
        intertainmentService.delete(new long[]{id});
    }
    
    //Check và Update intertainment
    @PostMapping("/api/intertainment/check")
    public ResponseEntity<?> checkIntertainment(@RequestBody IntertainmentCheckRequest request) {
        List<IntertainmentDTO> updatedIntertainments = intertainmentService.checkAndUpdateIntertainments(request.getIntertainmentIds());
        return ResponseEntity.ok(updatedIntertainments);
    }
}
