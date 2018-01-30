package org.lxy.utils.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by lxy on 2017/5/15 0015.
 * 交易信息生成PDF
 */
@Slf4j
public class TradePdfUtils {

    private static BaseFont baseFont;

    private static Font fontTitle;

    private static Font fontVs;
    private static Font fontMargin;

    private static Font fontContent;

    // #CEAD78
    private static BaseColor colorVs = new BaseColor(206, 173, 120);

    private static final float cellHeight1 = 28;
    private static final float cellHeight2 = 23.8f;

    static {
        try {
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontTitle = new Font(baseFont, 16, Font.BOLD);
            fontContent = new Font(baseFont, 9, Font.NORMAL);
            fontVs = new Font(baseFont, 12, Font.NORMAL, colorVs);
            fontMargin = new Font(baseFont, 5, Font.NORMAL, colorVs);
        } catch (Exception e) {
            log.error("failed to createFont!", e);
        }
    }


    public static void createTradePdf(Trade trade, Organization org, OutputStream outputStream,
                                      String bgImg, String sealImg) {
        Document document = null;
        try {

            document = new Document(PageSize.A4,
                    20, 20, 106, 20);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();

            Paragraph title = new Paragraph(TradePdfConstants.pdfTitle, fontVs);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            Paragraph titleS = new Paragraph(" ", fontMargin);
            document.add(titleS);

            TradeMainTypeEnum tradeType = TradeMainTypeEnum.valueOf(trade.getMainTradeType());
            switch (tradeType) {
                case APPLY_BUY: {
                    document.add(getTradeApplyTable(trade, org));
                    break;
                }
                case RANSOM: {
                    document.add(getTradeRansomTable(trade, org));
                    break;
                }
                case TRANSFER: {
                    document.add(getTradeTransferTable(trade, org));
                    break;
                }
            }

            addTips(document, pdfWriter);
            addSign(document);
            addBackGroundAndSeal(pdfWriter, bgImg, sealImg);

        } catch (Exception e) {
            log.error("failed to create trade pdf", e);
            throw new RuntimeException("failed to create tradePdf" + e.getMessage());
        } finally {
            if (null != document) {
                document.close();
            }
        }

    }

    private static PdfPTable getTradeApplyTable(Trade trade, Organization org) {
        PdfPTable table = getTradeCommonTable(trade, org, "申购");
        // row 6 买入基金代码 买入基金名称
        addTableCell(table, cellHeight2, "买入基金代码");
        addTableCell(table, cellHeight2, trade.getBuyFundCode());
        addTableCell(table, cellHeight2, "买入基金名称");
        addTableCell(table, cellHeight2, trade.getBuyFundName());

        // row 7 卖出基金代码 卖出基金名称
        addTableCell(table, cellHeight2, "卖出基金代码");
        addEmptyTableCell(table, cellHeight2);
        addTableCell(table, cellHeight2, "卖出基金名称");
        addEmptyTableCell(table, cellHeight2);

        // row 8 申请金额 申请份额
        addTableCell(table, cellHeight2, "申请金额");
        addTableCell(table, cellHeight2, trade.getBuyTotal().toPlainString());
        addTableCell(table, cellHeight2, "申请份额");
        addEmptyTableCell(table, cellHeight2);


        // row 9 巨额赎回方式 分红方式
        addTableCell(table, cellHeight2, "巨额赎回方式");
        addEmptyTableCell(table, cellHeight2);
        addTableCell(table, cellHeight2, "分红方式");
        addEmptyTableCell(table, cellHeight2);

        return table;
    }

