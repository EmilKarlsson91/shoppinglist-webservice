package com.shoppinglist.webservice;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shoppinglist.api.Priority;
import com.shoppinglist.webservice.entities.ShoppingListElementJPA;

public interface ShoppingListRepository extends CrudRepository<ShoppingListElementJPA, Long> {
	List<ShoppingListElementJPA> findByPriority(Priority priotiry);
}
