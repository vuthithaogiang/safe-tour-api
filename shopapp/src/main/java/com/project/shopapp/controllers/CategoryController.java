package com.project.shopapp.controllers;



import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.project.shopapp.services.CategoryService;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    //Dependency Injection Service
    private final CategoryService categoryService;


//    @Autowired
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }

    @PostMapping()
    public ResponseEntity<?> insertCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages =  result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully.");
    }

    @GetMapping() //http://localhost/8088/api/v1/categories?page=1&limit=20
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam ("page") int page,
            @RequestParam ("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }



    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category id: " + id + " successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category id: " + id + " successfully");
    }

}
