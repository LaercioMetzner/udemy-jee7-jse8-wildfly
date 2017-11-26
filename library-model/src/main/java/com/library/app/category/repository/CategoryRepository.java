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

		final StringBuffer jpql = new StringBuffer();
		jpql.append("Select 1 From Category e Where e.name = :name");
		if (category.getId() != null) {
			jpql.append(" And  e.id != :id");
		}
		final Query query = em.createQuery(jpql.toString());
		query.setParameter("name", category.getName());
		if (category.getId() != null) {
			query.setParameter("id", category.getId());
		}
		int toCheck = query.setMaxResults(1).getResultList().size();
		boolean toReturn = toCheck > 0; 
		return toReturn;
		
	}

}
