package com.group.api.model;

public class Product {
	
	private Integer id;
	private Integer ean;
	private String title;
	private String brand;
	private Double price;
	private Integer stock;
	
	
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Product(Integer id, Integer ean, String title, String brand, Double price, Integer stock) {
		super();
		this.id = id;
		this.ean = ean;
		this.title = title;
		this.brand = brand;
		this.price = price;
		this.stock = stock;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEan() {
		return ean;
	}
	public void setEan(Integer ean) {
		this.ean = ean;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", ean=" + ean + ", title=" + title + ", brand=" + brand + ", price=" + price
				+ ", stock=" + stock + "]";
	}
	
	
}
