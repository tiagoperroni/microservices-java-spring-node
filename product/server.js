const express = require("express");

// ## zip
const zipkin = require("./src/config/zipkin.config.js");
var appzip = require('appmetrics-zipkin')(zipkin);

const eurekaHelper = require('./eureka-helper.js');
require('dotenv').config();
const actuator = require('express-actuator');
const actuatorOptions = require("./actuator-helper.js");
const routes = require("./src/routes/product.routes.js");
const conectDB = require("./src/db/mongo_db.js");

const app = express();
app.use(express.json());
app.use(actuator(actuatorOptions));
app.use(routes);

conectDB();

const PORT = process.env.SERVER_PORT || 3232;

eurekaHelper.registerWithEureka('product-api', PORT)

app.listen(PORT, () => console.log(`Server on - Port ${PORT}`));

module.exports = app;
