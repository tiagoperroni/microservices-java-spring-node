import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";

export type FormDocument = Form & Document;
   
@Schema()
export class Form {

    @Prop()
    name: string;

    @Prop()
    email: string

    @Prop()
    message: string;
}

export const FormSchema = SchemaFactory.createForClass(Form);
