package com.shoppinglist.webservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppinglist.webservice.entities.ProductJPA;
import com.shoppinglist.webservice.entities.ShoppingListElementJPA;

public interface ProductRepository extends JpaRepository<ProductJPA, Long> {
	List<ProductJPA> findByName(String name);
	List<ProductJPA> findByProducer(String producer);
	List<ProductJPA> findByPrice(float price);
	List<ProductJPA> findByListElement(ShoppingListElementJPA listElement);
}
