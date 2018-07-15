package com.group.api.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;
import org.springframework.stereotype.Service;
import com.group.api.model.Product;
import com.group.api.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Override
	public Map<String, List<Product>> defaultGroup(List<Product> list) {
		Map<String, List<Product>> map = new HashMap<>();
		Map<String, List<Product>> mapEan = new HashMap<>();
		Map<String, List<Product>> mapBrand = new HashMap<>();
		Map<Object, List<Product>> titleMap = new HashMap<>();
		Map<String, List<Product>> finalMap = new HashMap<>();

		mapEan = list.stream().collect(Collectors.groupingBy(Product::getEan));

		mapBrand = list.stream().collect(Collectors.groupingBy(Product::getBrand));

		map = Stream.concat(mapBrand.entrySet().stream(), mapEan.entrySet().stream())
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		map.entrySet().stream().forEach(m -> {
			if (m.getValue().size() > 1) {
				finalMap.put(m.getKey(), m.getValue());
			}
		});

		return finalMap;
	}

	@Override
	public Map<String, List<Product>> groupBy(List<Product> list, String attribute) {

		Map<String, List<Product>> r = new HashMap<String, List<Product>>();

		switch (attribute.trim().toLowerCase()) {
		case "id":
			r = list.stream().collect(groupingBy(Product::getId));
			break;
		case "ean":
			r = list.stream().collect(groupingBy(Product::getEan));
			break;
		case "title":
			r = list.stream().collect(groupingBy(Product::getTitle));
			break;
		case "brand":
			r = list.stream().collect(groupingBy(Product::getBrand));
			break;
		case "stock":
			r = list.stream().collect(groupingBy(Product::getBrand));
			break;
		default:
			r = list.stream().collect(groupingBy(Product::getEan));
			break;
		}
		return r;
	}

	@Override
	public Map<String, List<Product>> defaultOrder(Map<String, List<Product>> map) {

		map.entrySet().stream().forEach(f -> {
			// sort list first by price asc
			f.getValue().sort((Product p1, Product p2) -> p2.getPrice().compareTo(p1.getPrice()));
			// then sort by stock desc
			f.getValue().sort((Product p1, Product p2) -> p2.getStock() - p1.getStock());
		});

		return map;
	}

	@Override
	public Map<String, List<Product>> orderBy(Map<String, List<Product>> map, String order) throws Exception {
		String[] s = order.split(":");
		
		if (!order.contains(":") || s.length != 2) {
			throw new Exception("Invalid format. The correct is ..?order={class attribute}:{asc/desc} and both can't be empty!");
		}

		switch (s[0].trim().toLowerCase()) {
		case "id":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getId)
						: Comparator.comparing(Product::getId).reversed());
			});
			break;
		case "ean":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getEan)
						: Comparator.comparing(Product::getId).reversed());
			});
			break;
		case "title":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getTitle)
						: Comparator.comparing(Product::getId).reversed());
			});
			break;
		case "brand":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getBrand)
						: Comparator.comparing(Product::getId).reversed());
			});
			break;
		case "price":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getPrice)
						: Comparator.comparing(Product::getId).reversed());
			});
			break;
		case "stock":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getStock)
						: Comparator.comparing(Product::getId).reversed());
			});
			break;
		}

		return map;
	}

}
