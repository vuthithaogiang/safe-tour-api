package com.project.shopapp.controllers;



import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.responses.DeleteCategoryResponse;
import com.project.shopapp.responses.InsertCategoryResponse;
import com.project.shopapp.responses.UpdateCategoryResponse;
import com.project.shopapp.components.LocalizationUtil;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.project.shopapp.services.CategoryService;

import java.util.ArrayList;
import java.util.List;



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
        try{
            if(result.hasErrors()) {
                List<String> errorMessages =  result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(InsertCategoryResponse
                        .builder()
                        .message(localizationUtil.getLocalizationMessage(MessageKeys.CATEGORY_CREATE_FAILED))
                        .errors(errorMessages)
                        .build());
            }
            Category category =  categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(InsertCategoryResponse
                    .builder()
                    .message(localizationUtil.getLocalizationMessage(MessageKeys.CATEGORY_CREATE_SUCCESSFULLY))
                    .category(category)
                    .build());

        }

        catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());

            return ResponseEntity.badRequest().body(InsertCategoryResponse
                    .builder()
                    .message(localizationUtil.getLocalizationMessage(MessageKeys.CATEGORY_CREATE_FAILED))
                    .errors(errors)
                    .build());

        }
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
                                            BindingResult result
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
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.CATEGORY_UPDATE_SUCCESSFULLY, id))
                            .build());

        }catch (Exception e) {
            return  ResponseEntity.badRequest().body(UpdateCategoryResponse.builder()
                            .message(e.getMessage())
                    .build());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteCategoryResponse> deleteCategory(@PathVariable Long id) {

        try{
            categoryService.deleteCategory(id);

            return ResponseEntity.ok(DeleteCategoryResponse.builder()
                            .message(localizationUtil.
                                    getLocalizationMessage(MessageKeys.CATEGORY_DELETE_SUCCESSFULLY, id))
                    .build());
        }

        catch (Exception e) {

            return ResponseEntity.badRequest().body(DeleteCategoryResponse.builder()
                            .message(e.getMessage())
                    .build());

        }

    }

}
