const productModel = require('../model/product.schema');

const findAllProducts = () => productModel.find();
const findProductByName = (productId) => productModel.findById(productId);
const saveProduct = (newProduct) => productModel.create(newProduct);

module.exports = {
  findAllProducts,
  saveProduct,
  findProductByName
};
