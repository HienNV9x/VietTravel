package com.viettravelbk.pageable;

import java.util.ArrayList;
import java.util.List;
import com.viettravelbk.dto.RoomDTO;
import lombok.Data;

@Data
public class RoomOutput {
	private int page;
	private int totalPage;
	private List<RoomDTO> listResult = new ArrayList<>();
}
