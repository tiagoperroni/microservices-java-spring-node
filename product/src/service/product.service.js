const products = require("../model/product.js")

class ProductService {
    updateStock(id, stock){
        for (const prod of products) {
          if (prod.id == id) {
            if (prod.stock >= stock) {
              prod.stock -= stock;
            }
         }
    }
}
}

module.exports = new ProductService();