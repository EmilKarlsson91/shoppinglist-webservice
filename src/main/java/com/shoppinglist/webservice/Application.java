package com.shoppinglist.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.shoppinglist.api.Priority;
import com.shoppinglist.webservice.entities.ProductJPA;
import com.shoppinglist.webservice.entities.ShoppingListElementJPA;

@SpringBootApplication
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	//Test data for products.
	private static String[] productNames = {"Ost", "Toalettpapper", "Mjölk"};
	private static String[] producers = {"Mjölkbolaget AB", "Ostiga Ostarna AB", "Torka baken AB"};
	
	//Test data for list element.
	private static Priority[] priorities = {Priority.HIGH, Priority.MEDIUM, Priority.LOW};
	private static String[] texts = {"Kan du köpa ost?", "Snälla åk förbi affären", "Nu åker du till affären!"};
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
	
	@Bean
	public CommandLineRunner init(ProductRepository productRepository, ShoppingListRepository shoppingListRepository) {
		return (args) -> {
			
			// save a couple of list elements
			addListElement(productRepository, shoppingListRepository);
			addListElement(productRepository, shoppingListRepository);
			addListElement(productRepository, shoppingListRepository);

			// fetch all list elements
			log.info("Elements found with findAll():");
			log.info("-------------------------------");
			for (ShoppingListElementJPA element : shoppingListRepository.findAll()) {
				log.info("Element id: " + element.getId());
				log.info("Product list length: " + element.getProducts().size());
			}
			log.info("");

			// fetch an element by id
			ShoppingListElementJPA element = shoppingListRepository.findOne(1L);
			log.info("List element found with findOne(1L):");
			log.info("--------------------------------");
			log.info("Element id: " + element.getId());
			log.info("Text: " + element.getText());
			log.info("Priority: " + element.getPriority());
			for (ProductJPA product : element.getProducts()) {
				log.info("--------------------------------------------");
				log.info("Name: " + product.getName());
				log.info("Producer: " + product.getProducer());
				log.info("Price: " + product.getPrice());
			}
			log.info("");

			// fetch match by sport
			
			for (ShoppingListElementJPA elementPriority : shoppingListRepository.findByPriority(Priority.HIGH)) {
				log.info("Element found with findByPriority(Priority.HIGH):");
				log.info("--------------------------------------------");
				log.info("Elemtn id: " + elementPriority.getId());
				for (ProductJPA product : elementPriority.getProducts()) {
					log.info("--------------------------------------------");
					log.info("Name: " + product.getName());
					log.info("Producer: " + product.getProducer());
					log.info("Price: " + product.getPrice());
				}
			}
			log.info("");
		};		
	}
	
	private void addListElement(ProductRepository productRepository, ShoppingListRepository shoppingListRepository) {
		ShoppingListElementJPA element = new ShoppingListElementJPA(priorities[(int) (Math.random() * priorities.length)], texts[(int) (Math.random() * texts.length)]);
		shoppingListRepository.save(element);
		for (int c = 0; c < 2; c++) {
			productRepository.save(new ProductJPA(element, productNames[(int) (Math.random() * productNames.length)], producers[(int) (Math.random() * producers.length)], (float) (100 * Math.random())));
		}	
	}
}
