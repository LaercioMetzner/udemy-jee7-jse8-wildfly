package com.library.app.category.repository;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.commontests.utils.DBCommand;
import com.library.app.commontests.utils.DBCommandTransactionalExecutor;

public class CategoryRepositoryUTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;

	@Before
	public void initTestCase() {

		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();
		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(em);

	}

	@Test
	public void addCategoryAndFindIt() {
		Long categoryAddedId = null;
		try {
			categoryAddedId = dBCommandTransactionalExecutor.executeCommand(() -> categoryRepository.add(java()).getId());
			assertThat(categoryAddedId, is(notNullValue()));
		} catch (final Exception e) {
			fail("This exception should not have been thrown");
			e.printStackTrace();
		}

		final Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

}
