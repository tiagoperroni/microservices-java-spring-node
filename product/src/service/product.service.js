const { v4 } = require('uuid');
const ProductException = require('../exception/product.exception.js');
const ProductModel = require('../model/product.model.js');
const { findAllProducts, saveProduct, findProductById, updateProduct, deleteProduct } = require("../repository/product.repository.js");

class ProductService {
  async findAllProducts() {
    let product = await findAllProducts();   
    return product;
  }

  async findById(id) {     
    let findProduct = await findProductById(id); 
    if (findProduct == null) return { message: "Not found product with id " + productId};    
    const productResponse = { id: v4(), name: findProduct.name, price: findProduct.price, stock: findProduct.stock };   
    return productResponse;
  }

  async findByIdAndUpdateStock(id, quantity) {     
    let product = await this.updateStock(id, quantity);
    if (product == null) return { message: "Not found product with id " + productId};    
    const productResponse = { id: v4(), name: product.name, price: product.price, stock: product.stock };   
    return productResponse;
  }

  async saveProduct(newProduct) {  
    if (await this.verifyDuplicateProduct(newProduct)) return { message: "Already exists a product with this name." }
    let productRequest = { name: newProduct.name, price: newProduct.price, stock: newProduct.stock };
    if (productRequest.name === undefined || productRequest.price === undefined || productRequest.stock === undefined) {
      return { message: "Please, informe all fields." };
    }
    let product = await saveProduct(productRequest);
    return product;
  }

  async updateProduct(productId, newProduct) {     
    let findProduct = await findProductById(productId); 
    if (findProduct == null) return { message: "Not found product with id " + productId}; 
    let productRequest = { name: newProduct.name, price: newProduct.price, stock: newProduct.stock };    
    let product = await updateProduct(productId, productRequest);
    return product;
  }

  async deleteProduct(productId) {   
    let findProduct = await findProductById(productId); 
    if (findProduct == null) return { message: "Not found product with id " + productId};        
    await deleteProduct(productId);
    return { message: "Produto was deleted with success." };
  }

  async updateStock(id, quantity) {    
    let findProduct = await findProductById(id); 
    if (findProduct === null) throw new ProductException("Product not found.", 404);   
    let product = new ProductModel().convertProduct(findProduct);
    console.log("LOG: Service = atualizando estoque.");
    product.stock -= +quantity;
    this.updateProduct(id, product);
    return product;
  }

  verifyStock() {

  }

  async verifyDuplicateProduct(product) {
    let productRequest = await findAllProducts();
    for (const prod of productRequest) {
      if (prod.name === product.name) {  
        return true;
      }
    }
  }
}

module.exports = new ProductService();
