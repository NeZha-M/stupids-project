package utilityFunctions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ComponentFns.ExecutionManager;

public class ExcelTestDataManager {

	public static LinkedHashMap<String, String> readTestData(String excelLoc,String sheetName) throws Throwable{
		int fileCheckOutCount = 0;
		while(true){
			if(!(ExecutionManager.testDataInUse)){
				ExecutionManager.testDataInUse = true;
				System.out.println("Test Data checked out");
				break;
			}
			if(fileCheckOutCount==100){
				return null;
			}
			Thread.sleep(1000);
			System.out.println("Waiting for Test Data sheet"+fileCheckOutCount);
			fileCheckOutCount++;
		}
		LinkedHashMap<String, String> testData = new LinkedHashMap<String,String>();
		XSSFWorkbook srcBook = new XSSFWorkbook(excelLoc);
		try{	
			XSSFSheet sourceSheet = srcBook.getSheet(sheetName);     //srcBook.getSheetAt(0);
			XSSFRow sourceRow1 = sourceSheet.getRow(0);
			XSSFRow sourceRow2 = sourceSheet.getRow(1);
			int noOfCols = sourceRow1.getLastCellNum();
			for(int colNum=0;colNum<noOfCols;colNum++){
				if(sourceRow2.getCell(colNum)==null){
					testData.put(sourceRow1.getCell(colNum).toString(), "");
					continue;
				}
				testData.put(sourceRow1.getCell(colNum).toString(), sourceRow2.getCell(colNum).toString());
			}
		}
		catch(Throwable t){
			System.out.println(t.toString());
		}
		finally{
			srcBook.close();
		}
		ExecutionManager.testDataInUse = false;
		return testData;
	}
	public static void updateTestDataColumn(String excelLoc,String sheetName,String dataKey,String[] dataValue) throws IOException{

		XSSFWorkbook srcBook = null;
		FileOutputStream fileOut = null;
		try{
			srcBook = new XSSFWorkbook(new FileInputStream(excelLoc));     
			XSSFSheet sourceSheet = srcBook.getSheet(sheetName);     //srcBook.getSheetAt(0);
			XSSFRow sourceRow = sourceSheet.getRow(0);
			int noOfCols = sourceRow.getLastCellNum();
			for(int colNum=0;colNum<noOfCols;colNum++){
				if(sourceRow.getCell(colNum)!=null && sourceRow.getCell(colNum).toString().equals(dataKey)){
					XSSFCell modifyCell = null;
					for(int dataVal=0;dataVal<dataValue.length;dataVal++){
						sourceRow = sourceSheet.getRow(dataVal+1);
						if(sourceRow==null){
							sourceRow = sourceSheet.createRow(dataVal+1);
						}
						modifyCell = sourceRow.createCell(colNum);
						modifyCell.setCellValue(dataValue[dataVal]);
					}
					for(int dataVal=dataValue.length;dataVal<50;dataVal++){
						sourceRow = sourceSheet.getRow(dataVal+1);
						if(sourceRow==null){
							sourceRow = sourceSheet.createRow(dataVal+1);
						}
						modifyCell = sourceRow.createCell(colNum);
						sourceRow.removeCell(modifyCell);
					}
					break;
				}
			}
			fileOut = new FileOutputStream(excelLoc);
		    srcBook.write(fileOut);
		}
		catch(Throwable t){
			System.out.println("Oops, test data not updated");
			System.out.println(t.toString());
		}
		finally{
			fileOut.flush();
	        fileOut.close();
			srcBook.close();
		}
	}
	public static LinkedHashMap<String,LinkedHashMap<String, String>> readTestData(String excelLoc,String sheetName,String deviceName) throws Throwable{
		int fileCheckOutCount = 0;
		String[] deviceDataRows = readExcelColumn(excelLoc,sheetName,"DeviceName");
		while(true){
			if(!(ExecutionManager.testDataInUse)){
				ExecutionManager.testDataInUse = true;
				System.out.println("Test Data checked out");
				break;
			}
			if(fileCheckOutCount==100){
				return null;
			}
			Thread.sleep(1000);
			System.out.println("Waiting for Test Data sheet"+fileCheckOutCount);
			fileCheckOutCount++;
		}
		LinkedList<Integer> rowIndexes = new LinkedList<Integer>();
		for(int dataRow=0;dataRow<deviceDataRows.length;dataRow++){
			if(deviceDataRows[dataRow].equals(deviceName)){
				rowIndexes.add(dataRow+1);
			}
		}
		LinkedHashMap<String,LinkedHashMap<String, String>> testData = new LinkedHashMap<String,LinkedHashMap<String, String>>();
		Integer testDataCount = 0;
		for (Integer integer : rowIndexes) {
			LinkedHashMap<String, String> tempTestData = new LinkedHashMap<String,String>();
			XSSFWorkbook srcBook = new XSSFWorkbook(excelLoc);;
			try{	
				XSSFSheet sourceSheet = srcBook.getSheet(sheetName);     //srcBook.getSheetAt(0);
				XSSFRow sourceRow1 = sourceSheet.getRow(0);
				XSSFRow sourceRow2 = sourceSheet.getRow(integer);
				int noOfCols = sourceRow1.getLastCellNum();
				for(int colNum=0;colNum<noOfCols;colNum++){
					if(sourceRow2.getCell(colNum)==null){
						tempTestData.put(sourceRow1.getCell(colNum).toString(), "");
						continue;
					}
					tempTestData.put(sourceRow1.getCell(colNum).toString(), sourceRow2.getCell(colNum).toString());
				}
			}
			catch(Throwable t){
				System.out.println(t.toString());
			}
			finally{
				srcBook.close();
			}
			testData.put(testDataCount.toString(), tempTestData);
			testDataCount++;
		}
		ExecutionManager.testDataInUse = false;
		return testData;
	}
	public static LinkedHashMap<String, String> readExcelSheet(String excelLoc,String sheetName) throws IOException{
		LinkedHashMap<String, String> excelData = new LinkedHashMap<String,String>();	
		XSSFWorkbook srcBook = new XSSFWorkbook(excelLoc);;
		try{	
			XSSFSheet sourceSheet = srcBook.getSheet(sheetName);     //srcBook.getSheetAt(0);
			XSSFRow sourceRow = null;
			int noOfRows = sourceSheet.getLastRowNum();
			System.out.println("No of Rows in "+sheetName+" are : "+noOfRows);
			int noOfCols = 0;
			for(int rowNum=0;rowNum<noOfRows;rowNum++){
				sourceRow = sourceSheet.getRow(rowNum);
				noOfCols = sourceRow.getLastCellNum();
				System.out.println("No of Cols in Row "+rowNum+" are : "+noOfCols);
				for(int colNum=0;colNum<noOfCols;colNum++){
					if(!(sourceRow.getCell(colNum)==null)){
						excelData.put((rowNum+""+colNum), sourceRow.getCell(colNum).toString());
					}
					else{
						excelData.put((rowNum+""+colNum), "");
					}
				}
			}
		}
		catch(Throwable t){
			System.out.println(t.toString());
		}
		finally{
			srcBook.close();
		}
		return excelData;
	}
	public static String[] readExcelColumn(String excelLoc,String sheetName,String columnName) throws Throwable{
		String[] excelData = null;
		int fileCheckOutCount = 0;
		while(true){
			if(!(ExecutionManager.testDataInUse || ExecutionManager.testPlanInUse)){
				ExecutionManager.testDataInUse = true;
				ExecutionManager.testPlanInUse = true;
				System.out.println("Test Data & Test Plan checked out");
				break;
			}
			if(fileCheckOutCount==100){
				return null;
			}
			Thread.sleep(1000);
			System.out.println("Waiting for Test Data & Test Case sheet"+fileCheckOutCount);
			fileCheckOutCount++;
		}
		XSSFWorkbook srcBook = new XSSFWorkbook(excelLoc);
		try{
			int noOfRowsCount = 0;
			XSSFSheet sourceSheet = srcBook.getSheet(sheetName);     //srcBook.getSheetAt(0);
			XSSFRow sourceRow = sourceSheet.getRow(0);
			int noOfRows = sourceSheet.getLastRowNum();
			String[] tempexcelData = new String[noOfRows];
			int noOfCols = sourceRow.getLastCellNum();
			for(int col=0;col<noOfCols;col++){
				if(sourceRow.getCell(col)!=null && sourceRow.getCell(col).toString().equals(columnName)){
					for(int row=0;row<noOfRows;row++){
						if(sourceSheet.getRow(row+1).getCell(col)==null){
							tempexcelData[row] = "Oops";
							continue;
						}
						noOfRowsCount++;
						tempexcelData[row] = sourceSheet.getRow(row+1).getCell(col).toString();
					}
					break;
				}
			}			
			excelData = new String[noOfRowsCount];
			for(int row=0;row<noOfRowsCount;row++){
				int oopsCount = -1;
				for(int dataCount=0;dataCount<noOfRows;dataCount++){
					if(!tempexcelData[dataCount].equals("Oops")){
						oopsCount++;
					}
					if(oopsCount==row){
						excelData[row] = tempexcelData[dataCount];
						break;
					}
				}
			}
		}
		catch(Throwable t){
			System.out.println(t.toString());
		}
		finally{
			srcBook.close();
		}
		ExecutionManager.testDataInUse = false;
		ExecutionManager.testPlanInUse = false;
		return excelData;
	}
	public static boolean updateTestDataSheet(String excelLoc,String sheetName,String dataKey,String dataValue,boolean replace) throws Throwable{
		int fileCheckOutCount = 0;
		while(true){
			if(!(ExecutionManager.testDataInUse)){
				ExecutionManager.testDataInUse = true;
				System.out.println("Test Data checked out");
				break;
			}
			if(fileCheckOutCount==100){
				return false;
			}
			Thread.sleep(1000);
			System.out.println("Waiting for Test Data sheet"+fileCheckOutCount);
			fileCheckOutCount++;
		}
		XSSFWorkbook srcBook = null;
		FileOutputStream fileOut = null;
		try{
			srcBook = new XSSFWorkbook(new FileInputStream(excelLoc));     
			XSSFSheet sourceSheet = srcBook.getSheet(sheetName);     //srcBook.getSheetAt(0);
			XSSFRow sourceRow = sourceSheet.getRow(0);
			int noOfCols = sourceRow.getLastCellNum();
			for(int colNum=0;colNum<noOfCols;colNum++){
				if(sourceRow.getCell(colNum)!=null && sourceRow.getCell(colNum).toString().equals(dataKey)){
					XSSFRow modifyRow = sourceSheet.getRow(1);
					if(replace){
						if(modifyRow.getCell(colNum)==null){
							XSSFCell dataValueCell= modifyRow.createCell(colNum);
							dataValueCell.setCellValue(dataValue);
						}
						else{
							modifyRow.getCell(colNum).setCellValue(dataValue);
						}					
					}
					else{
						String rowValue = "";
						if(modifyRow.getCell(colNum)==null){
							XSSFCell dataValueCell= modifyRow.createCell(colNum);
							dataValueCell.setCellValue(dataValue);
						}
						else{
							rowValue = modifyRow.getCell(colNum).toString();
							modifyRow.getCell(colNum).setCellValue(rowValue+";"+dataValue);
						}			
					}
					break;
				}
			}
			fileOut = new FileOutputStream(excelLoc);
		    srcBook.write(fileOut);
		}
		catch(Throwable t){
			System.out.println("Oops, test data not updated");
			System.out.println(t.toString());
		}
		finally{
			fileOut.flush();
	        fileOut.close();
			srcBook.close();
		}
		ExecutionManager.testDataInUse = false;
		return true;
	}
	public static boolean updateTestDataColumn_1(String excelLoc,String sheetName,String dataKey,String[] dataValue) throws Throwable{
		int fileCheckOutCount = 0;
		while(true){
			if(!(ExecutionManager.testDataInUse)){
				ExecutionManager.testDataInUse = true;
				System.out.println("Test Data checked out");
				break;
			}
			if(fileCheckOutCount==100){
				return false;
			}
			Thread.sleep(1000);
			System.out.println("Waiting for Test Data sheet"+fileCheckOutCount);
			fileCheckOutCount++;
		}
		XSSFWorkbook srcBook = null;
		FileOutputStream fileOut = null;
		try{
			srcBook = new XSSFWorkbook(new FileInputStream(excelLoc));     
			XSSFSheet sourceSheet = srcBook.getSheet(sheetName);     //srcBook.getSheetAt(0);
			XSSFRow sourceRow = sourceSheet.getRow(0);
			int noOfCols = sourceRow.getLastCellNum();
			for(int colNum=0;colNum<noOfCols;colNum++){
				if(sourceRow.getCell(colNum)!=null && sourceRow.getCell(colNum).toString().equals(dataKey)){
					XSSFCell modifyCell = null;
					for(int dataVal=0;dataVal<dataValue.length;dataVal++){
						sourceRow = sourceSheet.getRow(dataVal+1);
						if(sourceRow==null){
							sourceRow = sourceSheet.createRow(dataVal+1);
						}
						modifyCell = sourceRow.createCell(colNum);
						modifyCell.setCellValue(dataValue[dataVal]);
					}			
					break;
				}
			}
			fileOut = new FileOutputStream(excelLoc);
		    srcBook.write(fileOut);
		}
		catch(Throwable t){
			System.out.println("Oops, test data not updated");
			System.out.println(t.toString());
		}
		finally{
			fileOut.flush();
	        fileOut.close();
			srcBook.close();
		}
		ExecutionManager.testDataInUse = false;
		return true;
	}

}
