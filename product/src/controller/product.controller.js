const products = require('../model/product.js');
const productService = require('../service/product.service.js');

class ProductController {
        getProduct(req, res) {
        const { id, quantity } = req.params;   
        console.log("LOG => Novo pedido recebido:", { idProduto: id, quantidadePedido: quantity  });           
        let prod = products.filter((x) => x.id == id);
        console.log("LOG: Requisição recebida! Enviando produto: ", prod);
        return res.status(200).json(prod[0]) & productService.updateStock(id, quantity) & console.log("New stock: ", prod[0]);
    };
}

module.exports = new ProductController();