package com.project.shopapp.services;


import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements  ICategoryService{

    private final CategoryRepository categoryRepository;

//    public CategoryService(CategoryRepository categoryRepository){
//        this.categoryRepository = categoryRepository;
//    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
            if(categoryRepository.existsByName(categoryDTO.getName())) {
                throw  new RuntimeException("Category's name have existed");
            }

            Category newCategory = Category
                    .builder()
                    .name(categoryDTO.getName())
                    .build();
            return  categoryRepository.save(newCategory);

    }

    @Override
    public Category getCategoryById(long id) {
        return  categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category id not found!"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long id, CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existingCategory);

    }

    @Override
    public void deleteCategory(long id) {

        categoryRepository.deleteById(id);
    }
}
