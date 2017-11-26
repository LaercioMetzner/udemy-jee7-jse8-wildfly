package com.library.app.category.services.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices{
	
	Validator validator;
	CategoryRepository categoryRepository;

	@Override
	public Category add(Category category) {
		
		Set<ConstraintViolation<Category>> errors = validator.validate(category);
		errors.stream().forEach((e) -> {
			throw new FieldNotValidException(e.getPropertyPath().toString(), e.getMessage());
		});
		
		if (categoryRepository.alreadyExists(category)) {
			throw new CategoryExistentException();
		}
		
		return categoryRepository.add(category);
	}

}