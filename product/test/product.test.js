const chai = require("chai");
const products = require("../src/model/product.js");

let expect = chai.expect;
let assert = chai.assert;

describe("/Test of product model", () => {
  it("Test to get products expect", () => {
    expect(products[0]).to.be.a("object");
    expect(products[0].name).to.be.a("String");
    expect(products[0]).to.have.property("name");
    expect(products).to.have.lengthOf(4);      
  });

  it("Test to get products assertions", () => {
    assert.equal("Refri Cola", products[0].name); 
    assert.typeOf(products[0].name, 'string'); 
    assert.lengthOf(products[0].name, 10)
    assert.property(products[0], 'name');
    assert.lengthOf(products, 4);
  });
});
