package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private  final ProductImageRepository productImageRepository;


    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {

        Category existingCategory =  categoryRepository.
                findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Not  found Category id: "
                        + productDTO.getCategoryId()));

        Product newProduct =  Product
                .builder()
                .name(productDTO.getName())
                .thumbnail((productDTO.getThumbnail()))
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();

        return productRepository.save(newProduct);

    }

    @Override
    public Product getProductById(Long id) throws Exception {
         return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Not found Product" +
                        "id: " + id ));

    }

    @Override
    public Page<ProductResponse> getAllProduct(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(
                ProductResponse::convertFromProduct
        );
    }

    @Override
    public Product updateProduct(
            Long id,
            ProductDTO productDTO
    )
            throws Exception {
       Product existingProduct = productRepository
               .findById(id)
               .orElseThrow(() ->
                       new DataNotFoundException("Not found Product id: " + id));

       if(existingProduct != null) {
           Category existingCategory = categoryRepository
                   .findById(productDTO.getCategoryId())
                   .orElseThrow(() ->
                           new DataNotFoundException("Not Found Category id: " + productDTO.getCategoryId()) );

           existingProduct.setName(productDTO.getName());
           existingProduct.setCategory(existingCategory);
           existingProduct.setThumbnail(productDTO.getThumbnail());
           existingProduct.setDescription(productDTO.getDescription());
           existingProduct.setPrice(productDTO.getPrice());

           return productRepository.save(existingProduct);

       }
       return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        optionalProduct.ifPresent(productRepository::delete);

    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }


    @Override
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO
    ) throws DataNotFoundException, InvalidParamException
    {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException("Not found Product id: " +
                                productImageDTO.getProductId()));


        ProductImage newProductImage  = ProductImage
                .builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // Do not insert larger 5 file images for 1 Product
        int length = productImageRepository.findByProductId(productId).size();

        if(length >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException
                    ("Do not insert larger " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT +
                            " file images for 1 Product");
        }
         return productImageRepository.save(newProductImage);

    }
}
