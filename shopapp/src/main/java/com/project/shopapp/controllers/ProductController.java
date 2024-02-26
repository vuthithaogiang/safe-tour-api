package com.project.shopapp.controllers;


import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam ("page") int page,
            @RequestParam ("limit") int limit){

        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("createdAt").descending()
        );

        Page<ProductResponse> productPage = productService
                .getAllProduct(pageRequest);

        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products  = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse
                .builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(
            @PathVariable("id") Long id) {

        try{
            Product existingProduct = productService .getProductById(id);
            return ResponseEntity.ok(ProductResponse.convertFromProduct(existingProduct));

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "")
    public ResponseEntity<?> insertProduct(@Valid @RequestBody ProductDTO productDTO,
                                          // @ModelAttribute("files") List<MultipartFile> files,
                                          //  @RequestPart("file") MultipartFile file,
                                           BindingResult result) {

        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);

            }
            Product newProduct =  productService.createProduct(productDTO);

          //  List<MultipartFile> files = productDTO.getFiles();
            return ResponseEntity.ok(newProduct);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") long productId,
                                          @ModelAttribute("files") List<MultipartFile> files) {
       try{
           Product existingProduct = productService.getProductById(productId);
           files = files == null ? new ArrayList<>() : files;

           if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
              return ResponseEntity.badRequest().body("Do not insert larger " +
                      ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " file images for 1 Product");
           }

           List<ProductImage> productImages = new ArrayList<>();

           for(MultipartFile file: files) {
               if(file != null) {
                   if(file.getSize() == 0) {
                       continue;
                   }
                   // >10MB
                   if( file.getSize()> 10 * 1024 * 1024) {
                       return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                               .body("File is too large, maximum size file is 10MB");
                   }

                   String contentType = file.getContentType();
                   if(contentType == null || !contentType.startsWith("image/")){
                       return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                               .body("File must be an image");
                   }
                   String fileName = storeFile(file);

                   // Create Object ProductImage to store
                   ProductImage productImage =  productService.createProductImage(
                           existingProduct.getId(),
                           ProductImageDTO
                                   .builder()
                                   //.productId(existingProduct.getId())
                                   .imageUrl(fileName)
                                   .build());
                   // Add to list
                   productImages.add(productImage);

                   // Store `fileName` into table: product_images
               }
           }

           return  ResponseEntity.ok(productImages);
       }

       catch (Exception exception){
           return ResponseEntity.badRequest().body(exception.getMessage());
       }
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDTO productDTO) {

        try{
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {

        try{
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product id: " + id);

        }

        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    //@PostMapping("/generate-fake-products")
    private ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();

        for(int i = 0; i < 1000; i++) {
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)) {
                continue;
            }

            ProductDTO productDTO = ProductDTO
                    .builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 9999))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1, 3))
                    .thumbnail("")
                    .build();

           try{
               productService.createProduct(productDTO);
           }

           catch (Exception e) {
               return ResponseEntity.badRequest().body(e.getMessage());
           }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }


    public String storeFile(MultipartFile file) throws IOException{
        if(!isImageFile(file) || file.getOriginalFilename() == null ) {
            throw new IOException("Invalid image format");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");

        if(!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;

    }

    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
