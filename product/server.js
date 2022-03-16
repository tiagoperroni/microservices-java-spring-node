const express = require("express");
const eurekaHelper = require('./eureka-helper.js');
require('dotenv').config();
const actuator = require('express-actuator');
const actuatorOptions = require("./actuator-helper.js")

const app = express();
app.use(express.json());
app.use(actuator(actuatorOptions));

const products = [
  { id: 1, name: "Refri Cola", price: 7.98, stock: 100 },
  { id: 2, name: "Refri Fanta", price: 7.32, stock: 100 },
  { id: 3, name: "Refri Uva", price: 6.87, stock: 100 },
  { id: 4, name: "Refri Guaraná", price: 6.55, stock: 100 },
];

app.get("/product/:id/:stock", (req, res) => {
  const { id, stock } = req.params;
  console.log(stock);
  let prod = products.filter((x) => x.id == id);
  console.log("LOG: Requisição recebida! Enviando produto: ", prod);
  return res.status(200).json(prod) & updateStock(id, stock) & console.log("New stock: ", prod[0]);
});

function updateStock(id, stock) {
  for (const prod of products) {
    if (prod.id == id) {
      if (prod.stock >= stock) {
        prod.stock -= stock;
      }
    }
  }
}

const PORT = process.env.SERVER_PORT || 3232;

eurekaHelper.registerWithEureka('product-api', PORT)

app.listen(PORT, () => console.log(`Server on - Port ${PORT}`));
