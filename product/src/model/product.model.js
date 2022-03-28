class ProductModel {
    
    constructor(id, name, price, stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    convertProduct(productRequest) {
        let product = new ProductModel();
        product.id = productRequest.id;
        product.name = productRequest.name;
        product.price = productRequest.price;
        product.stock = productRequest.stock;
        return product;
    }
}

module.exports = ProductModel;