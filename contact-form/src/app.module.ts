import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose'; 
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { FormModule } from './form/form.module';

@Module({
  imports: [MongooseModule.forRoot('mongodb://localhost:27017/contact_db'), FormModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
