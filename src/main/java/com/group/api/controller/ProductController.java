package com.group.api.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group.api.model.Product;
import com.group.api.model.Result;
import com.group.api.response.Response;
import com.group.api.service.ProductService;

@RestController
@RequestMapping("product")
@CrossOrigin("*")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService service;

	@PostMapping
	public ResponseEntity<Response<List<Result>>> group(@Valid @RequestBody List<Product> list,
			@RequestParam(value = "group", required = false) String group,
			@RequestParam(value = "order", required = false) String order, BindingResult result)
			throws NoSuchAlgorithmException {
		log.info("Products to group: {}", list.toString());
		// response declaration
		Response<List<Result>> response = new Response<List<Result>>();

		// list to insert results of grouping
		List<Result> resultList = new ArrayList<>();
		Map<String, List<Product>> map = new HashMap<String, List<Product>>();

		if (group != null && !group.isEmpty()) {
			try {
				map = service.groupBy(list, group);
			} catch (Exception e) {
				List<String> errors = new ArrayList<>();
				errors.add(e.getMessage());
				response.setErrors(errors);
				return ResponseEntity.badRequest().body(response);
			}
		} else {
			// result of grouping and sorting
			map = service.defaultGroup(list);
		}

		if (order != null && !order.isEmpty()) {
			try {
				map = service.orderBy(map, order);
			} catch (Exception e) {
				List<String> errors = new ArrayList<>();
				errors.add(e.getMessage());
				response.setErrors(errors);
				return ResponseEntity.badRequest().body(response);
			}
		} else {
			map = service.defaultOrder(map);
		}

		// create response objects
		map.entrySet().stream().forEach(f -> {
			Result r = new Result();
			//alter description name 
			if (group != null && (group.equals("ean") || group.equals("title"))
					|| (!f.getKey().equals(f.getValue().get(0).getBrand()))) {
				r.setDescription(f.getValue().get(0).getTitle());
			} else {
				r.setDescription(f.getKey());
			}
			r.setItems(f.getValue());
			
			//map was grouped and ordered, so we can find for duplicates
			if (!resultList.stream().filter(rl -> rl.getDescription().equals(r.getDescription())).findFirst().isPresent()) {
				resultList.add(r);
			}
		});

		response.setData(resultList);
		return ResponseEntity.ok(response);
	}
}
