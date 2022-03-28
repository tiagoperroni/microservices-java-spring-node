class ProductException {
  message;
  statusCode;

  constructor(message, statusCode) {
    this.message = message;
    this.statusCode = statusCode;
  }

  getMessage() {
    return this.getMessage;
  }

  setMessage(message) {
    this.message = message;
  }

  getStatusCode() {
    return this.statusCode;
  }

  setStatusCode(statusCode) {
    this.statusCode = statusCode;
  }
}

module.exports = ProductException;
