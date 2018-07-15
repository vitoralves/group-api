package com.group.api.model;

import java.util.List;

public class Result {

	private String description;
	private List<Product> items;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Product> getItems() {
		return items;
	}

	public void setItems(List<Product> items) {
		this.items = items;
	}

	
	
}
