package vn.iotstar.controllers.api;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

@RestController
@RequestMapping(path = "/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<Response>(
            new Response(true, "Thành công", productService.findAll()), 
            HttpStatus.OK
        );
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(
                new Response(true, "Thành công", optProduct.get()), 
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<Response>(
                new Response(false, "Không tìm thấy sản phẩm", null), 
                HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(path = "/getProduct")
    public ResponseEntity<?> getProduct(@RequestParam("id") Long id) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(
                new Response(true, "Thành công", optProduct.get()), 
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<Response>(
                new Response(false, "Không tìm thấy sản phẩm", null), 
                HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile productImages,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0") Double discount,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @RequestParam(value = "status", defaultValue = "1") Short status) {

        Optional<Product> optProduct = productService.findByProductName(productName);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(
                new Response(false, "Sản phẩm này đã tồn tại trong hệ thống", optProduct.get()), 
                HttpStatus.BAD_REQUEST
            );
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount != null ? discount : 0.0);
        product.setQuantity(quantity);
        product.setDescription(description != null ? description : "");
        product.setStatus(status != null ? status : 1);
        product.setCreateDate(new Date());

        // Gán Category
        Optional<Category> optCategory = categoryService.findById(categoryId);
        optCategory.ifPresent(product::setCategory);

        // Upload ảnh
        if (productImages != null && !productImages.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            product.setImages(storageService.getSorageFilename(productImages, uuString));
            storageService.store(productImages, product.getImages());
        }

        productService.save(product);
        return new ResponseEntity<Response>(
            new Response(true, "Thêm sản phẩm thành công", product), 
            HttpStatus.OK
        );
    }

    @PutMapping(path = "/updateProduct")
    public ResponseEntity<?> updateProduct(
            @Validated @RequestParam("productId") Long productId,
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile productImages,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0") Double discount,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @RequestParam(value = "status", defaultValue = "1") Short status) {

        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(
                new Response(false, "Không tìm thấy sản phẩm", null), 
                HttpStatus.BAD_REQUEST
            );
        }

        Product product = optProduct.get();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount != null ? discount : 0.0);
        product.setQuantity(quantity);
        product.setDescription(description != null ? description : "");
        product.setStatus(status != null ? status : 1);

        // Gán Category mới
        Optional<Category> optCategory = categoryService.findById(categoryId);
        optCategory.ifPresent(product::setCategory);

        // Upload ảnh mới nếu có
        if (productImages != null && !productImages.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            product.setImages(storageService.getSorageFilename(productImages, uuString));
            storageService.store(productImages, product.getImages());
        }

        productService.save(product);
        return new ResponseEntity<Response>(
            new Response(true, "Cập nhật sản phẩm thành công", product), 
            HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId) {
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(
                new Response(false, "Không tìm thấy sản phẩm", null), 
                HttpStatus.BAD_REQUEST
            );
        }

        productService.delete(optProduct.get());
        return new ResponseEntity<Response>(
            new Response(true, "Xóa sản phẩm thành công", optProduct.get()), 
            HttpStatus.OK
        );
    }
}
