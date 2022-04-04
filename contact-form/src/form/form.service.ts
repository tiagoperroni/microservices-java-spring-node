import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { CreateFormDto } from './dto/create-form.dto';
import { UpdateFormDto } from './dto/update-form.dto';
import { Model } from 'mongoose';
import { Form, FormDocument } from './entities/form.entity';

@Injectable()
export class FormService {
  constructor(@InjectModel(Form.name) private formModel: Model<FormDocument>) {}

  create(form: Form) {
    return this.formModel.create(form);
  }

  findAll(): Promise<Form[]> {
    return this.formModel.find().exec();
  }  

  update(id: string, form: Form) {
    return this.formModel.findByIdAndUpdate(id, form).exec();
  }

  remove(id: string) {
    return this.formModel.findByIdAndDelete(id);
  }
}
