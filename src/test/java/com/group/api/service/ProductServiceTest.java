package com.group.api.service;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.group.api.model.Product;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

	@Autowired
	private ProductService service;

	private static final String EAN = "19941101";
	private static final String BRAND = "nikana";
	
	@Before
	public void setUp() {
		// BDDMockito.given(service.defaultGroup(returnProductList())).willReturn(new
		// HashMap<String, List<Product>>());
	}

	/**
	 * Método deve agrupar os itens na seguinte ordem de prioridade
	 * EAN -> titulo -> marca
	 * Espera-se que a lista enviada seja agrupada pela chave nikana e contenha 3 itens
	 */
	@Test
	public void testDefaultGroup() {
		List<Product> list = returnProductList();
		Map<String, List<Product>> map = this.service.defaultGroup(list);
		assertTrue(map.get("nikana").size() == 3);
	}
	
	/**
	 * Método que ordena os itens já agrupados com a seguinte prioridade
	 * Estoque (desc) -> Preço (asc)
	 * espera-se que o priemiro item seja o de id = 2, stock = 2 e preço = 2 
	 */
	@Test
	public void testDefautlOrder() {
		Map<String, List<Product>> map = returnMap();
		Map<String, List<Product>> orderedMap = this.service.defaultOrder(map);
		
		assertTrue(orderedMap.get(BRAND).get(0).getId().equals("2"));
		assertTrue(orderedMap.get(BRAND).get(0).getStock().equals(2));
		assertTrue(orderedMap.get(BRAND).get(0).getPrice().equals(4.0));
	}
	
	/**
	 * Teste que agrupa a lista de acordo com o atributo de classe escolhido e passado por parametro
	 * Caso não for encontrado por padrão será agrupado por EAN identicos
	 * 
	 * Testando aqui agrupamento por marca
	 * Deve funcionar passando o atributo maísculo também
	 * @throws Exception 
	 */
	@Test
	public void testGroupByBrand() throws Exception {
		List<Product> list = returnProductList();
		Map<String, List<Product>> groupedMap = this.service.groupBy(list, "BRAND");
		
		assertTrue(!groupedMap.get(BRAND).isEmpty());
		assertTrue(groupedMap.get(BRAND).size() == 3);
	}
	
	/**
	 * Teste que agrupa a lista de acordo com o atributo de classe escolhido e passado por parametro
	 * Caso não for encontrado por padrão será agrupado por EAN identicos
	 * 
	 * Testando aqui agrupamento por EAN
	 * @throws Exception 
	 */
	@Test
	public void testGroupByEAN() throws Exception {
		List<Product> list = returnProductList();
		Map<String, List<Product>> groupedMap = this.service.groupBy(list, "ean");
		
		assertTrue(!groupedMap.get(EAN).isEmpty());
		assertTrue(groupedMap.get(EAN).size() == 3);
	}
	
	/**
	 * Teste que agrupa a lista de acordo com o atributo de classe escolhido e passado por parametro
	 * Caso não for encontrado por padrão será retornada uma exceção
	 * 
	 * Testando aqui agrupamento passando um atributo inexistente, deve retornar uma mensagem informando a exceção
	 * @throws Exception 
	 */
	@Test(expected = Exception.class)
	public void testGroupByNonexistentAttribute() throws Exception {
		List<Product> list = returnProductList();
		this.service.groupBy(list, "description");
	}
	
	/**
	 * Orderna a lista de um MAP de acordo com o atributo da classe passado como parametro 
	 * 
	 * ORdenando por id em forma crescente
	 * @throws Exception 
	 */
	@Test
	public void testOrderByIdAsc() throws Exception {
		Map<String, List<Product>> map = returnMap();
		Map<String, List<Product>> orderedMap = this.service.orderBy(map, "ID:asc");
		
		assertTrue(orderedMap.get(BRAND).get(0).getId().equals("1"));
		assertTrue(orderedMap.get(BRAND).get(1).getId().equals("2"));
		assertTrue(orderedMap.get(BRAND).get(2).getId().equals("3"));
	}
	
	/**
	 * Orderna a lista de um MAP de acordo com o atributo da classe passado como parametro 
	 * 
	 * 
	 * Ordenando por id em forma decrescente
	 * @throws Exception 
	 */
	@Test
	public void testOrderByIdDesc() throws Exception {
		Map<String, List<Product>> map = returnMap();
		Map<String, List<Product>> orderedMap = this.service.orderBy(map, "ID:DESC");
		
		assertTrue(orderedMap.get(BRAND).get(0).getId().equals("3"));
		assertTrue(orderedMap.get(BRAND).get(1).getId().equals("2"));
		assertTrue(orderedMap.get(BRAND).get(2).getId().equals("1"));
	}
	
	/**
	 * Tentado ordenar lista passando o parâmetro de forma inválida
	 * 
	 * No parâmetro deve ser passado o campo que deseja-se ordenação e a orientação
	 * Exemplos: id:asc ou ead:desc
	 * 
	 *  Caso não for encontrado os dois parâmetros uma exception é retornada
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void testOrderByIdInvalidFormat() throws Exception {
		Map<String, List<Product>> map = returnMap();
		this.service.orderBy(map, "ID");
	}
	
	/**
	 * Tentado ordenar lista passando o parâmetro de forma inválida
	 * 
	 * No parâmetro deve ser passado o campo que deseja-se ordenação e a orientação
	 * Exemplos: id:asc ou ead:desc
	 * 
	 *  Caso não for encontrado os dois parâmetros uma exception é retornada
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void testOrderByIdInvalidFormatOrientation() throws Exception {
		Map<String, List<Product>> map = returnMap();
		this.service.orderBy(map, "ID:");
	}
	
	/**
	 * Tentado ordenar lista passando um parâmetro que não existe
	 * 
	 * No parâmetro deve ser passado o campo que deseja-se ordenação e a orientação
	 * Exemplos: id:asc ou ead:desc
	 * 
	 * Quando o attributo não for encontrado uma exception é retornada
	 * @throws Exception
	 */
	@Test(expected = Exception.class)
	public void testOrderByIdInvalidAttribute() throws Exception {
		Map<String, List<Product>> map = returnMap();
		this.service.orderBy(map, "description");
	}
	
	
	private List<Product> returnProductList() {
		int aux = 0;
		double price = 4;
		List<Product> pList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			aux++;
			price--;
			Product p = new Product();
			p.setBrand(BRAND);
			p.setEan(EAN);
			p.setId(String.valueOf(aux));
			p.setPrice(i == 1 ? 4.0 : price);
			p.setStock(aux == 3 ? 2 : aux); //forçando repetição de estoques para conferir ordenação
			p.setTitle(RandomStringUtils.randomAlphabetic(20));

			pList.add(p);
		}

		return pList;
	}

	/**
	 * Retorna mapa agrupado por marca
	 * @return
	 */
	private Map<String, List<Product>> returnMap() {
		Map<String, List<Product>> map = new HashMap<>();
		List<Product> list = returnProductList();
		
		map.put(BRAND, list);
		
		return map;
	}
}