    private static PdfPTable getTradeRansomTable(Trade trade, Organization org) {

        PdfPTable table = getTradeCommonTable(trade, org, "赎回");

        // row 6 买入基金代码 买入基金名称
        addTableCell(table, cellHeight2, "买入基金代码");
        addEmptyTableCell(table, cellHeight2);
        addTableCell(table, cellHeight2, "买入基金名称");
        addEmptyTableCell(table, cellHeight2);


        // row 7 卖出基金代码 卖出基金名称
        addTableCell(table, cellHeight2, "卖出基金代码");
        addTableCell(table, cellHeight2, trade.getRansomFundCode());
        addTableCell(table, cellHeight2, "卖出基金名称");
        addTableCell(table, cellHeight2, trade.getRansomFundName());

        // row 8 申请金额 申请份额
        addTableCell(table, cellHeight2, "申请金额");
        addEmptyTableCell(table, cellHeight2);
        addTableCell(table, cellHeight2, "申请份额");
        addTableCell(table, cellHeight2, trade.getRansomApplyShare().toPlainString());


        // row 9 巨额赎回方式 分红方式
        addTableCell(table, cellHeight2, "巨额赎回方式");
        addTableCell(table, cellHeight2, "");
        addTableCell(table, cellHeight2, "分红方式");
        addEmptyTableCell(table, cellHeight2);

        return table;
    }


    private static PdfPTable getTradeTransferTable(Trade trade, Organization org) {
        PdfPTable table = getTradeCommonTable(trade, org, "转换");

        // row 6 买入基金代码 买入基金名称
        addTableCell(table, cellHeight2, "买入基金代码");
        addTableCell(table, cellHeight2, trade.getBuyFundCode());
        addTableCell(table, cellHeight2, "买入基金名称");
        addTableCell(table, cellHeight2, trade.getBuyFundName());

        // row 7 卖出基金代码 卖出基金名称
        addTableCell(table, cellHeight2, "卖出基金代码");
        addTableCell(table, cellHeight2, trade.getRansomFundCode());
        addTableCell(table, cellHeight2, "卖出基金名称");
        addTableCell(table, cellHeight2, trade.getRansomFundName());

        // row 8 申请金额 申请份额
        addTableCell(table, cellHeight2, "申请金额");
        addEmptyTableCell(table, cellHeight2);
        addTableCell(table, cellHeight2, "申请份额");
        addTableCell(table, cellHeight2, trade.getRansomApplyShare().toPlainString());


        // row 9 巨额赎回方式 分红方式
        addTableCell(table, cellHeight2, "巨额赎回方式");
        addEmptyTableCell(table, cellHeight2);
        addTableCell(table, cellHeight2, "分红方式");
        addEmptyTableCell(table, cellHeight2);
        return table;
    }

    private static PdfPTable getTradeCommonTable(Trade trade, Organization org, String businessType) {
        PdfPTable table = new PdfPTable(new float[]{1f, 1.1f, 1.1f, 1.2f});
        table.setWidthPercentage(92);

        // row 1 机构名称
        addTableCell(table, cellHeight1, "机构名称");
        addTableCell(table, cellHeight1, org.getFullName(), 3);

        // row 2 机构类型
        addTableCell(table, cellHeight1, "机构类型");
        addTableCell(table, cellHeight1, "一般企业", 3);

        // row 3 证件类型 证件号码
        addTableCell(table, cellHeight2, "证件类型");
        addTableCell(table, cellHeight2, "");
        addTableCell(table, cellHeight2, "证件号码");
        addTableCell(table, cellHeight2, org.getCertNo());

        // row 4 私募/信托代码 私募/信托名称
        addTableCell(table, cellHeight2, "私募/信托代码");
        addEmptyTableCell(table, cellHeight2);
        addTableCell(table, cellHeight2, "私募/信托名称");
        addEmptyTableCell(table, cellHeight2);

        // row 5 申请工作日 , 业务类型
        String applyTime = "";
        addTableCell(table, cellHeight2, "申请工作日");
        addTableCell(table, cellHeight2, applyTime);
        addTableCell(table, cellHeight2, "业务类型");
        addTableCell(table, cellHeight2, businessType);

        return table;
    }

