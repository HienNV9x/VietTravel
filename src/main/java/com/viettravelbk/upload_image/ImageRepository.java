package com.viettravelbk.upload_image;

import org.springframework.data.jpa.repository.JpaRepository;
import com.viettravelbk.upload_image.ImageInfo;

public interface ImageRepository extends JpaRepository<ImageInfo, Long>{

}
