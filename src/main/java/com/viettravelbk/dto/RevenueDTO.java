package com.viettravelbk.dto;

import lombok.Data;

@Data
public class RevenueDTO extends AbstractDTO<RevenueDTO>{
	private String title_room;
	private String category_room;
	private double income;
	private int quantity;
	private String cuisineLocal;
	private String intertainmentLocal;
	private String moveLocal;
}
