const axios = require("axios");
const { expect } = require("@jest/globals");
const productService = require("../src/service/product.service");

test("Should get all products", async () => {
  const response = await axios({
    url: "http://localhost:3000/product",
    method: "get",
  });
  expect(response.data[0].name).toEqual("Refri Cola");
  expect(response.data[0].stock).toBeGreaterThan(50);
});

test("Should get product by id", async () => {
  const response = await axios({
    url: "http://localhost:3000/product/623f1743f90db5740d9b708a",
    method: "get",
  });
  expect(response.data.name).toEqual("Refri Fanta");
  expect(response.data.stock).toBeGreaterThan(50);
});

test("Should save a new product", async () => {
  const response = await axios({
    url: "http://localhost:3000/product/",
    method: "post",
    data: { name: "Refri Teste", price: 7.89, stock: 100 },
  });
  expect(response.data.name).toEqual("Refri Teste");
  expect(response.data.stock).toEqual(100);
  deleteProduct(response.data._id);
});

test("Should update a product", async () => {     
  const response = await axios({
    url: `http://localhost:3000/product/623f13bb44faf1c795e9cac0`,
    method: "put",
    data: { name: "Refri Cola", price: 7.89, stock: 100 },
  });
  expect(response.data.name).toEqual("Refri Cola");
  expect(response.data.stock).toEqual(100);
});

test("Should delete a product", async () => {
  const postResponse = await saveProduct();
  const response = await axios({
    url: `http://localhost:3000/product/${postResponse._id}`,
    method: "delete"   
  });
  expect(response.data.message).toEqual("Produto was deleted with success."); 
  expect(response.status).toEqual(202);
});

test("Should get product and update stock", async () => {     
  const response = await axios({
    url: `http://localhost:3000/product/623f13bb44faf1c795e9cac0/1`,
    method: "get"   
  });
  expect(response.data.name).toEqual("Refri Cola");
  expect(response.data.stock).toEqual(99);
});

async function saveProduct() {
  const response = await axios({
    url: "http://localhost:3000/product/",
    method: "post",
    data: { name: "Refri Teste", price: 7.89, stock: 100 },
  });
  return response.data;
}

async function deleteProduct(id) {
  await axios({
    url: `http://localhost:3000/product/${id}`,
    method: "delete",
  });
}
