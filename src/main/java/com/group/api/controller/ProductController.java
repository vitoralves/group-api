package com.group.api.controller;

import java.security.NoSuchAlgorithmException;

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
import org.springframework.web.bind.annotation.RestController;

import com.group.api.model.Product;
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
	public ResponseEntity<Response<Product>> group(@Valid @RequestBody Product p, BindingResult result)
			throws NoSuchAlgorithmException {
		log.info("Products to group: {}", p.toString());
		Response<Product> response = new Response<Product>();
		
		
		return ResponseEntity.ok(response);
	}
}
