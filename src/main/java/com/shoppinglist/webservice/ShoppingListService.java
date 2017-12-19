package com.shoppinglist.webservice;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shoppinglist.api.Priority;
import com.shoppinglist.api.ProductDTO;
import com.shoppinglist.api.ShoppingListDTO;
import com.shoppinglist.api.ShoppingListElementDTO;
import com.shoppinglist.webservice.entities.ProductJPA;
import com.shoppinglist.webservice.entities.ShoppingListElementJPA;

@RestController
@RequestMapping("/shopping-list")
public class ShoppingListService {

	private ProductRepository productRepository;
	private ShoppingListRepository shoppingListRepository;

	@Autowired
	ShoppingListService(ProductRepository productRepository, ShoppingListRepository shoppingListReposiroty) {
		this.productRepository = productRepository;
		this.shoppingListRepository = shoppingListReposiroty;
	}

	@RequestMapping(method = RequestMethod.GET)
	ShoppingListDTO readList() {
		return new ShoppingListDTO(translateElementList());
	}

	@RequestMapping(value = "/single", method = RequestMethod.GET)
	private ShoppingListElementDTO getSingle() {
		return translateShoppingListElement(shoppingListRepository.findOne(1L));
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	private List<ProductDTO> getProducts() {
		List<ProductDTO> list = new ArrayList<ProductDTO>();
		for(ProductJPA productJPA : productRepository.findAll()) {
			list.add(translateProduct(productJPA));
		}
		return list;
	}
	
	@RequestMapping(value = "/element", method = RequestMethod.GET)
	private ShoppingListDTO getElement() {
		return new ShoppingListDTO(translateShoppingListElement(shoppingListRepository.findOne(1L)));
	}

	@RequestMapping(value = "element/{id}", method = RequestMethod.GET)
	private ShoppingListDTO getListElementById(@PathVariable Long id) {
		return new ShoppingListDTO(translateShoppingListElement(shoppingListRepository.findOne(id)));
	}

	@RequestMapping(value = "priority/{priority}", method = RequestMethod.GET)
	private ShoppingListDTO getByPriority(@PathVariable String priority) {
		Priority p;
		switch (priority.toUpperCase()) {
		case "LOW":
			p = Priority.LOW;
			break;
		case "MEDIUM":
			p = Priority.MEDIUM;
			break;
		case "HIGH":
			p = Priority.HIGH;
			break;
		default:
			p = Priority.DEFAULT;
			break;
		}

		return new ShoppingListDTO(translateElementList(shoppingListRepository.findByPriority(p)));
	}

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@RequestBody ShoppingListElementDTO input) {
		ShoppingListElementJPA element = translateElementJPA(input);
		this.shoppingListRepository.save(element);
		for (ProductDTO product : input.getProducts()) {
			this.productRepository.save(translateProductJPA(product, element));
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(element.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.PUT, value="edit/{id}")
	ResponseEntity<?> edit(@RequestBody ShoppingListElementDTO input, @PathVariable Long id) {
		ShoppingListElementJPA elementJPA = this.shoppingListRepository.findOne(id);
		if (elementJPA.getText() != input.getText())
			elementJPA.setText(input.getText());
		if (elementJPA.getPriority() != input.getPriority())
			elementJPA.setPriority(input.getPriority());
		int i = 0;
		for (ProductDTO productDTO : input.getProducts()) {
			if (elementJPA.getProducts().size() <= i) {
				this.productRepository.save(translateProductJPA(productDTO, elementJPA));
			} else {
				ProductJPA productJPA = elementJPA.getProducts().get(i);
				if (productJPA.getName() != productDTO.getName())productJPA.setName(productDTO.getName());
				if (productJPA.getProducer() != productDTO.getProducer())productJPA.setProducer(productDTO.getProducer());
				if (productJPA.getPrice() != productDTO.getPrice())productJPA.setPrice(productDTO.getPrice());
				this.productRepository.saveAndFlush(productJPA);
			}
			i++;
		}
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="delete/{id}")
	ResponseEntity<?> delete(@PathVariable Long id){
		ShoppingListElementJPA elementJPA = this.shoppingListRepository.findOne(id);
		for(ProductJPA product : elementJPA.getProducts()) {
			this.productRepository.delete(product);
		}
		this.shoppingListRepository.delete(elementJPA);
		return ResponseEntity.ok().build();
	}

	private ShoppingListElementJPA translateElementJPA(ShoppingListElementDTO elementDTO) {
		return new ShoppingListElementJPA(elementDTO.getPriority(), elementDTO.getText());
	}

	private ProductJPA translateProductJPA(ProductDTO productDTO, ShoppingListElementJPA element) {
		return new ProductJPA(element, productDTO.getName(), productDTO.getProducer(), productDTO.getPrice());
	}

	private List<ProductJPA> translateProductListJPA(ShoppingListElementDTO elementDTO,
			ShoppingListElementJPA elementJPA) {
		List<ProductJPA> products = new ArrayList<ProductJPA>();
		for (ProductDTO productDTO : elementDTO.getProducts()) {
			products.add(translateProductJPA(productDTO, elementJPA));
		}
		return products;
	}

	private List<ShoppingListElementDTO> translateElementList(List<ShoppingListElementJPA> elements) {
		List<ShoppingListElementDTO> elementsDTO = new ArrayList<ShoppingListElementDTO>();
		for (ShoppingListElementJPA elementJPA : elements) {
			elementsDTO.add(translateShoppingListElement(elementJPA));
		}
		return elementsDTO;
	}

	private List<ShoppingListElementDTO> translateElementList() {
		List<ShoppingListElementDTO> elements = new ArrayList<ShoppingListElementDTO>();
		for (ShoppingListElementJPA elementJPA : shoppingListRepository.findAll()) {
			elements.add(translateShoppingListElement(elementJPA));
		}
		return elements;
	}

	private List<ProductDTO> translateProductList(ShoppingListElementJPA element) {
		List<ProductDTO> products = new ArrayList<ProductDTO>();
		for (ProductJPA productJPA : productRepository.findByListElement(element)) {
			products.add(translateProduct(productJPA));
		}
		return products;
	}

	private ShoppingListElementDTO translateShoppingListElement(ShoppingListElementJPA elementJPA) {
		return new ShoppingListElementDTO(elementJPA.getText(), elementJPA.getPriority(),
				translateProductList(elementJPA));
	}

	private ProductDTO translateProduct(ProductJPA productJPA) {
		return new ProductDTO(productJPA.getName(), productJPA.getProducer(), productJPA.getPrice());
	}
}
