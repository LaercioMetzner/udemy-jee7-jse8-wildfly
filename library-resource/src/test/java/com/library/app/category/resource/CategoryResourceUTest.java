package com.library.app.category.resource;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.app.category.services.CategoryServices;

public class CategoryResourceUTest {

	private CategoryResource categoryResource;
	
	@Mock
	private CategoryServices categoryServices;
	
	@Before
	public void initTestCase() {
		
		MockitoAnnotations.initMocks(this);
		
		categoryResource = new CategoryResource();
		categoryResource.categoryServices = categoryServices;
	}
	
}
