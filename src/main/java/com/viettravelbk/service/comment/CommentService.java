package com.viettravelbk.service.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;										

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.viettravelbk.converter.CommentConverter;
import com.viettravelbk.dto.CommentDTO;
import com.viettravelbk.model.Comment;
import com.viettravelbk.model.User;
import com.viettravelbk.repository.CommentRepository;
import com.viettravelbk.repository.UserRepository;
import com.viettravelbk.service.oauth2.CustomOAuth2User;

@Service
public class CommentService implements ICommentService{
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private CommentConverter commentConverter;
    @Autowired
    private UserRepository userRepository;
	
	@Override
	public CommentDTO save(CommentDTO commentDTO) {							//post

        //Tạo entity từ DTO
		Comment commentEntity = commentConverter.toEntityComment(commentDTO);
	
        //Lấy thông tin người dùng đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "";
        if (authentication != null) {										//Xác thực đối tượng có đang đăng nhập hay ko?
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                currentPrincipalName = ((UserDetails) principal).getUsername();
            } else if (principal instanceof CustomOAuth2User) {
                currentPrincipalName = ((CustomOAuth2User) principal).getName();
            } else {
                currentPrincipalName = principal.toString();
            }
        }
        User user = userRepository.findByUsername(currentPrincipalName);     //Lấy đối tượng đang đăng nhập
        commentEntity.setUser(user); 										 //Thiết lập User cho Comment
	    
	    if (commentDTO.getId() != null) {
	        Optional<Comment> optionalOldComment = commentRepository.findById(commentDTO.getId());
	        if (optionalOldComment.isPresent()) {
	            commentEntity = commentConverter.toEntityComment(commentDTO, optionalOldComment.get());
	        }
	    }
	    commentEntity = commentRepository.save(commentEntity); 				//Lưu commentEntity vào database
	    return commentConverter.toDTOComment(commentEntity); 				//Trả về DTO từ entity đã lưu
	}
	
	@Override																//delete
	public void delete(long[] ids) {
		for(long item: ids) {
			commentRepository.deleteById(item);
		}
	}
	public boolean canDeleteComment(Long commentId) {						//Kiểm tra chỉ có user đăng comment mới có thể delete xóa comment của họ
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return false;
	    }
	    String currentUsername = authentication.getName();
	    Optional<Comment> comment = commentRepository.findById(commentId);
	    return comment.map(Comment::getUser)
	                  .map(User::getUsername)
	                  .filter(username -> username.equals(currentUsername))
	                  .isPresent();
	}
	
	@Override
	public List<CommentDTO> findAll() {										//Lấy tất cả các trang
		List<CommentDTO> resultss = new ArrayList<>();
		List<Comment> entitiess = commentRepository.findAll();				//Truyền pageable xuống entity
		for (Comment item: entitiess) {
			CommentDTO commentDTO = commentConverter.toDTOComment(item);
			resultss.add(commentDTO);
		}
		return resultss;
	}
	
	//Tìm kiếm comment theo roomId
    public List<CommentDTO> findByRoomId(Long roomId) {
        List<Comment> comments = commentRepository.findByRoomId(roomId);
        return comments.stream()
                       .map(commentConverter::toDTOComment)
                       .collect(Collectors.toList());
    }
}