    private static void addTips(Document document, PdfWriter pdfWriter) throws Exception {

        /*Paragraph tipTitle = new Paragraph("重要提示：", fontTitle);
        tipTitle.setAlignment(Element.ALIGN_LEFT);
        tipTitle.setIndentationLeft(45f);
        document.add(tipTitle);
        PdfContentByte canvas = pdfWriter.getDirectContent();
        CMYKColor magentaColor = new CMYKColor(0.0f, 0.1602f, 0.4175f, 0.1922f);
        canvas.setColorStroke(magentaColor);
        canvas.moveTo(65, 450);
        canvas.lineTo(132, 450);
        canvas.closePathStroke();*/

        addEmptyRow(document);
        addEmptyRow(document);

        for (String tip : TradePdfConstants.tips) {
            Paragraph p = new Paragraph(tip, fontContent);
            p.setLeading(23f);
            p.setIndentationLeft(45f);
            p.setIndentationRight(55f);
            document.add(p);
        }
    }

    private static void addSign(Document document) throws Exception {
        addEmptyRow(document);
        addEmptyRow(document);

        Paragraph signName = new Paragraph(TradePdfConstants.pdfSignName, fontContent);
        signName.setLeading(23f);
        signName.setIndentationRight(60f);
        signName.setAlignment(Element.ALIGN_RIGHT);
        document.add(signName);

        String signDateString = TradePdfConstants.pdfSignDate + getPdfSignDateString();
        Paragraph signDate = new Paragraph(signDateString, fontContent);
        signDate.setIndentationRight(60f);
        signDate.setAlignment(Element.ALIGN_RIGHT);
        document.add(signDate);
    }

    private static void addEmptyRow(Document document) throws Exception {
        Paragraph blankRow = new Paragraph(18f, " ", fontContent);
        document.add(blankRow);
    }

    private static String getPdfSignDateString() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR));
        sb.append("年");
        sb.append(calendar.get(Calendar.MONTH) + 1);
        sb.append("月");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append("日");
        return sb.toString();
    }

    private static void addTableCell(PdfPTable table, float cellHeight, String cellValue) {
        PdfPCell cell = new PdfPCell(new Phrase(cellValue, fontContent));
        cell.setFixedHeight(cellHeight);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private static void addTableCell(PdfPTable table, float cellHeight, String cellValue, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(cellValue, fontContent));
        cell.setFixedHeight(cellHeight);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(colspan);
        table.addCell(cell);
    }

    private static void addEmptyTableCell(PdfPTable table, float cellHeight) {
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(cellHeight);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private static void addEmptyTableCell(PdfPTable table, float cellHeight, int colSpan) {
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(cellHeight);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(colSpan);
        table.addCell(cell);
    }

    private static void addBackGroundAndSeal(PdfWriter pdfWriter, String bgImg, String sealImg) throws Exception {

        log.info("bgImg:{},sealImg:{}", bgImg, sealImg);
        try (InputStream inputStream = new FileInputStream(bgImg)) {
            log.info(inputStream.available() + "");
        } catch (Exception e) {
            log.error("failed to load image", e);
        }
        PdfContentByte canvas = pdfWriter.getDirectContentUnder();
        Image image = Image.getInstance(bgImg);
        image.scaleAbsolute(PageSize.A4);
        image.setAbsolutePosition(0, 0);
        canvas.addImage(image);

        PdfContentByte seal = pdfWriter.getDirectContent();
        Image sealImage = Image.getInstance(sealImg);
        sealImage.scaleAbsolute(80, 80);
        sealImage.setAbsolutePosition(415, 48);
        seal.saveState();
        PdfGState state = new PdfGState();
        state.setFillOpacity(0.8f);
        seal.setGState(state);
        seal.addImage(sealImage);
        seal.restoreState();
        seal.addImage(sealImage);
    }

    @Test
    public void test1() {
        try {
            String bg = "D:/pdf/pdfBg.jpg";
            Image image = Image.getInstance(bg);
            log.info(image.getPlainHeight() + "");
            Image image2 = Image.getInstance(bg);
            log.info(image2.getPlainHeight() + "");
            Image image3 = Image.getInstance(bg);
            log.info(image3.getPlainHeight() + "");
            Image image4 = Image.getInstance(bg);
            log.info(image4.getPlainHeight() + "");
        } catch (Exception e) {
            log.error("failed ", e);
        }
    }
}
