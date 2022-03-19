const routes = require("express").Router();
const productController = require('../controller/product.controller.js')

routes.get("/product/:id/:quantity", productController.getProduct);

module.exports = routes;