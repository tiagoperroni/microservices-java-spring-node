const mongoose = require('mongoose');
require('dotenv').config({ path: '.env' });

const conectDB = async () => {

    try {

        await mongoose.connect(process.env.DB_MONGO, {                     
            useNewUrlParser: true,
            useUnifiedTopology: true
           
        });
        console.info({ mongodb_status: "Connected!" });
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

module.exports = conectDB;