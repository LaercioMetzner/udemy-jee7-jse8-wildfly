package com.library.app.category.repository;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
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
		categoryAddedId = dBCommandTransactionalExecutor.executeCommand(() -> categoryRepository.add(java()).getId());
		assertThat(categoryAddedId, is(notNullValue()));

		final Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@Test
	public void findCategoryByIdNotFound() {
		final Category category = categoryRepository.findById(999L);
		assertThat("Category should be null", category, is(nullValue()));
	}

	@Test
	public void findCategoryByIdWithNullId() {
		final Category category = categoryRepository.findById(null);
		assertThat("Category should be null", category, is(nullValue()));
	}

	@Test
	public void updateCategory() {

		final Long categoryAddId = dBCommandTransactionalExecutor
				.executeCommand(() -> categoryRepository.add(java()).getId());

		final Category categoryAfterAdd = categoryRepository.findById(categoryAddId);
		assertThat("Category after insert is null", categoryAfterAdd, is(notNullValue()));
		assertThat("Category name should be java", categoryAfterAdd.getName(), is(equalTo(java().getName())));

		categoryAfterAdd.setName(cleanCode().getName());
		dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.update(categoryAfterAdd);
			return null;
		});

		final Category categoryAfterUpdate = categoryRepository.findById(categoryAddId);
		assertThat("Category after update is null ", categoryAfterUpdate, is(notNullValue()));
		assertThat("Category name after update is Clean Code", categoryAfterUpdate.getName(),
				is(equalTo(cleanCode().getName())));

	}

	@Test
	public void findAllCategories() {

		dBCommandTransactionalExecutor.executeCommand(() -> {
			allCategories().stream().forEach(categoryRepository::add);
			return null;
		});

		final List<Category> categories = categoryRepository.findAll("name");

		assertThat("List is null", categories, is(notNullValue()));
		assertThat("List should have a length of four", categories.size(), is(equalTo(4)));
		assertThat(String.format("Element at %1$d is called %2$s ", 0, architecture().getName()),
				categories.get(0).getName(), is(equalTo(architecture().getName())));
		assertThat(String.format("Element at %1$d is called %2$s ", 1, cleanCode().getName()),
				categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(String.format("Element at %1$d is called %2$s ", 2, java().getName()), categories.get(2).getName(),
				is(equalTo(java().getName())));
		assertThat(String.format("Element at %1$d is called %2$s ", 3, networks().getName()),
				categories.get(3).getName(), is(equalTo(networks().getName())));
	}

	@Test
	public void alreadyExistsForAdd() {

		dBCommandTransactionalExecutor.executeCommand(() -> categoryRepository.add(java()));

		assertThat("Java should exists", categoryRepository.alreadyExists(java()), is(equalTo(true)));
		assertThat("CleanCode should exists", categoryRepository.alreadyExists(cleanCode()), is(equalTo(false)));

	}

	@Test
	public void alreadyExistsCategoryWithId() {

		final Category java = dBCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.add(cleanCode());
			return categoryRepository.add(java());
		});

		assertThat("Shoud not be the same cause it's the same id", categoryRepository.alreadyExists(java),
				is(equalTo(false)));

		java.setName(cleanCode().getName());
		assertThat("Should exist cause has the same code and different names, with a name that exists in the database",
				categoryRepository.alreadyExists(java), is(equalTo(true)));

		java.setName(networks().getName());
		assertThat(
				"Should not exist cause has the same code and different names, with a name that doesn't exists in the database",
				categoryRepository.alreadyExists(java), is(equalTo(false)));

	}

	@Test
	public void exitstById() {

		final Long categoryAddedId = dBCommandTransactionalExecutor
				.executeCommand(() -> categoryRepository.add(java()).getId());

		assertThat("Just added category should exists", categoryRepository.existsById(categoryAddedId),
				is(equalTo(true)));
		assertThat("category with id 999 should not exists", categoryRepository.existsById(999L), is(equalTo(false)));

	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

}
