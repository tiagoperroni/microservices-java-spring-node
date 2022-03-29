package com.tiagoperroni.ordermail.invoice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.tiagoperroni.ordermail.model.OrderItems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConfig {

    private Logger logger = LoggerFactory.getLogger(InvoiceConfig.class);

    public void execute(String name, String cpf, String date, String invoiceNumber, List<OrderItems> items,
            Double total) throws IOException {
        logger.info("InvoiceConfig: Starting prepare invoice.. {}", invoiceNumber);

        String path = "./src/main/resources/arquivos/invoice.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        float col = 280f;
        float columnWidth[] = { col, col };
        Table table = new Table(columnWidth);

        // cor de fundo e texto
        table.setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE);
        // ajustando tabela o texto
        table.addCell(new Cell().add("INVOICE")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMarginTop(30f)
                .setMarginBottom(30f)
                .setFontSize(30f)
                .setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add("Perroni Tecnologia\n#3465 Paranavaí\n87710-140")
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30f)
                .setMarginBottom(30f)
                .setBorder(Border.NO_BORDER)
                .setMarginRight(10f));

        // criando coluna
        float colWidth[] = { 80, 300, 100, 80 };
        Table customerInfoTable = new Table(colWidth);

        customerInfoTable.addCell(new Cell(0, 4) // tamanho da coluna
                .add("Customer Information")
                .setBold()
                .setBorder(Border.NO_BORDER));

        customerInfoTable.addCell(new Cell().add("Name").setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(name).setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add("Invoice No.").setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(invoiceNumber).setBorder(Border.NO_BORDER));

        customerInfoTable.addCell(new Cell().add("CPF:").setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(cpf).setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add("Data").setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(LocalDate.now().toString()).setBorder(Border.NO_BORDER));

        float itemInfoColWidth[] = { 140, 140, 140, 140 };
        Table itemInfoTable = new Table(itemInfoColWidth);

        // coluna cabeçalho
        itemInfoTable.addCell(new Cell().add("Nome do Produto").setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE));
        itemInfoTable.addCell(new Cell().add("Quantidade.").setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE));

        itemInfoTable.addCell(new Cell().add("Preço unitário (BRL)").setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setTextAlignment(TextAlignment.RIGHT));

        itemInfoTable.addCell(
                new Cell().add("Amount").setBackgroundColor(new DeviceRgb(63, 169, 219)).setFontColor(Color.WHITE)
                        .setTextAlignment(TextAlignment.RIGHT));

        for (OrderItems orderItem : items) {
            itemInfoTable.addCell(new Cell().add(orderItem.getProductName()));
            itemInfoTable.addCell(new Cell().add("" + orderItem.getQuantity()));
            itemInfoTable.addCell(new Cell().add(orderItem.getProductPrice().toString()).setTextAlignment(TextAlignment.RIGHT));
            itemInfoTable.addCell(new Cell()
            .add(this.convertTotalPrice(orderItem.getQuantity(), orderItem.getProductPrice())).setTextAlignment(TextAlignment.RIGHT));       
        }

        itemInfoTable.addCell(new Cell().add("").setTextAlignment(TextAlignment.RIGHT)
                .setBackgroundColor(new DeviceRgb(63, 169, 219)).setBorder(Border.NO_BORDER));

        itemInfoTable.addCell(
                new Cell().add("").setBackgroundColor(new DeviceRgb(63, 169, 219)).setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add("Preço Total").setBold().setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setBorder(Border.NO_BORDER)
                .setFontColor(Color.WHITE));

        itemInfoTable.addCell(new Cell().add(total.toString()).setBold().setTextAlignment(TextAlignment.RIGHT)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setBorder(Border.NO_BORDER)
                .setFontColor(Color.WHITE));

        document.add(table);
        document.add(new Paragraph("\n"));
        document.add(customerInfoTable);
        document.add(new Paragraph("\n"));
        document.add(itemInfoTable);
        document.add(new Paragraph("\n(Assinatura Autorização)").setTextAlignment(TextAlignment.RIGHT));

        document.close();
        logger.info("InvoiceConfig: PDF was created with success.");
    }

    public String convertTotalPrice(int quantity, Double price) {
        double total = quantity * price;
        return String.format("%.2f", total);
    }

}
