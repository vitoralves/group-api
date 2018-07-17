package com.group.api.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import com.group.api.model.Product;
import com.group.api.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Override
	public List<Product> filterBy(String filter, List<Product> list) throws Exception {
		String[] f = filter.split(":");

		if (!filter.contains(":") || f.length != 2) {
			throw new Exception(
					"Invalid format to filter. The correct is ..?filter={class attribute}:{value} and both can't be empty!");
		}

		switch (f[0].trim().toLowerCase()) {
		case "id":
			list = list.stream().filter(product -> product.getId().equals(f[1])).collect(Collectors.toList());
			break;
		case "ean":
			list = list.stream().filter(product -> product.getEan().equals(f[1])).collect(Collectors.toList());
			break;
		case "title":
			list = list.stream().filter(product -> product.getTitle().equals(f[1])).collect(Collectors.toList());
			break;
		case "brand":
			list = list.stream().filter(product -> product.getBrand().equals(f[1])).collect(Collectors.toList());
			break;
		case "stock":
			list = list.stream().filter(product -> product.getStock().equals(Integer.parseInt(f[1])))
					.collect(Collectors.toList());
			break;
		case "price":
			list = list.stream().filter(product -> product.getPrice().equals(Double.parseDouble(f[1])))
					.collect(Collectors.toList());
			break;
		default:
			throw new Exception("Attribute " + f[0] + " does not exist on class Product!");
		}
		return list;

	}

	@Override
	public Map<String, List<Product>> defaultGroup(List<Product> list) {
		Map<String, List<Product>> map = new HashMap<>();
		Map<String, List<Product>> mapEan = new HashMap<>();
		Map<String, List<Product>> mapBrand = new HashMap<>();
		// hashmap grouped by title similarity >= 70%
		Map<String, List<Product>> titleMap = groupByTitle(list);
		Map<String, List<Product>> finalMap = new HashMap<>();

		// map grouped by EAN
		mapEan = list.stream().collect(Collectors.groupingBy(Product::getEan));

		// map grouped by BRAN
		mapBrand = list.stream().collect(Collectors.groupingBy(Product::getBrand));

		// concat mapEAN with title map according priority
		map = Stream.concat(mapEan.entrySet().stream(), titleMap.entrySet().stream())
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		// concat according priority
		map = Stream.concat(map.entrySet().stream(), mapBrand.entrySet().stream())
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		map.entrySet().stream().forEach(m -> {
			// insert into final map only items witch were grouped
			if (m.getValue().size() > 1) {
				finalMap.put(m.getKey(), m.getValue());
			}
		});

		return finalMap;
	}

	@Override
	public Map<String, List<Product>> groupBy(List<Product> list, String attribute) throws Exception {

		Map<String, List<Product>> r = new HashMap<String, List<Product>>();

		switch (attribute.trim().toLowerCase()) {
		case "id":
			r = list.stream().collect(groupingBy(Product::getId));
			break;
		case "ean":
			r = list.stream().collect(groupingBy(Product::getEan));
			break;
		case "title":
			r = groupByTitle(list);
			break;
		case "brand":
			r = list.stream().collect(groupingBy(Product::getBrand));
			break;
		case "stock":
			r = groupByStock(list);
			break;
		case "price":
			r = groupByPrice(list);
			break;
		default:
			throw new Exception("Attribute " + attribute + " does not exist on class Product!");
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
			throw new Exception(
					"Invalid format to order. The correct is ..?order={class attribute}:{asc/desc} and both can't be empty!");
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
						: Comparator.comparing(Product::getEan).reversed());
			});
			break;
		case "title":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getTitle)
						: Comparator.comparing(Product::getTitle).reversed());
			});
			break;
		case "brand":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getBrand)
						: Comparator.comparing(Product::getBrand).reversed());
			});
			break;
		case "price":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getPrice)
						: Comparator.comparing(Product::getPrice).reversed());
			});
			break;
		case "stock":
			map.entrySet().stream().forEach(f -> {
				f.getValue().sort(s[1].equals("asc") ? Comparator.comparing(Product::getStock)
						: Comparator.comparing(Product::getStock).reversed());
			});
			break;
		default:
			throw new Exception("Attribute " + order + " does not exist on class Product");
		}

		return map;
	}

	/**
	 * This method returns a map grouped by product title similarity if the
	 * similarity is 70% or more
	 * 
	 * @param list
	 * @return
	 */
	@Override
	public Map<String, List<Product>> groupByTitle(List<Product> list) {
		Map<String, List<Product>> map = new HashMap<>();
		// copy of list to remove grouped items
		List<Product> removes = new ArrayList<>(list);

		for (Product p : new ArrayList<>(removes)) {
			List<Product> listSimilar = new ArrayList<>();
			// pass current product title to compare to all items of list
			// if other product has title with 70% or more similarity we group them
			listSimilar = similarity(p.getTitle(), removes);
			// verify similarity with this product
			if (listSimilar.size() > 1) {
				listSimilar.forEach(l -> {
					// remove grouped products to avoid duplicates
					removes.remove(l);
				});

				map.put(p.getTitle(), listSimilar);
			}
		}

		return map;
	}

	/**
	 * Calculates the similarity (a number within 0 and 1) between two strings. In
	 * this case compare a product title with all products in list if the similarity
	 * is 70% or more add to list
	 */
	@Override
	public List<Product> similarity(String s1, List<Product> list) {
		List<Product> similar = new ArrayList<>();

		list.forEach(l -> {
			String longer = s1, shorter = l.getTitle();
			if (s1.length() < l.getTitle().length()) { // longer should always have greater length
				longer = l.getTitle();
				shorter = s1;
			}
			int longerLength = longer.length();
			/* both strings are zero length */
			if (longerLength == 0) {
				similar.add(l);
			} else {
				/*
				 * Using Apache Commons Text, to calculate the edit distance
				 * 
				 */
				LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
				double result = (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength;

				if (result >= 0.7) {
					similar.add(l);
				}
			}
		});

		return similar;
	}

	/**
	 * Group a product list with same stock
	 * 
	 * @param list
	 * @return
	 */
	@Override
	public Map<String, List<Product>> groupByStock(List<Product> list) {
		List<Product> removes = new ArrayList<>(list);
		Map<String, List<Product>> map = new HashMap<>();

		for (Product p : new ArrayList<>(removes)) {
			List<Product> listGroup = new ArrayList<>();

			removes.forEach(r -> {
				if (p.getStock() == r.getStock()) {
					listGroup.add(r);
				}
			});

			if (listGroup.size() > 1) {
				listGroup.forEach(l -> {
					removes.remove(l);
				});

				map.put(p.getTitle(), listGroup);
			}
		}

		return map;
	}

	/**
	 * Group a product list with same price
	 */
	@Override
	public Map<String, List<Product>> groupByPrice(List<Product> list) {
		List<Product> removes = new ArrayList<>(list);
		Map<String, List<Product>> map = new HashMap<>();

		for (Product p : new ArrayList<>(removes)) {
			List<Product> listGroup = new ArrayList<>();

			removes.forEach(r -> {
				if (p.getPrice().equals(r.getPrice())) {
					listGroup.add(r);
				}
			});

			if (listGroup.size() > 1) {
				listGroup.forEach(l -> {
					removes.remove(l);
				});

				map.put(p.getTitle(), listGroup);
			}
		}

		return map;
	}
}
