package com.shoppinglist.webservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class ProductJPA {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column private String name;	
	@Column private String producer;
	@Column private float price;
	
	@ManyToOne
	@JoinColumn(name="elementid", referencedColumnName="id")
	private ShoppingListElementJPA listElement;
	
	public ProductJPA() {}	//For JPA
	public ProductJPA(ShoppingListElementJPA element, String name) {
		this.listElement = element;
		this.name = name;
		this.producer = "";
		this.price = 0;
	}
	public ProductJPA(ShoppingListElementJPA element, String name, float price) {
		this.listElement = element;
		this.name = name;
		this.producer = "";
		this.price = price;
	}
	public ProductJPA(ShoppingListElementJPA element, String name, String producer) {
		this.listElement = element;
		this.name = name;
		this.producer = producer;
		this.price = 0;
	}
	public ProductJPA(ShoppingListElementJPA element, String name, String producer, float price) {
		this.listElement = element;
		this.name = name;
		this.producer = producer;
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Long getId() {
		return id;
	}	
}
