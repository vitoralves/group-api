package com.group.api.model;

public class Product {

	private String id;
	private String ean;
	private String title;
	private String brand;
	private Double price;
	private Integer stock;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(String id, String ean, String title, String brand, Double price, Integer stock) {
		super();
		this.id = id;
		this.ean = ean;
		this.title = title;
		this.brand = brand;
		this.price = price;
		this.stock = stock;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Product product = (Product) o;

		if (ean != null ? !ean.equals(product.ean) : product.ean != null)
			return false;
		if (title != null ? !title.equals(product.title) : product.title != null)
			return false;
		return brand != null ? brand.equals(product.brand) : product.brand == null;
	}

}
