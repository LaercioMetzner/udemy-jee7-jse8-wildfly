package com.library.app.category.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.library.app.category.model.Category;

public class CategoryRepository {

	EntityManager em;

	public Category add(final Category category) {
		em.persist(category);
		return category;
	}

	public Category findById(final Long id) {
		if (id == null) {
			return null;
		}
		return em.find(Category.class, id);
	}

	public void update(Category categoryAfterAdd) {
		em.merge(categoryAfterAdd);		
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAll(String orderField) {
		return em.createQuery(String.format("Select e From Category e Order by %s", orderField)).getResultList();
	}

}
