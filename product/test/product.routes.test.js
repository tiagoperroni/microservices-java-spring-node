const chai = require("chai");
const chaiHttp = require("chai-http");
const server = require("../server.js");

let assert = chai.assert;

chai.use(chaiHttp);

describe("/Test of product server routes controller", () => {
  it("Test to get products assertions", () => {
    chai
      .request(server)
      .get("/product/1/0")
      .end((err, res) => {   
        assert.equal(res.body[0].name, "Refri Cola");
        assert.property(res.body[0], "stock"); 
        assert.typeOf(res.body, 'array');      
      }); 
  });
});
