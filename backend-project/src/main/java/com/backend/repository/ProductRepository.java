package com.backend.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.backend.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

}
