package com.viettravelbk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/")
	public String homePage() {
		return "index";
	}
	@GetMapping("/viettravel/more")
	public String pageMore() {
		return "header_more";
	}
	
	@GetMapping("/viettravel/hagiang")
	public String pageHaGiang() {
		return "locations/hagiang";
	}
	@GetMapping("/viettravel/hanoi")
	public String pageHaNoi() {
		return "locations/hanoi";
	}
	@GetMapping("/viettravel/halong")
	public String pageHaLong() {
		return "locations/halong";
	}
	@GetMapping("/viettravel/ninhbinh")
	public String pageNinhBinh() {
		return "locations/ninhbinh";
	}
	@GetMapping("/viettravel/danang")
	public String pageDaNang() {
		return "locations/danang";
	}
	@GetMapping("/viettravel/hoian")
	public String pageHoiAn() {
		return "locations/hoian";
	}
	@GetMapping("/viettravel/dalat")
	public String pageDaLat() {
		return "locations/dalat";
	}
	@GetMapping("/viettravel/tphcm")
	public String pageTPHCM() {
		return "locations/tphcm";
	}
	@GetMapping("/viettravel/phuquoc")
	public String pagePhuQuoc() {
		return "locations/phuquoc";
	}
	@GetMapping("/viettravel/camau")
	public String pageCaMau() {
		return "locations/camau";
	}
	
	//AdminPage
	@GetMapping("/admin_user/admin/dashboard/1289Z54YO3974KSW01LM6")
	public String adminDashboard() {
		return "admin_user/admin/dashboard";
	}
	@GetMapping("/admin_user/admin/upload-cuisine/1289Z54YO3974KSW01LM6")
	public String cuisineAddNew() {
		return "admin_user/admin/upload-cuisine";
	}
	@GetMapping("/admin_user/admin/all-cuisine/1289Z54YO3974KSW01LM6")
	public String cuisineAll() {
		return "admin_user/admin/all-cuisine";
	}
	@GetMapping("/admin_user/admin/upload-room/1289Z54YO3974KSW01LM6")
	public String roomAddNew() {
		return "admin_user/admin/upload-room";
	}
	@GetMapping("/admin_user/admin/all-room/1289Z54YO3974KSW01LM6")
	public String roomAll() {
		return "admin_user/admin/all-room";
	}
	@GetMapping("/admin_user/admin/upload-entertainment/1289Z54YO3974KSW01LM6")
	public String entertainmentAddNew() {
		return "admin_user/admin/upload-entertainment";
	}
	@GetMapping("/admin_user/admin/all-entertainment/1289Z54YO3974KSW01LM6")
	public String entertainmentAll() {
		return "admin_user/admin/all-entertainment";
	}
	@GetMapping("/admin_user/admin/upload-transports/1289Z54YO3974KSW01LM6")
	public String transportsAddNew() {
		return "admin_user/admin/upload-transports";
	}
	@GetMapping("/admin_user/admin/all-transports/1289Z54YO3974KSW01LM6")
	public String transportAll() {
		return "admin_user/admin/all-transports";
	}
	
	//Page Room Detail
    @GetMapping("/detail")
    public String detail() {
    	return "roomDetail";
    }
    
	//Page Cart
    @GetMapping("/cart")
    public String cart() {
    	return "admin_user/user/cart";
    }
    
	//Search room
    @GetMapping({"/viettravel/search_room", "/search_room"})
    public String search_room() {
    	return "search_room";
    }
    
    //QRCode
    /*@GetMapping("/viettravel/qrcode")
    public String qrcode() {
    	return "admin_user/user/popup_qrcode";
    }*/

}