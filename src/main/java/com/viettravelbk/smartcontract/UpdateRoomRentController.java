package com.viettravelbk.smartcontract;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/smartcontract")
@RequiredArgsConstructor
public class UpdateRoomRentController {
    private final UpdateRoomRentService updateRoomRentService;

    @PostMapping("/update-room-rent")
    public String updateRooomRent(@RequestBody RoomRentCreateRequest createRequest){
        return updateRoomRentService.updateTotalRoomRent(createRequest);
    }
}
