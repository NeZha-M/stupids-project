package utilityFunctions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;

import ComponentFns.ExecutionManager;


public class TestReporting
{
	public boolean createReportDataHeader(String reportName,String strSprintInfo,String strTestScriptName,String strDescription,String expectedResult,String strTesterInfo,String strTestdata,String strDeviceUsed,String strVersion,ExecutionManager executionManager){
		String strReportColor="";
		executionManager.path = CONSTANTS.reportsLocation+reportName+".docx";
		if(executionManager.accessReport){
			try {
				FileInputStream pathStream = new FileInputStream(executionManager.path);
				executionManager.document = new XWPFDocument(pathStream);
			} 
			catch (Throwable e) {
				executionManager.document=new XWPFDocument();
				System.out.println("File not found");
			}
			executionManager.accessReport = false;
		}
		System.out.println("Report Location : "+executionManager.path);
		try{
			if(CONSTANTS.brand.toUpperCase().equalsIgnoreCase("ED")){
				strReportColor="2EB82E";//This is Green fro ED
			}
			else{
				strReportColor="D63385";// This is for Essent (some reddish pink)
			}
			XWPFTable table=executionManager.document.createTable(13,2);
			XWPFTableRow tableRowOne=table.getRow(0);
			XWPFTableCell tableRowOneCellOne=tableRowOne.getCell(0);			
			//tableRowOneCellOne.set
			tableRowOneCellOne.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2800));
			tableRowOneCellOne.setColor(strReportColor);
			tableRowOneCellOne.setText("  Sprint ,Epic, Test Case");
			XWPFTableCell tableRowOneCellTwo=tableRowOne.getCell(1);
			tableRowOneCellTwo.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(6000));
			tableRowOneCellTwo.setText("  "+strSprintInfo);
			//create second row
			XWPFTableRow tableRowTwo=table.getRow(1);
			XWPFTableCell tableRowTwoCellOne=tableRowTwo.getCell(0);	
			tableRowTwoCellOne.setColor(strReportColor);
			tableRowTwoCellOne.setText("  Test script");
			XWPFTableCell tableRowTwoCellTwo=tableRowTwo.getCell(1);
			tableRowTwoCellTwo.setText("  "+strTestScriptName);
			//create third row
			XWPFTableRow tableRowThree=table.getRow(2);
			XWPFTableCell tableRowThreeCellOne=tableRowThree.getCell(0);
			tableRowThreeCellOne.setColor(strReportColor);
			tableRowThreeCellOne.setText("  Description");
			XWPFTableCell tableRowThreeCellTwo=tableRowThree.getCell(1);
			tableRowThreeCellTwo.setText("  "+strDescription);
			XWPFTableRow tableRowTFour=table.getRow(3);
			XWPFTableCell tableRowFourCellOne=tableRowTFour.getCell(0);
			tableRowFourCellOne.setColor(strReportColor);
			tableRowFourCellOne.setText("  Expected Result");
			XWPFTableCell tableRowFourCellTwo=tableRowTFour.getCell(1);
			tableRowFourCellTwo.setText("  "+expectedResult);
			XWPFTableRow tableRowFive=table.getRow(4);
			XWPFTableCell tableRowFiveCellOne=tableRowFive.getCell(0);
			tableRowFiveCellOne.setColor(strReportColor);
			tableRowFiveCellOne.setText("  Tester");
			XWPFTableCell tableRowFiveCellTwo=tableRowFive.getCell(1);
			tableRowFiveCellTwo.setText("  "+strTesterInfo);
			XWPFTableRow tableRowSix=table.getRow(5);
			XWPFTableCell tableRowSixCellOne=tableRowSix.getCell(0);
			tableRowSixCellOne.setColor(strReportColor);
			tableRowSixCellOne.setText("  Test data");
			XWPFTableCell tableRowSixCellTwo=tableRowSix.getCell(1);
			tableRowSixCellTwo.setText("  "+strTestdata);
			XWPFTableRow tableRowSeven=table.getRow(6);
			XWPFTableCell tableRowSevenCellOne=tableRowSeven.getCell(0);
			tableRowSevenCellOne.setColor(strReportColor);
			tableRowSevenCellOne.setText("  Device used");
			XWPFTableCell tableRowSevenCellTwo=tableRowSeven.getCell(1);
			tableRowSevenCellTwo.setText("  "+strDeviceUsed);
			XWPFTableRow tableRowEighth=table.getRow(7);
			XWPFTableCell tableRowEighthCellOne=tableRowEighth.getCell(0);
			tableRowEighthCellOne.setColor(strReportColor);
			tableRowEighthCellOne.setText("  Version");
			XWPFTableCell tableRowEighthCellTwo=tableRowEighth.getCell(1);
			tableRowEighthCellTwo.setText("  "+strVersion);
			XWPFTableRow tableRowNinth=table.getRow(8);
			XWPFTableCell tableRowNineCellOne=tableRowNinth.getCell(0);
			tableRowNineCellOne.setColor(strReportColor);
			tableRowNineCellOne.setText("  Environments");
			XWPFTableCell tableRowNineCellTwo=tableRowNinth.getCell(1);
			tableRowNineCellTwo.setText("  "+CONSTANTS.environment);
			XWPFTableRow tableRowTen=table.getRow(9);
			XWPFTableCell tableRowTenCellOne=tableRowTen.getCell(0);
			tableRowTenCellOne.setColor(strReportColor);
			tableRowTenCellOne.setText("  Execution Date");
			XWPFTableCell tableRowTenCellTwo=tableRowTen.getCell(1);
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
			java.util.Date date = new java.util.Date();
			String time = sdf.format(date);
			tableRowTenCellTwo.setText("  "+time);
			XWPFTableRow tableRowEleven=table.getRow(10);
			XWPFTableCell tableRowElevenCellOne=tableRowEleven.getCell(0);
			tableRowElevenCellOne.setColor(strReportColor);
			tableRowElevenCellOne.setText("  Approved");
			XWPFTableCell tableRowElevenCellTwo=tableRowEleven.getCell(1);
			tableRowElevenCellTwo.setText("");
			XWPFTableRow tableRowTwelwe=table.getRow(11);
			XWPFTableCell tableRowTwelweCellOne=tableRowTwelwe.getCell(0);
			tableRowTwelweCellOne.setColor(strReportColor);
			tableRowTwelweCellOne.setText("  Jira Issues");
			XWPFTableCell tableRowTwelweCellTwo=tableRowTwelwe.getCell(1);
			tableRowTwelweCellTwo.setText("");
			XWPFTableRow tableRowThirteen=table.getRow(12);
			XWPFTableCell tableRowThirteenCellOne=tableRowThirteen.getCell(0);
			tableRowThirteenCellOne.setColor(strReportColor);
			tableRowThirteenCellOne.setText("  Remarks");
			XWPFTableCell tableRowThirteenCellTwo=tableRowThirteen.getCell(1);
			tableRowThirteenCellTwo.setText("");
			XWPFStyles style = executionManager.document.createStyles();
			String heading1 = "Heading1";
			addCustomHeadingStyle(executionManager.document, style, heading1, 1, 36, "0000FF");
			executionManager.out = new FileOutputStream(executionManager.path);
			executionManager.document.write(executionManager.out);                          
			executionManager.out.flush();executionManager.out.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private static void addCustomHeadingStyle(XWPFDocument docxDocument, XWPFStyles styles, String strStyleId, int headingLevel, int pointSize, String hexColor) {
	    CTStyle ctStyle = CTStyle.Factory.newInstance();
	    ctStyle.setStyleId(strStyleId);
	    CTString styleName = CTString.Factory.newInstance();
	    styleName.setVal(strStyleId);
	    ctStyle.setName(styleName);
	    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
	    indentNumber.setVal(BigInteger.valueOf(headingLevel));
	    // lower number > style is more prominent in the formats bar
	    ctStyle.setUiPriority(indentNumber);
	    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
	    ctStyle.setUnhideWhenUsed(onoffnull);
	    // style shows up in the formats bar
	    ctStyle.setQFormat(onoffnull);
	    // style defines a heading of the given level
	    CTPPr ppr = CTPPr.Factory.newInstance();
	    ppr.setOutlineLvl(indentNumber);
	    ctStyle.setPPr(ppr);
	    XWPFStyle style = new XWPFStyle(ctStyle);
	    CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
	    size.setVal(new BigInteger(String.valueOf(pointSize)));
	    CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
	    size2.setVal(new BigInteger("36"));
	    CTFonts fonts = CTFonts.Factory.newInstance();
	    fonts.setAscii("Cambria (Headings)" );
	    CTRPr rpr = CTRPr.Factory.newInstance();
	    rpr.setRFonts(fonts);
	    rpr.setSz(size);
	    rpr.setSzCs(size2);
	    style.setType(STStyleType.PARAGRAPH);
	    styles.addStyle(style);

	}
	public static byte[] hexToBytes(String hexString) {
	     HexBinaryAdapter adapter = new HexBinaryAdapter();
	     byte[] bytes = adapter.unmarshal(hexString);
	     return bytes;
	}
	public boolean updateStepInfo(String strStepInfo,String strExpectedResult,ExecutionManager executionManager){
		if(executionManager.accessReport){
			try {
				FileInputStream pathStream = new FileInputStream(executionManager.path);
				executionManager.document = new XWPFDocument(pathStream);
			} 
			catch (Throwable e) {
				e.printStackTrace();
				System.out.println("File not found");
			}
			executionManager.accessReport = false;
		}
		try{
			if(strStepInfo.length()>0)
			{
				executionManager.paragraphStepInfo=executionManager.document.createParagraph();
				executionManager.paragraphStepInfo.setStyle("Heading1");
				executionManager.stepRun=executionManager.paragraphStepInfo.createRun();
				executionManager.stepRun.setColor("0000FF");
				executionManager.stepRun.setBold(true);          
				executionManager.stepRun.addCarriageReturn();
				executionManager.stepRun.addCarriageReturn();
				executionManager.stepRun.setText(strStepInfo);
				System.out.println("Report Updated : "+executionManager.path);
				executionManager.out = new FileOutputStream(executionManager.path);
				executionManager.document.write(executionManager.out);                          
				executionManager.out.flush();executionManager.out.close();
			}
			if(strExpectedResult.length()>0){
				executionManager.paragraphExpectedActual = executionManager.document.createParagraph();
				executionManager.runExpectedActual=executionManager.paragraphExpectedActual.createRun();
				executionManager.runExpectedActual.setFontSize(12);
				executionManager.runExpectedActual.setText("Expected : "+strExpectedResult);
				executionManager.out = new FileOutputStream(executionManager.path);
				executionManager.document.write(executionManager.out);                          
				executionManager.out.flush();executionManager.out.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void createTableOfContents(boolean testPassed,ExecutionManager executionManager){
		if(executionManager.accessReport){
			try {
				FileInputStream pathStream = new FileInputStream(executionManager.path);
				executionManager.document = new XWPFDocument(pathStream);
			} 
			catch (Throwable e) {
				e.printStackTrace();
				System.out.println("File not found");
			}
			executionManager.accessReport = false;
		}
		executionManager.paragraphStepInfo = executionManager.document.createParagraph();
		executionManager.paragraphStepInfo.setStyle("Heading1");
		executionManager.stepRun=executionManager.paragraphStepInfo.createRun();
		executionManager.stepRun.setBold(true);          
		executionManager.stepRun.addCarriageReturn();
		executionManager.stepRun.addCarriageReturn();
		if(testPassed){
			executionManager.stepRun.setColor("008000");
			executionManager.stepRun.setText("TEST OK");
		}
		else{
			executionManager.stepRun.setColor("FF0000");
			executionManager.stepRun.setText("TEST NOT OK");
		}
		try {
			executionManager.out = new FileOutputStream(executionManager.path);
			executionManager.document.write(executionManager.out);                          
			executionManager.out.flush();executionManager.out.close();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("Created TOC");
	}
	public boolean updateActualResults(String strStepActual,String strStepStatus,ExecutionManager executionManager){
		try{
			if(strStepActual.length()>0){
				executionManager.runExpectedActual.addBreak();
				executionManager.runExpectedActual.setText("Actual : "+strStepActual);
				executionManager.runExpectedActual.addBreak();
			}
			if(strStepStatus.equalsIgnoreCase("pass")){
				executionManager.stepRun.addPicture(new FileInputStream(CONSTANTS.reportsLocation+"PassImage.png"), XWPFDocument.PICTURE_TYPE_PNG, CONSTANTS.reportsLocation+"\\PassImage.png", Units.toEMU(20), Units.toEMU(20)); 
			} else if(strStepStatus.equalsIgnoreCase("fail")){
				executionManager.stepRun.addPicture(new FileInputStream(CONSTANTS.reportsLocation+"FailImage.png"), XWPFDocument.PICTURE_TYPE_PNG, CONSTANTS.reportsLocation+"\\FailImage.png", Units.toEMU(20), Units.toEMU(20)); 
			}
			executionManager.out = new FileOutputStream(executionManager.path);
			executionManager.document.write(executionManager.out);                          
			executionManager.out.flush();executionManager.out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean addScreenshot(String screenshotLocaiton,ExecutionManager executionManager){
		try{
			String imgFile = screenshotLocaiton;
			executionManager.imagePara = executionManager.document.createParagraph();
			executionManager.imageRun = executionManager.imagePara.createRun();
			executionManager.imageRun.setText(screenshotLocaiton.split("/")[screenshotLocaiton.split("/").length-1].substring(0,(screenshotLocaiton.split("/")[screenshotLocaiton.split("/").length-1]).length()-4));
			executionManager.imageRun.addBreak();
			executionManager.imageRun.addPicture(new FileInputStream(imgFile), XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(500), Units.toEMU(350)); // 200x200 pixels
			executionManager.out = new FileOutputStream(executionManager.path);
			executionManager.document.write(executionManager.out);       
			executionManager.out.flush();executionManager.out.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

