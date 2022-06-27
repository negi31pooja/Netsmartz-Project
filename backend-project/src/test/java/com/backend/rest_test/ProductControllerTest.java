package com.backend.rest_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import com.backend.entity.Product;
import com.backend.repository.ProductRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

	@Autowired
	ProductRepository pRepo;

	// CREATE

	@Test
	@Order(1)
	public void testCreate() {
		Product p = new Product();
		p.setId(11);
		p.setCategory("hardware");
		p.setName("microphone");
		p.setPrice(1770);
		p.setStock(45);
		pRepo.save(p);
		assertNotNull(pRepo.findById(11).get());
	}

	// READ

	@Test
	@Order(2)
	public void testReadAll() {
		List<Product> list = (List<Product>) pRepo.findAll();
		assertThat(list).size().isGreaterThan(0);
	}

	// READ BY ID

	@Test
	@Order(3)
	public void testSingleProduct() {
		Product product = pRepo.findById(5).get();
		assertEquals("cpu", product.getName());
	}

	// UPDATE

	@Test
	@Order(4)
	public void testUpdate() {
		Product p = pRepo.findById(5).get();
		p.setStock(70);
		pRepo.save(p);
		assertNotEquals(40, pRepo.findById(5).get().getStock());
	}

	// DELETE

	@Test
	@Order(5)
	public void testDelete() {
		pRepo.deleteById(10);
		assertThat(pRepo.existsById(10)).isFalse();
	}

}
