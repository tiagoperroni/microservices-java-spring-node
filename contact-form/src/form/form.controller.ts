import { Controller, Get, Post, Body, Patch, Param, Delete, Put } from '@nestjs/common';
import { FormService } from './form.service';
import { Form } from './entities/form.entity';

@Controller('form')
export class FormController {
  constructor(private readonly formService: FormService) {}

  @Post()
  create(@Body()form: Form) {
    return this.formService.create(form);
  }

  @Get()
  findAll() {
    return this.formService.findAll();
  } 

  @Put(':id')
  update(@Param('id') id: string, @Body() form: Form) {
    return this.formService.update(id, form);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.formService.remove(id);
  }
}
