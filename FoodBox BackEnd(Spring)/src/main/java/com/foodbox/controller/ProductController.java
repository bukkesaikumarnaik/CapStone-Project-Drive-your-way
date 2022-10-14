package com.foodbox.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodbox.model.Admin;
import com.foodbox.model.Product;
import com.foodbox.repository.AdminRepository;
import com.foodbox.repository.ProductRepository;

import com.foodbox.exception.ResourceNotFoundException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired AdminRepository adminRepository;

	@GetMapping("/products/Admin")
	public List<Product> getAdminProducts() {
		return productRepository.findAll();
	}

	@GetMapping("/products/cust")
	public List<Product> getAllProducts() {
		List<Product> prodList=productRepository.findIfAvail();
		if(prodList.isEmpty()) {
			List<Admin> adminList = adminRepository.findAll();
			if(adminList.isEmpty()) {
				adminRepository.save(new Admin("admin","password"));
			}
			addProdIfEmpty(new Product(1,"Audi","To be able to mount powerful engines","Germany",4489000,0,0,"yes","./assets/images/audi.jpg"));
			addProdIfEmpty(new Product(2,"Ford","Acceptable jack points are on a Ford Focus.","India",690000,10,0,"yes","./assets/images/ford.jpg"));
			addProdIfEmpty(new Product(3,"BMW","fuel consumption,","",4410000,20,0,"yes","./assets/images/bmw.jpg"));
			addProdIfEmpty(new Product(4,"KIA","KIA's VIN through the lower-right corner of the windshield","India",700000,5,0,"yes","./assets/images/kia.jpg"));
			addProdIfEmpty(new Product(2,"Suzuki","full range of specs of older and newer models","America",756000,0,0,"yes","./assets/images/suzuki.jpg"));
			addProdIfEmpty(new Product(2,"Tesla","Spend Less Time At the Gas Station","USA",850000,0,0,"yes","./assets/images/tesla.jpg"));
			addProdIfEmpty(new Product(2,"Toyota"," Good performance","USA",1420000,18,0,"yes","./assets/images/toyota.jpg"));
			addProdIfEmpty(new Product(2,"Honda","Transmission (10 Speed Automatic EX-L) Back to top","Japan",925000,7,0,"yes","./assets/images/honda.jpg"));
			addProdIfEmpty(new Product(2,"Volkswagen","Regenerative Front Disc/Rear Drum Brakes w/4-Wheel ABS","Germany",1100000,0,0,"yes","./assets/images/volkswagen.jpg"));
			prodList=productRepository.findIfAvail();
		}
		return prodList;
	}
	
	public void addProdIfEmpty(Product product) {
		int min = 10000;
		int max = 99999;
		int b = (int) (Math.random() * (max - min + 1) + min);
		product.setId(b);
		float temp = (product.getActualPrice()) * (product.getDiscount() / 100);
		float price = product.getActualPrice() - temp;
		product.setPrice(price);
		productRepository.save(product);
	}

	@PostMapping("/products")
	public Product addProduct(@RequestBody Product product) {
		int min = 10000;
		int max = 99999;
		int b = (int) (Math.random() * (max - min + 1) + min);
		product.setId(b);
		float temp = (product.getActualPrice()) * (product.getDiscount() / 100);
		float price = product.getActualPrice() - temp;
		product.setPrice(price);
		return productRepository.save(product);
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails){
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with " + id));
		product.setName(productDetails.getName());
		product.setDesc(productDetails.getDesc());
		product.setCategory(productDetails.getCategory());
		product.setImagepath(productDetails.getImagepath());
		product.setActualPrice(productDetails.getActualPrice());
		product.setDiscount(productDetails.getDiscount());
		product.setAvail(productDetails.getAvail());
		float temp = (product.getActualPrice()) * (product.getDiscount() / 100);
		float price = product.getActualPrice() - temp;
		product.setPrice(price);
		
		Product updatedProd = productRepository.save(product);
		return ResponseEntity.ok(updatedProd);
		
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Not Found with " + id));
		productRepository.delete(product);
		Map<String, Boolean> map = new HashMap<>();
		map.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(map);
	}

	@GetMapping("products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found with " + id));
		return ResponseEntity.ok(product);
	}

	@GetMapping("products/search/{keyword}")
	public List<Product> getSearchProducts(@PathVariable String keyword) {
		return productRepository.homeSearch(keyword);
	}

	@GetMapping("products/chinese")
	public List<Product> getChinese() {
		return productRepository.getChinese();
	}

	@GetMapping("products/indian")
	public List<Product> getIndian() {
		return productRepository.getIndian();
	}

	@GetMapping("products/mexican")
	public List<Product> getMexican() {
		return productRepository.getMexican();
	}

	@GetMapping("products/italian")
	public List<Product> getItalian() {
		return productRepository.getItalian();
	}
}