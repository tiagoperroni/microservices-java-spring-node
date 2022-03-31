const productService = require("../service/product.service.js");

class ProductController {
  async getProductProducts(req, res) {
    console.log("LOG => Requisição para listar todos produtos recebida.");
    let products = await productService.findAllProducts();
    return res.status(200).json(products);
  }

  async getProductById(req, res) {
    console.log("LOG => Requisição para buscar produto por id recebida.");
    try {
      let id = req.params.id;
      let productId = await productService.findById(id);
      return res.status(200).json(productId);
    } catch (error) {
      return res.status(404).json(error);
    }
  }

  async getProductByIdAndUpdateStock(req, res) {
    console.log("LOG => Requisição para buscar produto por id e atualizar estoque recebida.");
    try {
      let id = req.params.id;
      let quantity = req.params.quantity;
      let productId = await productService.findByIdAndUpdateStock(id, quantity);
      return res.status(200).json(productId);
    } catch (error) {
      return res.status(404).json(error);
    }
  }

  async saveProduct(req, res) {
    console.log("LOG => Requisição para salvar produto recebida.");
    try {
      const { name, price, stock } = req.body;
      let productRequest = { name: name, price: price, stock: stock };
      let productResponse = await productService.saveProduct(productRequest);
      return res.status(201).json(productResponse);
    } catch (error) {
      return res.status(400).json(error);
    }
  }

  async updateProduct(req, res) {
    console.log("LOG => Requisição para atualizar produto recebida.");
    try {
      const { name, price, stock } = req.body;
      const id = req.params.id;
      let productRequest = { name: name, price: price, stock: stock };     
      let productResponse = await productService.updateProduct(id, productRequest);
      return res.status(202).json(productResponse);
    } catch (error) {
      return res.status(404).json(error);
    }
  }

  async deleteProduct(req, res) {
    console.log("LOG => Requisição para deletar produto recebida.");
    try {
      const id = req.params.id;
      let productResponse = await productService.deleteProduct(id);
      return res.status(202).json(productResponse);
    } catch (error) {
      return res.status(404).json(error);
    }
  }
}

module.exports = new ProductController();
