package com.viettravelbk.extra_object.cuisine;

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
public class CuisineController {
	@Autowired
	private CuisineService cuisineService;
	
	@Autowired
	private CuisineRepository cuisineRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CuisineConverter cuisineConverter;
	
	//Lấy toàn bộ các Cuisine
    @GetMapping(value = "/cuisine")
    public ResponseEntity<Map<String, Object>> showCuisine() {
        List<CuisineDTO> cuisines = cuisineService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("listResult", cuisines);  					//Đóng gói danh sách cuisines vào một map với khóa là "listResult"
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
    //Lấy thông tin của cuisine theo id
    @GetMapping(value = "/cuisine/{id}")							
    public ResponseEntity<CuisineDTO> getCuisineById(@PathVariable("id") Long id) {
        CuisineDTO cuisineDTO = cuisineService.findById(id);
        if(cuisineDTO != null) {
            return new ResponseEntity<>(cuisineDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    //Lấy các Cuisine theo category
    @GetMapping(value = "/cuisine/byCategory")
    public ResponseEntity<Map<String, Object>> getCuisineByCategory(@RequestParam String categoryCode) {
        List<CuisineDTO> cuisines = cuisineService.findByCategory(categoryCode);
        Map<String, Object> response = new HashMap<>();
        response.put("listResult", cuisines);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /*@PostMapping("/cuisine")									//Khi thêm 1 bản ghi mới thì id sẽ tự động tạo ra
    public CuisineDTO createCuisine(@RequestBody CuisineDTO model) {
            return cuisineService.save(model);
    }*/
    //Post Cuisine kèm ImageId
    @PostMapping("/cuisine")
    public ResponseEntity<CuisineDTO> createCuisine(@RequestBody CuisineDTO model) {
        ImageInfo image = imageRepository.findById(model.getImageId())
                          .orElseThrow(() -> new RuntimeException("Image not found with id: " + model.getImageId()));
        Category category = categoryRepository.findOneByCode(model.getCategoryCode());
        if (category == null) {
            throw new RuntimeException("Category not found with code: " + model.getCategoryCode());
        }
        
        Cuisine cuisine = cuisineConverter.toEntity(model);  			//Chuyển đổi DTO sang Entity
        cuisine.setImageId(image);  									//Set ImageInfo cho Cuisine entity
        cuisine.setCategory(category); 									//Set Category cho Cuisine entity
        cuisine = cuisineRepository.save(cuisine);  					//Lưu Cuisine entity
        CuisineDTO savedCuisine = cuisineConverter.toDTO(cuisine);  	//Chuyển đổi entity trở lại thành DTO

        return ResponseEntity.ok(savedCuisine);
    }
    
    @PutMapping(value = "/cuisine/{id}")								//Khi thay đổi 1 bản ghi thì phải có id bản ghi muốn thay đổi
    public CuisineDTO updateCuisine(@RequestBody CuisineDTO model, @PathVariable("id") long id) {
    	model.setId(id);
    	return cuisineService.save(model);
    }
    
    @DeleteMapping(value = "/cuisine/{id}")
    public void deleteCuisineById(@PathVariable("id") long id) {
        cuisineService.delete(new long[]{id});
    }
    
    //Check và Update cart
    @PostMapping("/api/cuisine/check")
    public ResponseEntity<?> checkCuisine(@RequestBody CuisineCheckRequest request) {
        List<CuisineDTO> updatedCuisines = cuisineService.checkAndUpdateCuisines(request.getCuisineIds());
        return ResponseEntity.ok(updatedCuisines);
    }
}
