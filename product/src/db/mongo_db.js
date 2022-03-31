const mongoose = require('mongoose');
require('dotenv').config({ path: '.env' });

const conectDB = async () => {

    try {

        await mongoose.connect(process.env.DB_MONGO, {   
            //Observe que, se você especificar useNewUrlParser: true, deverá especificar uma porta em sua string de conexão                  
            useNewUrlParser: true,
            //Falso por padrão. Defina para true se optar por usar o novo mecanismo de gerenciamento de conexão do driver MongoDB. 
            //Você deve definir essa opção como true, exceto no caso improvável de impedir que você mantenha uma conexão estável.
            useUnifiedTopology: true
           
        });
        console.info({ mongodb_status: "Connected!" });
    } catch (error) {
        console.error(error);
        process.exit(1);
    }
}

module.exports = conectDB;