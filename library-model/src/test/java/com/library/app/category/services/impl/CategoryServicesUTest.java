package com.library.app.category.services.impl;


import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistentException;
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
	
	public void addCategoryWithInvalidName(String name) {
		try {
			categoryServices.add(new Category(name));
			fail("An error should have been trown");
		} catch (FieldNotValidException e) {
			assertThat("Category Name should be the field that caused the exception", e.getFieldName(), is(equalTo("name")));
		}
	}
	
	private void updateCategoryWithInvalidName(String name) {
		try {
			categoryServices.update(new Category(name));
			fail("An exception should have been thrown");
		} catch (FieldNotValidException e) {
			assertThat("Category Name should be the field that caused the exception", e.getFieldName(), is(equalTo("name")));
		}
	}
	
	@Test
	public  void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);		
	}
	
	@Test
	public  void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("a");
	}
	
	@Test
	public  void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("hahauehsdslfkjsdadlkjflçkjsdskfçskddffçlkslç sdsdflçskd fçasldsjf ");
	}

	
}
