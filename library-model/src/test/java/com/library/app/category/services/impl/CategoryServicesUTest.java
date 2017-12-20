package com.library.app.category.services.impl;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;


public class CategoryServicesUTest {

	private CategoryServices categoryServices;
	private CategoryRepository categoryRepository;
	private Validator validator;

	@Before
	public void initTestCase() {

		validator = Validation.buildDefaultValidatorFactory().getValidator();
		categoryServices = new CategoryServicesImpl();
		categoryRepository = mock(CategoryRepository.class);
		((CategoryServicesImpl) categoryServices).validator = validator;
		((CategoryServicesImpl) categoryServices).categoryRepository = categoryRepository;

	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}

	@Test
	public void addCategoryWithShortName() {
		addCategoryWithInvalidName("A");
	}

	@Test
	public void addCategoryWithLongName() {
		addCategoryWithInvalidName("This is a much longer name that is going to cause an exception to be thrown");
	}

	@Test(expected = CategoryExistentException.class)
	public void addCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(java())).thenReturn(true);
		categoryServices.add(java());
	}

	@Test()
	public void addValidCategory() {

		when(categoryRepository.alreadyExists(java())).thenReturn(false);
		categoryServices.add(java());
		when(categoryRepository.add(java())).thenReturn(categoryWithId(java(), 1L));

		Category categoryAdded = categoryServices.add(java());
		assertThat("Category should have the id of 1l", categoryAdded.getId(), is(equalTo(1L)));
	}

	@Test(expected = CategoryExistentException.class)
	public void updateCategoryWithExistentName() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(true);
		categoryServices.update(categoryWithId(java(), 1L));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void UpdateCategoryNotFound() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.existsById(1L)).thenReturn(false);

		categoryServices.update(categoryWithId(java(), 1L));

	}
	
	@Test
	public void updateValidCategory() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
		when(categoryRepository.existsById(1L)).thenReturn(true);
		categoryServices.update(categoryWithId(java(), 1L));
		
		verify(categoryRepository).update(categoryWithId(java(), 1L));
	}
	
	@Test
	public void findCategoryById() {
		when(categoryRepository.findById(1L)).thenReturn(categoryWithId(java(), 1L));
		final Category category = categoryServices.findById(1L);
		assertThat("Valid category wasn't found", category, is(notNullValue()));
		assertThat("Id should be 1L", category.getId(), is(equalTo(1L)));
		assertThat("Category name should be Java", category.getName(), is(equalTo(java().getName())));
	}
	
	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdNotFound() {
		when(categoryRepository.findById(1L)).thenReturn(null);
		final Category category = categoryServices.findById(1L);		
	}
	
	@Test
	public void findAllNoCategories() {
		when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());
		List<Category> categories = categoryServices.findAll();
		assertThat("Categories should have no elements at all", categories.isEmpty(), is(equalTo(true)));
	}
	
	@Test
	public void findAllCategories() {
		when(categoryRepository.findAll("name")).thenReturn(Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));
		List<Category> categories = categoryServices.findAll();
		assertThat("Categories should have two elements", categories.size(), is(equalTo(2)));
		assertThat("First element is java", categories.get(0), is(equalTo(categoryWithId(java(), 1L))));
		assertThat("Second element is network", categories.get(1), is(equalTo(categoryWithId(networks(), 2L))));
	}

	public void addCategoryWithInvalidName(String name) {
		try {
			categoryServices.add(new Category(name));
			fail("An error should have been trown");
		} catch (FieldNotValidException e) {
			assertThat("Category Name should be the field that caused the exception", e.getFieldName(),
					is(equalTo("name")));
		}
	}

	private void updateCategoryWithInvalidName(String name) {
		try {
			categoryServices.update(new Category(name));
			fail("An exception should have been thrown");
		} catch (FieldNotValidException e) {
			assertThat("Category Name should be the field that caused the exception", e.getFieldName(),
					is(equalTo("name")));
		}
	}

	@Test
	public void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);
	}

	@Test
	public void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("a");
	}

	@Test
	public void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("hahauehsdslfkjsdadlkjflçkjsdskfçskddffçlkslç sdsdflçskd fçasldsjf ");
	}

}
