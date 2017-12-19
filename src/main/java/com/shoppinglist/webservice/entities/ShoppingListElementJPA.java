package com.shoppinglist.webservice.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.shoppinglist.api.Priority;

@Entity(name="listelement")
public class ShoppingListElementJPA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;
	
	@Column private Priority priority;	
	@Column	private String text;
	
	@OneToMany(mappedBy="listElement", cascade = {CascadeType.ALL})
	private List<ProductJPA> products = new ArrayList<ProductJPA>();
	
	public ShoppingListElementJPA() {} //For JPA
	public ShoppingListElementJPA(Priority priority) {
		this.priority = priority;
	}
	public ShoppingListElementJPA(String text) {
		this.text = text;
		this.priority = Priority.DEFAULT;
	}
	public ShoppingListElementJPA(Priority priority, String text) {
		this.priority = priority;
		this.text = text;
	}
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<ProductJPA> getProducts() {
		return products;
	}
	public void setProducts(List<ProductJPA> products) {
		this.products = products;
	}
	public Long getId() {
		return id;
	}
}
