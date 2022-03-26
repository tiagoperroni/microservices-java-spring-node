const routes = require("express").Router();
const productController = require('../controller/product.controller.js')

routes.get("/product", productController.getProductProducts);
routes.get("/product/:id", productController.getProductById);
routes.post("/product", productController.saveProduct);

module.exports = routes;