package com.viettravelbk.service.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.viettravelbk.converter.RoomConverter;
import com.viettravelbk.dto.RoomDTO;
import com.viettravelbk.model.Category;
import com.viettravelbk.model.Room;
import com.viettravelbk.pageable.RoomOutput;
import com.viettravelbk.repository.CategoryRepository;
import com.viettravelbk.repository.RoomRepository;
import com.viettravelbk.service.room.IRoomService;
import com.viettravelbk.upload_image.FilesStorageService;			
import com.viettravelbk.upload_image.ImageInfo;						
import com.viettravelbk.upload_image.ImageRepository;				

@Service
public class RoomService implements IRoomService{
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private RoomConverter roomConverter;
	
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private FilesStorageService filesStorageService;

	@Override													//post và put
	public RoomDTO save(RoomDTO roomDTO) {						//Vì hàm save() có 2 vai trò là Thêm mới và Cập nhật nên 	
	    Room roomEntity = new Room();							//Sử dụng dùng gộp để Post và Put
	    if (roomDTO.getId() != null) {							//Cập nhật						
	        Optional<Room> optionalOldRoom = roomRepository.findById(roomDTO.getId());
	        if (optionalOldRoom.isPresent()) {
	            Room oldRoom = optionalOldRoom.get();			//Muốn thay đổi thì phải tìm id cũ
	            roomEntity = roomConverter.toEntity(roomDTO, oldRoom);
	        } else {
	            //Xử lý logic nếu không tìm thấy đối tượng với id tương ứng
	        }
	    } else {												//Thêm mới		
	        roomEntity = roomConverter.toEntity(roomDTO);
	    }
	    
	    Category category = categoryRepository.findOneByCode(roomDTO.getCategoryCode());
	    roomEntity.setCategory(category);
	    roomEntity = roomRepository.save(roomEntity);			//Khi oldNew đã có id thì sẽ là cập nhật			
	    return roomConverter.toDTO(roomEntity);
	}
	
    @Override
    public void delete(long[] ids) {
        for (long item : ids) {
            Optional<Room> roomOptional = roomRepository.findById(item);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                ImageInfo imageInfo = room.getImageId();
               
                roomRepository.deleteById(item);				// Xóa đối tượng Room trước
                
                if (imageInfo != null) {						// Xóa các tệp ảnh và đối tượng ImageInfo sau
                    String imageUrls = imageInfo.getImageUrls();
                    String[] urls = imageUrls.split(", ");
                    for (String url : urls) {
                        String filename = url.substring(url.lastIndexOf("/") + 1);
                        filesStorageService.delete(filename);
                    }
                    imageRepository.delete(imageInfo);
                }
            }
        }
    }
	
	//Lấy tất cả các trang - Lấy tất cả các room
	@Override
	public List<RoomDTO> findAll() {								
		List<RoomDTO> results = new ArrayList<>();
		List<Room> entities = roomRepository.findAll();							//Truyền pageable xuống entity
		for (Room item: entities) {
			RoomDTO roomDTO = roomConverter.toDTO(item);
			results.add(roomDTO);
		}
		return results;
	}
	
	//Lấy room theo category
    public List<RoomDTO> findByCategory(String categoryCode) {
        List<RoomDTO> results = new ArrayList<>();
        List<Room> entities = roomRepository.findByCategoryCode(categoryCode);
        for (Room item : entities) {
            RoomDTO roomDTO = roomConverter.toDTO(item);
            results.add(roomDTO);
        }
        return results;
    }
	
	//Lấy room theo id
	@Override											
	public RoomDTO findById(Long id) {
	    Optional<Room> room = roomRepository.findById(id);
	    return room.map(roomConverter::toDTO).orElse(null);
	}
	
	//Tìm kiếm theo từ khóa + Phân trang new
	@Override
	public RoomOutput findByCategoryName(String categoryName, Pageable pageable) {
	    Page<Room> page = roomRepository.findByCategoryName(categoryName, pageable);
	    List<RoomDTO> results = page.getContent().stream()
	                                .map(roomConverter::toDTO)
	                                .collect(Collectors.toList());

	    RoomOutput roomOutput = new RoomOutput();
	    roomOutput.setListResult(results);
	    roomOutput.setPage(pageable.getPageNumber() + 1); 						//Page index starts from 0
	    roomOutput.setTotalPage(page.getTotalPages());
	    return roomOutput;
	}
	@Override
	public int totalItem() {
		return (int) roomRepository.count();
	}
	
	//Check và Update cart
	@Override
	public List<RoomDTO> checkAndUpdateRooms(List<Long> roomIds) {
	    List<RoomDTO> updatedRooms = new ArrayList<>();
	    for (Long roomId : roomIds) {
	        Optional<Room> roomOptional = roomRepository.findById(roomId);
	        if (roomOptional.isPresent()) {
	            Room room = roomOptional.get();
	            RoomDTO roomDTO = roomConverter.toDTO(room);
	            updatedRooms.add(roomDTO);
	        } else {
	            //Bỏ qua nếu room không tìm thấy, hoặc có thể xử lý khác
	            //Ví dụ: ghi log, thông báo lỗi cụ thể, v.v...
	        }
	    }
	    return updatedRooms;
	}	
}
