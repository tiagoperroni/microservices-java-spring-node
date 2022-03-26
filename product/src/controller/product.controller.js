const productService = require("../service/product.service.js");

class ProductController {
  async getProductProducts(req, res) {
    console.log("LOG => Requisição para listar todos produtos recebida.");
    let products = await productService.findAllProducts();
    return res.status(200).json(products);
  }

  async getProductByName(req, res) {
    console.log("LOG => Requisição para buscar produto por nome recebida.");
    let productId = await productService.findById(req.params.id);  
    console.log(productId.message);
    return productId.message === undefined ? res.status(200).json(productId) : res.status(202).json(productId);
  }

  async saveProduct(req, res) {
    console.log("LOG => Requisição para salvar produto recebida.");
    const { name, price, stock } = req.body;
    let productRequest = { name: name, price: price, stock: stock };
    let productResponse = await productService.saveProduct(productRequest);
    return productResponse.name !== productRequest.name ? res.status(400).json(productResponse) : res.status(201).json(productResponse);
  }
}

module.exports = new ProductController();
