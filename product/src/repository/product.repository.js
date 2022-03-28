const productModel = require('../model/product.schema');

const findAllProducts = () => productModel.find();
const findProductById = (id) => productModel.findById(id);
const saveProduct = (newProduct) => productModel.create(newProduct);
const updateProduct = (productId, newProduct) => productModel.findByIdAndUpdate(productId, newProduct, {new: true});
const deleteProduct = (productId) => productModel.findByIdAndDelete(productId);

module.exports = {
  findAllProducts,
  saveProduct,
  findProductById,
  updateProduct,
  deleteProduct,
};
