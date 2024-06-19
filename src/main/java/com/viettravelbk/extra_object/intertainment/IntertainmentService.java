package com.viettravelbk.extra_object.intertainment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettravelbk.model.Category;
import com.viettravelbk.repository.CategoryRepository;

@Service
public class IntertainmentService implements IIntertainmentService{
	@Autowired 
	private IntertainmentRepository intertainmentRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private IntertainmentConverter intertainmentConverter;
	
	@Override																		//post và put
	public IntertainmentDTO save(IntertainmentDTO intertainmentDTO) {				//Vì hàm save() có 2 vai trò là Thêm mới và Cập nhật nên 	
	    Intertainment intertainmentEntity = new Intertainment();					//Sử dụng dùng gộp để Post và Put
	    if (intertainmentDTO.getId() != null) {										//Cập nhật						
	        Optional<Intertainment> optionalOldIntertainment = intertainmentRepository.findById(intertainmentDTO.getId());
	        if (optionalOldIntertainment.isPresent()) {
	            Intertainment oldIntertainment = optionalOldIntertainment.get();	//Muốn thay đổi thì phải tìm id cũ
	            intertainmentEntity = intertainmentConverter.toEntity(intertainmentDTO, oldIntertainment);
	        } else {
	            // Xử lý logic nếu không tìm thấy đối tượng với id tương ứng
	        }
	    } else {																	//Thêm mới		
	        intertainmentEntity = intertainmentConverter.toEntity(intertainmentDTO);
	    }
	    Category category = categoryRepository.findOneByCode(intertainmentDTO.getCategoryCode());
	    intertainmentEntity.setCategory(category);	    
	    intertainmentEntity = intertainmentRepository.save(intertainmentEntity);	//Khi oldNew đã có id thì sẽ là cập nhật			
	    return intertainmentConverter.toDTO(intertainmentEntity);
	}
	
	@Override																		//delete
	public void delete(long[] ids) {
		for(long item: ids) {
			intertainmentRepository.deleteById(item);
		}
	}
	
	@Override																		//Lấy tất cả các trang - Lấy tất cả các cuisine
	public List<IntertainmentDTO> findAll() {								
		List<IntertainmentDTO> results = new ArrayList<>();
		List<Intertainment> entities = intertainmentRepository.findAll();			//Truyền pageable xuống entity
		for (Intertainment item: entities) {
			IntertainmentDTO intertainmentDTO = intertainmentConverter.toDTO(item);
			results.add(intertainmentDTO);
		}
		return results;
	}
	
	//Lấy intertainment theo id
	@Override											
	public IntertainmentDTO findById(Long id) {
	    Optional<Intertainment> intertainment = intertainmentRepository.findById(id);
	    return intertainment.map(intertainmentConverter::toDTO).orElse(null);
	}
	
	//Lấy intertainment theo category
    public List<IntertainmentDTO> findByCategory(String categoryCode) {
        List<IntertainmentDTO> results = new ArrayList<>();
        List<Intertainment> entities = intertainmentRepository.findByCategoryCode(categoryCode);
        for (Intertainment item : entities) {
            IntertainmentDTO intertainmentDTO = intertainmentConverter.toDTO(item);
            results.add(intertainmentDTO);
        }
        return results;
    }
	
	//Check và Update intertainment
	@Override
	public List<IntertainmentDTO> checkAndUpdateIntertainments(List<Long> intertainmentIds) {
	    List<IntertainmentDTO> updatedIntertainments = new ArrayList<>();
	    for (Long intertainmentId : intertainmentIds) {
	        Optional<Intertainment> intertainmentOptional = intertainmentRepository.findById(intertainmentId);
	        if (intertainmentOptional.isPresent()) {
	            Intertainment intertainment = intertainmentOptional.get();
	            IntertainmentDTO intertainmentDTO = intertainmentConverter.toDTO(intertainment);
	            updatedIntertainments.add(intertainmentDTO);
	        } else {
	            //Bỏ qua nếu room không tìm thấy, hoặc có thể xử lý khác
	            //Ví dụ: ghi log, thông báo lỗi cụ thể, v.v...
	        }
	    }
	    return updatedIntertainments;
	}	
}
