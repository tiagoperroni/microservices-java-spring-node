const express = require("express");
const eurekaHelper = require('./eureka-helper.js');
require('dotenv').config();
const actuator = require('express-actuator');
const actuatorOptions = require("./actuator-helper.js");
const routes = require("./src/routes/product.routes.js");

const app = express();
app.use(express.json());
app.use(actuator(actuatorOptions));

app.use(routes);

const PORT = process.env.SERVER_PORT || 3232;

eurekaHelper.registerWithEureka('product-api', PORT)

app.listen(PORT, () => console.log(`Server on - Port ${PORT}`));
