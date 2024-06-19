package com.viettravelbk.repository;

import java.util.List;										//Tìm  kiếm theo từ khóa

import org.springframework.data.domain.Page;				//Phân trang new
import org.springframework.data.domain.Pageable;			//Tìm  kiếm theo từ khóa
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;		//Tìm  kiếm theo từ khóa
import org.springframework.data.repository.query.Param;		//Tìm  kiếm theo từ khóa

import com.viettravelbk.model.Room;
import com.viettravelbk.model.User;

public interface RoomRepository extends JpaRepository<Room, Long>{
    @Query(value = "SELECT r FROM Room r WHERE r.category.name LIKE %:categoryName%",
    countQuery = "SELECT count(r) FROM Room r WHERE r.category.name LIKE %:categoryName%")			//Phân trang new
    Page<Room> findByCategoryName(String categoryName, Pageable pageable);							//Phân trang new
    List<Room> findByCategoryCode(String categoryCode);												//Get Room theo category
    //List<Room> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable); //Khi phân trang new thì bỏ
}
