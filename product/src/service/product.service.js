const { v4 } = require('uuid');
const { findAllProducts, saveProduct, findProductByName } = require("../repository/product.repository.js");

class ProductService {
  async findAllProducts() {
    let product = await findAllProducts();   
    return product;
  }

  async findById(productId) {
    let findProduct = await findProductByName(productId); 
    if (findProduct == null) return { message: "Not found product with id " + productId};  
    console.log(findProduct);
    const productResponse = { id: v4(), name: findProduct.name, price: findProduct.price, stock: findProduct.stock };   
    return productResponse;
  }

  async saveProduct(newProduct) {   
    let productRequest = { name: newProduct.name, price: newProduct.price, stock: newProduct.stock };
    if (productRequest.name === undefined || productRequest.price === undefined || productRequest.stock === undefined) {
      return { message: "Please, informe all fields." };
    }
    let product = await saveProduct(productRequest);
    return product;
  }
}

module.exports = new ProductService();
