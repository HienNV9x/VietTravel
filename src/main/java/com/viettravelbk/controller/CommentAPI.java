package com.viettravelbk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.viettravelbk.dto.CommentDTO;
import com.viettravelbk.repository.CommentRepository;
import com.viettravelbk.service.comment.CommentService;

@RestController
public class CommentAPI {
	@Autowired
	private CommentService commentService;
	@Autowired
	private CommentRepository commentRepository;
	
    @GetMapping(value = "/comment")													
    public ResponseEntity<List<CommentDTO>> showComment() {
        List<CommentDTO> comments = commentService.findAll();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
    
    //Lấy comment theo roomId
    @GetMapping(value = "/comment/room/{roomId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByRoomId(@PathVariable("roomId") Long roomId) {
        List<CommentDTO> comments = commentService.findByRoomId(roomId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
    
    @PostMapping("/comment")									
    public CommentDTO createComment(@RequestBody CommentDTO model) {
            return commentService.save(model);
    }
    
    //Xóa dữ liệu dựa trên user post dữ liệu
    @DeleteMapping(value = "/comment/{id}")				
    public ResponseEntity<?> deleteComment(@PathVariable("id") long id) {
        if (!commentService.canDeleteComment(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        commentService.delete(new long[]{id});
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
