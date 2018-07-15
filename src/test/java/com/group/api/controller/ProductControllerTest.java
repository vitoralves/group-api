package com.group.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group.api.model.Product;
import com.group.api.service.ProductService;
import org.springframework.security.test.context.support.WithMockUser;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService service;

	private static final String URL_BASE = "/product";
	private static final String BRAND = "nikana";
	private static final String EAN = "19941101";
	private static final String TITLE = "Title of Product";

	/**
	 * Teste chamando o método principal sem passar nenhum parâmetro somenta uma
	 * lista que deve ser retornada
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void testCallProduct() throws JsonProcessingException, Exception {
		Map<String, List<Product>> map = returnDefaultMap();
		BDDMockito.given(this.service.defaultGroup(Mockito.anyListOf(Product.class))).willReturn(map);
		BDDMockito.given(this.service.defaultOrder(Mockito.anyMap())).willReturn(map);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE).content(this.returnProductListJon())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].description").value(BRAND)).andExpect(jsonPath("$.errors").isEmpty());
	}

	/**
	 * Teste chamando o método principal pedindo para agrupar por marca
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void testCallProductWithGroup() throws JsonProcessingException, Exception {
		Map<String, List<Product>> map = returnDefaultMap();
		BDDMockito.given(this.service.groupBy(Mockito.anyListOf(Product.class), Mockito.anyString())).willReturn(map);
		BDDMockito.given(this.service.defaultOrder(Mockito.anyMap())).willReturn(map);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "?group=brand").content(this.returnProductListJon())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].description").value(BRAND)).andExpect(jsonPath("$.errors").isEmpty());
	}

	/**
	 * Teste para agrupar por marca e ordenar por id de forma ascendente
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void testCallProductWithGroupAndOrder() throws JsonProcessingException, Exception {
		Map<String, List<Product>> map = returnDefaultMap();
		BDDMockito.given(this.service.groupBy(Mockito.anyListOf(Product.class), Mockito.anyString())).willReturn(map);
		BDDMockito.given(this.service.orderBy(Mockito.anyMap(), Mockito.anyString())).willReturn(map);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "?group=brand&order=id:asc").content(this.returnProductListJon())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].description").value(BRAND)).andExpect(jsonPath("$.errors").isEmpty());
	}

	/**
	 * Teste passando somente uma parametro de ordenação
	 * 
	 * Deve agrupar no padrão default e ordenar pelo parametro solicitado
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void testCallProductWithOrder() throws JsonProcessingException, Exception {
		Map<String, List<Product>> map = returnDefaultMap();
		BDDMockito.given(this.service.defaultGroup(Mockito.anyListOf(Product.class))).willReturn(map);
		BDDMockito.given(this.service.orderBy(Mockito.anyMap(), Mockito.anyString())).willReturn(map);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "?order=price:desc").content(this.returnProductListJon())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].description").value(BRAND)).andExpect(jsonPath("$.errors").isEmpty());
	}
	
	/**
	 * Quando for solicitado agrupamento por EAN ou título o valor do description deve ser o título do primeiro 
	 * produto da lista após ordenação
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	@WithMockUser
	public void testCallProductGroupByEAN() throws JsonProcessingException, Exception {
		Map<String, List<Product>> map = returnDefaultMap();
		BDDMockito.given(this.service.groupBy(Mockito.anyListOf(Product.class), Mockito.anyString())).willReturn(map);
		BDDMockito.given(this.service.defaultOrder(Mockito.anyMap())).willReturn(map);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE + "?group=ean").content(this.returnProductListJon())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].description").value(TITLE)).andExpect(jsonPath("$.errors").isEmpty());
	}
	
	private String returnProductListJon() throws JsonProcessingException {
		List<Product> list = new ArrayList<>();

		Product p1 = new Product();
		p1.setId("123");
		p1.setEan("7898100848355");
		p1.setTitle("Cruzador espacial Nikana - 3000m - sem garantia");
		p1.setBrand("nikana");
		p1.setPrice(820900.9);
		p1.setStock(1);

		Product p2 = new Product();
		p2.setId("321");
		p2.setEan("7898100848355");
		p2.setTitle("Cruzador espacial Nikana - 3000m - sem garantia");
		p2.setBrand("trek");
		p2.setPrice(790300.9);
		p2.setStock(0);

		Product p3 = new Product();
		p3.setId("123");
		p3.setEan("7898100848355");
		p3.setTitle("Cruzador espacial Nikana - 3000m - sem garantia");
		p3.setBrand("nikana");
		p3.setPrice(820900.9);
		p3.setStock(1);

		list.add(p1);
		list.add(p2);
		list.add(p3);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		String arrayToJson = objectMapper.writeValueAsString(list);

		return arrayToJson;
	}

	private Map<String, List<Product>> returnDefaultMap() {
		Map<String, List<Product>> map = new HashMap<>();
		List<Product> list = new ArrayList<>();

		Product p1 = new Product();
		p1.setId("123");
		p1.setEan(EAN);
		p1.setTitle(TITLE);
		p1.setBrand(BRAND);
		p1.setPrice(820900.9);
		p1.setStock(1);

		Product p2 = new Product();
		p2.setId("321");
		p2.setEan(EAN);
		p2.setTitle(TITLE);
		p2.setBrand(BRAND);
		p2.setPrice(790300.9);
		p2.setStock(0);

		Product p3 = new Product();
		p3.setId("1234");
		p3.setEan(EAN);
		p3.setTitle(TITLE);
		p3.setBrand("Another Brand");
		p3.setPrice(820900.9);
		p3.setStock(1);

		list.add(p1);
		list.add(p2);
		list.add(p3);

		map.put(BRAND, list);

		return map;
	}
}
