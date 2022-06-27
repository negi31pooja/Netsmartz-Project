package com.backend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.Product;
import com.backend.exception.ProductNotFoundException;
import com.backend.repository.ProductRepository;
import com.backend.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	// CREATE

	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		return ResponseEntity.ok(productRepository.save(product));
	}

	// READ

	@GetMapping
	public ResponseEntity<Iterable<Product>> getProductList() {
		return ResponseEntity.ok(productRepository.findAll());
	}

	// READ BY ID

	@GetMapping("/{id}")
	public ResponseEntity<Product> findProduct(@PathVariable Integer id) {
		return ResponseEntity.ok(
				productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found")));
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<String> handleProductNotFOundException() {
		return ResponseEntity.ok("Product Not Found");
	}

	// UPDATE

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
		Product p = productRepository.findById(product.getId()).get();
		p.setCategory(product.getCategory());
		p.setName(p.getName());
		p.setPrice(p.getPrice());
		p.setStock(p.getStock());
		p = productRepository.save(p);
		return ResponseEntity.ok(p);
	}

	// DELETE

	@DeleteMapping("/delete")
	public void deleteAll() {
		productRepository.deleteAll();
	}

	// DELETE BY ID

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
		productRepository.deleteById(id);
		return ResponseEntity.ok("Deleted");
	}

	// PAGINATION

	@GetMapping("/pagination")
	public ResponseEntity<List<Product>> getProductPagination(@RequestParam int page, @RequestParam int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Product> list = productRepository.findAll(pageable).getContent();
		return ResponseEntity.ok(list);
	}

	// DOWNLOAD LIST

	@GetMapping("/download")
	public ResponseEntity<ByteArrayResource> downloadFile() throws IOException {
		File file = productService.getFile();
		byte[] data = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		ByteArrayResource byteData = new ByteArrayResource(data);
		MediaType type = MediaType.parseMediaType("appication/txt");
		return (ResponseEntity<ByteArrayResource>) ResponseEntity.ok()
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
						"attachment;filename=" + file.getName())
				.contentType(type).contentLength((int) file.length()).body(byteData);
	}

}
