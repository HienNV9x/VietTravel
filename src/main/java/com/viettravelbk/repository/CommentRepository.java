package com.viettravelbk.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.viettravelbk.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByUserId(Long userId);
	List<Comment> findByRoomId(Long roomId);
}
