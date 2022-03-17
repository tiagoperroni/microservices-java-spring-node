const products = require('../model/product.js');
const productService = require('../service/product.service.js');

class ProductController {
        getProduct(req, res) {
        const { id, stock } = req.params;
        console.log(stock);
        let prod = products.filter((x) => x.id == id);
        console.log("LOG: Requisição recebida! Enviando produto: ", prod);
        return res.status(200).json(prod) & productService.updateStock(id, stock) & console.log("New stock: ", prod[0]);
    };
}

module.exports = new ProductController();