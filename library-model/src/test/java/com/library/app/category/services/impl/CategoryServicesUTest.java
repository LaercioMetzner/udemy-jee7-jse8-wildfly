package com.library.app.category.services.impl;


import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesUTest {
	
	private CategoryServices categoryServices;
	private Validator validator;
	
	@Before
	public void initTestCase() {

		validator = Validation.buildDefaultValidatorFactory().getValidator();		
		categoryServices = new CategoryServicesImpl();
		((CategoryServicesImpl) categoryServices).validator = validator;

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
	
	public void addCategoryWithInvalidName(String name) {
		try {
			categoryServices.add(new Category(name));
			fail("An error should have been trown");
		} catch (FieldNotValidException e) {
			assertThat("Category Name should be the field that caused the exception", e.getFieldName(), is(equalTo("name")));
		}
	}

	
}
