package com.group.api.service;

import java.util.List;
import java.util.Map;

import com.group.api.model.Product;

public interface ProductService {

	Map<String, List<Product>> defaultGroup(List<Product> list);
	
	Map<String, List<Product>> defaultOrder(Map<String, List<Product>> map);
	
	Map<String, List<Product>> groupBy(List<Product> list, String attribute);
	
	Map<String, List<Product>> orderBy(Map<String, List<Product>> map, String order) throws Exception;
}
