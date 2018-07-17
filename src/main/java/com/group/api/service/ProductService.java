package com.group.api.service;

import java.util.List;
import java.util.Map;

import com.group.api.model.Product;

public interface ProductService {
	
	List<Product> filterBy(String filter, List<Product> list) throws Exception;
	
	Map<String, List<Product>> defaultGroup(List<Product> list);
	
	Map<String, List<Product>> defaultOrder(Map<String, List<Product>> map);
	
	Map<String, List<Product>> groupBy(List<Product> list, String attribute) throws Exception;
	
	Map<String, List<Product>> orderBy(Map<String, List<Product>> map, String order) throws Exception;
	
	Map<String, List<Product>> groupByTitle(List<Product> list);
	
	List<Product> similarity(String s, List<Product> list);
	
	Map<String, List<Product>> groupByStock(List<Product> list);
	
	Map<String, List<Product>> groupByPrice(List<Product> list);
}
