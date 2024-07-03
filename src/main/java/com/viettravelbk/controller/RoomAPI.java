package com.viettravelbk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;										

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.viettravelbk.converter.RoomConverter;						
import com.viettravelbk.dto.RoomDTO;
import com.viettravelbk.model.Category;										
import com.viettravelbk.model.Room;
import com.viettravelbk.pageable.RoomOutput;
import com.viettravelbk.repository.CategoryRepository;						
import com.viettravelbk.repository.RoomRepository;
import com.viettravelbk.service.room.CartCheckRequest;
import com.viettravelbk.service.room.RoomService;
import com.viettravelbk.upload_image.ImageInfo;							
import com.viettravelbk.upload_image.ImageRepository;					

@RestController
public class RoomAPI {
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private RoomConverter roomConverter;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	//Phương thức GET lấy toàn bộ các trang, phân trang, tìm kiếm theo từ khóa
    @GetMapping(value = "/room")																//Phân trang							
    public RoomOutput showRoom(@RequestParam(value = "page", required = false) Integer page,	//Có thể phân trang và có thể ko, lấy tổng
    						 @RequestParam(value = "limit", required = false) Integer limit, 	//Mặc định của required là true, nếu phân trang thì sẽ có page, limit thì là false
    						 @RequestParam(value = "search", required = false) String search) {	//Tìm kiếm theo từ khóa
    	RoomOutput result = new RoomOutput();
        if (search != null && !search.isEmpty()) {
            Pageable pageable = PageRequest.of(page != null ? page - 1 : 0, limit != null ? limit : 100); //limit là số trang có thể hiển thị
            return roomService.findByCategoryName(search, pageable);
        }else {
        	if(page != null && limit != null) {
        		result.setPage(page);
        		Pageable pageable = PageRequest.of(page - 1, limit);	
        		//result.setListResult(roomService.findAll(pageable));
        		result.setTotalPage((int) Math.ceil((double) (roomService.totalItem()) / limit));
        	}else {
        		result.setListResult(roomService.findAll());
        	}
    	return result;
        }
    }
    
    //Lấy thông tin của room theo id
    @GetMapping(value = "/room/{id}")							
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable("id") Long id) {
        RoomDTO roomDTO = roomService.findById(id);
        if(roomDTO != null) {
            return new ResponseEntity<>(roomDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Lấy các Room theo category
    @GetMapping(value = "/room/byCategory")
    public ResponseEntity<Map<String, Object>> getRoomByCategory(@RequestParam String categoryCode) {
        List<RoomDTO> rooms = roomService.findByCategory(categoryCode);
        Map<String, Object> response = new HashMap<>();
        response.put("listResult", rooms);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /*
    @PostMapping("/room")									//Khi thêm 1 bài viết mới thì id sẽ tự động tạo ra
    public RoomDTO createNew(@RequestBody RoomDTO model) {
            return roomService.save(model);
    }
    */
    //Post Room kèm ImageId
    @PostMapping("/room")
    public ResponseEntity<RoomDTO> createNew(@RequestBody RoomDTO model) {
        ImageInfo image = imageRepository.findById(model.getImageId())
                          .orElseThrow(() -> new RuntimeException("Image not found with id: " + model.getImageId()));
        Category category = categoryRepository.findOneByCode(model.getCategoryCode());
        if (category == null) {
            throw new RuntimeException("Category not found with code: " + model.getCategoryCode());
        }
        
        Room room = roomConverter.toEntity(model);  			// Chuyển đổi DTO sang Entity
        room.setImageId(image);  								// Set ImageInfo cho Room entity
        room.setCategory(category); 							// Set Category cho Room entity
        room = roomRepository.save(room);  						// Lưu Room entity
        RoomDTO savedRoom = roomConverter.toDTO(room);  		// Chuyển đổi entity trở lại thành DTO

        return ResponseEntity.ok(savedRoom);
    }
    
    @PutMapping(value = "/room/{id}")							//Khi thay đổi 1 bài viết thì phải có id bài viết muốn thay đổi
    public RoomDTO updateNew(@RequestBody RoomDTO model, @PathVariable("id") long id) {
    	model.setId(id);
    	return roomService.save(model);
    }
    
    //Update roomPoint
    /*@PutMapping(value = "/roomPoint/{id}")
    public ResponseEntity<Double> updateRoomPoint(@PathVariable("id") long id, @RequestBody Map<String, Object> changes) {
        Double increment = (Double) changes.get("increment");
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        Double currentPoint = room.getRoomPoint() != null ? room.getRoomPoint() : 4;
        double updatedPoint = currentPoint + increment;
        updatedPoint = Math.round(updatedPoint * 100.0) / 100.0;	//Làm tròn updatedPoint đến hai chữ số thập phân
        room.setRoomPoint(updatedPoint);
        roomRepository.save(room);
        return ResponseEntity.ok(updatedPoint);
    }*/
    @PutMapping(value = "/roomPoint/{id}")
    public ResponseEntity<Double> updateRoomPoint(@PathVariable("id") long id, @RequestBody Map<String, Object> changes) {
        Double increment = (Double) changes.get("increment");
        String userId = (String) changes.get("userId");
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        Double currentPoint = room.getRoomPoint() != null ? room.getRoomPoint() : 4;
        double updatedPoint = currentPoint + increment;
        updatedPoint = Math.round(updatedPoint * 100.0) / 100.0;
        room.setRoomPoint(updatedPoint);
        if (increment > 0) {
            room.addUserIdLike(userId);
        } else {
            room.removeUserIdLike(userId);
        }
        roomRepository.save(room);
        return ResponseEntity.ok(updatedPoint);
    }
    
    @DeleteMapping(value = "/room")
    public void deleteRoom(@RequestBody long[] ids) {
    	roomService.delete(ids);
    }
    
    //Check và Update cart
    @PostMapping("/api/cart/check")
    public ResponseEntity<?> checkCart(@RequestBody CartCheckRequest request) {
        List<RoomDTO> updatedRooms = roomService.checkAndUpdateRooms(request.getRoomIds());
        return ResponseEntity.ok(updatedRooms);
    }
}
