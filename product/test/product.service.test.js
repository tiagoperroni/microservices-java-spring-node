const chai = require("chai");
const { updateStock } = require("../src/service/product.service.js");

let assert = chai.assert;

describe("/Test of product model", () => {
  it("Test to get products assertions", () => {
    assert.equal(98, updateStock(1, 2).stock); 
    assert.equal(updateStock(1, 2).name, 'Refri Cola');
    assert.equal(updateStock(3, 2).name, 'Refri Uva');
  });
});
