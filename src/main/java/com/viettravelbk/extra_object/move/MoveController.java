package com.viettravelbk.extra_object.move;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoveController {
	@Autowired
	private MoveService moveService;
	
	@Autowired
	private MoveRepository moveRepository;
	
	//Lấy toàn bộ các Move
    @GetMapping(value = "/move")
    public ResponseEntity<Map<String, Object>> showMove() {
        List<MoveDTO> Moves = moveService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("listResult", Moves);  						//Đóng gói danh sách moves vào một map với khóa là "listResult"
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    //Lấy thông tin của move theo id
    @GetMapping(value = "/move/{id}")							
    public ResponseEntity<MoveDTO> getMoveById(@PathVariable("id") Long id) {
        MoveDTO moveDTO = moveService.findById(id);
        if(moveDTO != null) {
            return new ResponseEntity<>(moveDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
    
    @PostMapping("/move")											//Khi thêm 1 bản ghi mới thì id sẽ tự động tạo ra
    public MoveDTO createMove(@RequestBody MoveDTO model) {
            return moveService.save(model);
    }
    
    @PutMapping(value = "/move/{id}")								//Khi thay đổi 1 bản ghi thì phải có id bản ghi muốn thay đổi
    public MoveDTO updateMove(@RequestBody MoveDTO model, @PathVariable("id") long id) {
    	model.setId(id);
    	return moveService.save(model);
    }
    
    @DeleteMapping(value = "/move/{id}")
    public void deleteMoveById(@PathVariable("id") long id) {
        moveService.delete(new long[]{id});
    }
    
    //Check và Update move
    @PostMapping("/api/move/check")
    public ResponseEntity<?> checkMove(@RequestBody MoveCheckRequest request) {
        List<MoveDTO> updatedMoves = moveService.checkAndUpdateMoves(request.getMoveIds());
        return ResponseEntity.ok(updatedMoves);
    }
}
