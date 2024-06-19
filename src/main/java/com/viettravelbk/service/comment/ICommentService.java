package com.viettravelbk.service.comment;

import java.util.List;
import com.viettravelbk.dto.CommentDTO;

public interface ICommentService {
	CommentDTO save(CommentDTO commentDTO);					//post và put 
	void delete(long[] ids);								//delete
	List<CommentDTO> findAll(); 							//get all
}
