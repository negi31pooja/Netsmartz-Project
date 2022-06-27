package com.backend.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.entity.Product;
import com.backend.repository.ProductRepository;
import com.backend.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public File getFile() {
		File file = new File("file.txt");
		try {
			List<Product> productList = (List<Product>) productRepository.findAll();

			try (FileOutputStream fos = new FileOutputStream(file); PrintWriter pw = new PrintWriter(fos)) {
				productList.forEach(product -> pw.println(product));
				pw.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

}
