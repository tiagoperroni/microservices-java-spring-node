const products = require('../model/product.js');
const productService = require('../service/product.service.js');

class ProductController {
        getProduct(req, res) {
        const { id, stock } = req.params;        
        let prod = products.filter((x) => x.id == id);
        console.log("LOG: Requisição recebida! Enviando produto: ", prod);
        return productService.updateStock(id, stock) &  res.status(200).json(prod[0]) & console.log("New stock: ", prod[0]);
    };
}

module.exports = new ProductController();