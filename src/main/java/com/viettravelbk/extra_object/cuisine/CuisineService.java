package com.viettravelbk.extra_object.cuisine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettravelbk.model.Category;
import com.viettravelbk.repository.CategoryRepository;

@Service
public class CuisineService implements ICuisineService{
	@Autowired 
	private CuisineRepository cuisineRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CuisineConverter cuisineConverter;
	
	@Override														//post và put
	public CuisineDTO save(CuisineDTO cuisineDTO) {					//Vì hàm save() có 2 vai trò là Thêm mới và Cập nhật nên 	
	    Cuisine cuisineEntity = new Cuisine();						//Sử dụng dùng gộp để Post và Put
	    if (cuisineDTO.getId() != null) {							//Cập nhật						
	        Optional<Cuisine> optionalOldCuisine = cuisineRepository.findById(cuisineDTO.getId());
	        if (optionalOldCuisine.isPresent()) {
	            Cuisine oldCuisine = optionalOldCuisine.get();		//Muốn thay đổi thì phải tìm id cũ
	            cuisineEntity = cuisineConverter.toEntity(cuisineDTO, oldCuisine);
	        } else {
	            //Xử lý logic nếu không tìm thấy đối tượng với id tương ứng
	        }
	    } else {													//Thêm mới		
	        cuisineEntity = cuisineConverter.toEntity(cuisineDTO);
	    }
	    Category category = categoryRepository.findOneByCode(cuisineDTO.getCategoryCode());
	    cuisineEntity.setCategory(category);	    
	    cuisineEntity = cuisineRepository.save(cuisineEntity);		//Khi oldNew đã có id thì sẽ là cập nhật			
	    return cuisineConverter.toDTO(cuisineEntity);
	}
	
	@Override														//delete
	public void delete(long[] ids) {
		for(long item: ids) {
			cuisineRepository.deleteById(item);
		}
	}
	
	@Override														//Lấy tất cả các trang - Lấy tất cả các cuisine
	public List<CuisineDTO> findAll() {								
		List<CuisineDTO> results = new ArrayList<>();
		List<Cuisine> entities = cuisineRepository.findAll();		//Truyền pageable xuống entity
		for (Cuisine item: entities) {
			CuisineDTO cuisineDTO = cuisineConverter.toDTO(item);
			results.add(cuisineDTO);
		}
		return results;
	}
	
	//Lấy cuisine theo id
	@Override											
	public CuisineDTO findById(Long id) {
	    Optional<Cuisine> cuisine = cuisineRepository.findById(id);
	    return cuisine.map(cuisineConverter::toDTO).orElse(null);
	}
	
	//Lấy cuisine theo category
    public List<CuisineDTO> findByCategory(String categoryCode) {
        List<CuisineDTO> results = new ArrayList<>();
        List<Cuisine> entities = cuisineRepository.findByCategoryCode(categoryCode);
        for (Cuisine item : entities) {
            CuisineDTO cuisineDTO = cuisineConverter.toDTO(item);
            results.add(cuisineDTO);
        }
        return results;
    }
	
	//Check và Update Cuisine
	@Override
	public List<CuisineDTO> checkAndUpdateCuisines(List<Long> cuisineIds) {
	    List<CuisineDTO> updatedCuisines = new ArrayList<>();
	    for (Long cuisineId : cuisineIds) {
	        Optional<Cuisine> cuisineOptional = cuisineRepository.findById(cuisineId);
	        if (cuisineOptional.isPresent()) {
	            Cuisine cuisine = cuisineOptional.get();
	            CuisineDTO cuisineDTO = cuisineConverter.toDTO(cuisine);
	            updatedCuisines.add(cuisineDTO);
	        } else {
	            //Bỏ qua nếu room không tìm thấy, hoặc có thể xử lý khác
	            //Ví dụ: ghi log, thông báo lỗi cụ thể, v.v...
	        }
	    }
	    return updatedCuisines;
	}	
}
