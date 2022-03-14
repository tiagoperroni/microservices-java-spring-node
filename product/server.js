const express = require("express");

const app = express();
app.use(express.json());

const products = [
  { id: 1, name: "Refri Cola", price: 7.98, stock: 100 },
  { id: 2, name: "Refri Fanta", price: 7.32, stock: 100 },
  { id: 3, name: "Refri Uva", price: 6.87, stock: 100 },
  { id: 4, name: "Refri Guaraná", price: 6.55, stock: 100 },
];

app.get("/product/:id/:stock", (req, res) => {
  const { id, stock } = req.params;
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

app.listen(3232, () => console.log("Server on - Port 3232"));
