package com.viettravelbk.extra_object.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveService implements IMoveService{
	@Autowired 
	private MoveRepository moveRepository;
	
	@Autowired
	private MoveConverter moveConverter;
	
	@Override														//post và put
	public MoveDTO save(MoveDTO moveDTO) {							//Vì hàm save() có 2 vai trò là Thêm mới và Cập nhật nên 	
	    Move moveEntity = new Move();								//Sử dụng dùng gộp để Post và Put
	    if (moveDTO.getId() != null) {								//Cập nhật						
	        Optional<Move> optionalOldMove = moveRepository.findById(moveDTO.getId());
	        if (optionalOldMove.isPresent()) {
	            Move oldMove = optionalOldMove.get();				//Muốn thay đổi thì phải tìm id cũ
	            moveEntity = moveConverter.toEntity(moveDTO, oldMove);
	        } else {
	            //Xử lý logic nếu không tìm thấy đối tượng với id tương ứng
	        }
	    } else {													//Thêm mới		
	        moveEntity = moveConverter.toEntity(moveDTO);
	    }    
	    moveEntity = moveRepository.save(moveEntity);				//khi oldNew đã có id thì sẽ là cập nhật			
	    return moveConverter.toDTO(moveEntity);
	}
	
	@Override														//delete
	public void delete(long[] ids) {
		for(long item: ids) {
			moveRepository.deleteById(item);
		}
	}
	
	@Override														//Lấy tất cả các trang - Lấy tất cả các move
	public List<MoveDTO> findAll() {								
		List<MoveDTO> results = new ArrayList<>();
		List<Move> entities = moveRepository.findAll();				//Truyền pageable xuống entity
		for (Move item: entities) {
			MoveDTO moveDTO = moveConverter.toDTO(item);
			results.add(moveDTO);
		}
		return results;
	}
	
	//Lấy move theo id
	@Override											
	public MoveDTO findById(Long id) {
	    Optional<Move> move = moveRepository.findById(id);
	    return move.map(moveConverter::toDTO).orElse(null);
	}
	
	//Check và Update move
	@Override
	public List<MoveDTO> checkAndUpdateMoves(List<Long> moveIds) {
	    List<MoveDTO> updatedMoves = new ArrayList<>();
	    for (Long moveId : moveIds) {
	        Optional<Move> moveOptional = moveRepository.findById(moveId);
	        if (moveOptional.isPresent()) {
	            Move move = moveOptional.get();
	            MoveDTO moveDTO = moveConverter.toDTO(move);
	            updatedMoves.add(moveDTO);
	        } else {
	            //Bỏ qua nếu move không tìm thấy, hoặc có thể xử lý khác
	            //Ví dụ: ghi log, thông báo lỗi cụ thể, v.v...
	        }
	    }
	    return updatedMoves;
	}
}
