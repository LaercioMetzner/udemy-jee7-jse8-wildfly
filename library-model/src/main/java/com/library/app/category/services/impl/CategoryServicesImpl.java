package com.library.app.category.services.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices{
	
	Validator validator;

	@Override
	public Category add(Category category) throws FieldNotValidException {
		Set<ConstraintViolation<Category>> errors = validator.validate(category);
		errors.stream().forEach((e) -> {
			throw new FieldNotValidException(e.getPropertyPath().toString(), e.getMessage());
		});
		return null;
	}

}
