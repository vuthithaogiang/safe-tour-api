package com.project.shopapp.controllers;



import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.responses.DeleteCategoryResponse;
import com.project.shopapp.responses.UpdateCategoryResponse;
import com.project.shopapp.utils.LocalizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.project.shopapp.services.CategoryService;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    //Dependency Injection Service
    private final CategoryService categoryService;

    private final LocalizationUtil localizationUtil;


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
        Category category =  categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(category);
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
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult result,
                                            HttpServletRequest request

    ){

        try{
            if(result.hasErrors()) {
                List<String> errorMessages =  result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }
            categoryService.updateCategory(id, categoryDTO);

            return ResponseEntity.ok(UpdateCategoryResponse.builder()
                            .message(localizationUtil.getLocalizationMessage(
                                    "category.update.update_successfully", request))
                            .build());

        }catch (Exception e) {
            return  ResponseEntity.badRequest().body(UpdateCategoryResponse.builder()
                            .message(e.getMessage())
                    .build());

        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteCategoryResponse> deleteCategory(@PathVariable Long id,
                                                                 HttpServletRequest request) {

        try{
            categoryService.deleteCategory(id);

            return ResponseEntity.ok(DeleteCategoryResponse.builder()
                            .message(localizationUtil.getLocalizationMessage("category.delete.delete_successfully",
                                    request))
                    .build());

        }

        catch (Exception e) {

            return ResponseEntity.badRequest().body(DeleteCategoryResponse.builder()
                            .message(e.getMessage())
                    .build());

        }

    }

}
