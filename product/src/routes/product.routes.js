const routes = require("express").Router();
const productController = require('../controller/product.controller.js')

routes.get("/product", productController.getProductProducts);
routes.get("/product/:id", productController.getProductById);
routes.get("/product/:id/:quantity", productController.getProductByIdAndUpdateStock);
routes.post("/product", productController.saveProduct);
routes.put("/product/:id", productController.updateProduct);
routes.delete("/product/:id", productController.deleteProduct);

module.exports = routes;