package com.library.app.category.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	public Object alreadyExists(Category category) {

		final String jpql = "Select 1 From Category e Where e.name = :name";
		final Query query = em.createQuery(jpql);
		query.setParameter("name", category.getName());
		int toCheck = query.setMaxResults(1).getResultList().size();
		boolean toReturn = toCheck > 0; 
		return toReturn;
		
	}

}
