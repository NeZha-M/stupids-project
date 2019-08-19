package ComponentFns;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.WebDriver;

import resources.ComponentFns.ExecutionManagerHelper;
import utilityFunctions.CONSTANTS;
import utilityFunctions.ExcelTestDataManager;
import utilityFunctions.TestDataAutomation;
import utilityFunctions.TestReporting;

import com.rational.test.ft.*;
import com.rational.test.ft.object.interfaces.*;
import com.rational.test.ft.object.interfaces.SAP.*;
import com.rational.test.ft.object.interfaces.WPF.*;
import com.rational.test.ft.object.interfaces.dojo.*;
import com.rational.test.ft.object.interfaces.siebel.*;
import com.rational.test.ft.object.interfaces.flex.*;
import com.rational.test.ft.object.interfaces.generichtmlsubdomain.*;
import com.rational.test.ft.script.*;
import com.rational.test.ft.value.*;
import com.rational.test.ft.vp.*;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;
/**
 * Description   : Functional Test Script
 * @author UI171010
 */
public class ExecutionManager extends ExecutionManagerHelper
{
	public static boolean testDataInUse = false;
	public static boolean testPlanInUse = false;
	public static boolean sapInUse = false;
	public String destDir = "";
	public FileOutputStream out =null;
	public XWPFParagraph paragraphStepInfo=null;
	public XWPFParagraph tableOfContents=null;
	public XWPFParagraph imagePara=null;
	public XWPFRun imageRun=null;
	public XWPFRun stepRun=null;
	public int imageWidth = 0;
	public int imageHeight = 0;
	public XWPFParagraph paragraphExpectedActual=null;
	public XWPFRun runExpectedActual=null;
	public XWPFParagraph paragraphActual = null;
	public String path = CONSTANTS.reportsLocation;
	public XWPFDocument document=new XWPFDocument();
	public boolean testPassed = true;
	public boolean stopScript = false;
	public String reportName = "";
	public String scenarioName = "B2C";
	public WebDriver seleniumDriver = null;
	public boolean accessReport = true;
	public String methodArguments = "";
	public TestReporting testReporting = new TestReporting();

	public void testMain(Object[] args)
	{
		try{
			// TODO Insert code he
			String[] testCases = ExcelTestDataManager.readExcelColumn(CONSTANTS.testPlanLoc, "B2CTestGroup", "Test Case Name");
			String[] testExecutionInfo = ExcelTestDataManager.readExcelColumn(CONSTANTS.testPlanLoc, "B2CTestGroup", "Execute?");
			for(int testCase=0;testCase<testCases.length;testCase++){
				if(testExecutionInfo[testCase].equalsIgnoreCase(CONSTANTS.YES)){				
					String[] testSteps = ExcelTestDataManager.readExcelColumn(CONSTANTS.testPlanLoc, testCases[testCase], "TestSteps");
					String[] testStepExecutionInfo = ExcelTestDataManager.readExcelColumn(CONSTANTS.testPlanLoc, testCases[testCase], "Execute");
					String[] methodArguments = new String[testSteps.length];
					methodArguments = ExcelTestDataManager.readExcelColumn(CONSTANTS.testPlanLoc, testCases[testCase], "MethodArguments");
					//String[] testCaseInfo = testCasesInfo.get(testCases[testCase]).split(";");	
					LinkedHashMap<String,LinkedHashMap<String, String>> totalTestData = ExcelTestDataManager.readTestData(CONSTANTS.testDataLoc, testCases[testCase],"NA");
					for(Integer data=0;data<totalTestData.size();data++){				
						LinkedHashMap<String, String> testData = totalTestData.get(data.toString());
						//Test Data Automation
						if(testData.get("bpNumber").startsWith("01")){
							System.out.println("Test Data Automation not requiered");
						}
						else{
							TestDataAutomation dataAutomation = new TestDataAutomation(CONSTANTS.environment,false,"ed");
							dataAutomation.productFilter = new String[]{"",""};
							dataAutomation.TestDataRequestProcessor(testData.get("bpNumber"));
							if(dataAutomation.bpNumbers.size()==0){
								System.out.println("No test avaialable for execution");
								continue;
							}
							else{
								System.out.println("Business Partner numbers available for Testing : "+dataAutomation.bpNumbers);
								Random rand = new Random();
								int testDataRandomBPNumberIndex = rand.nextInt(dataAutomation.bpNumbers.size()-1);
								System.out.println("Business partner chosen : "+dataAutomation.bpNumbers.get(testDataRandomBPNumberIndex));
								testData.put("bpNumber", dataAutomation.bpNumbers.get(testDataRandomBPNumberIndex).split(";")[0]);
								dataAutomation = new TestDataAutomation(CONSTANTS.environment,true,"ed");
								String newUserCredentials = dataAutomation.GetNewCredentialsForUser(testData.get("bpNumber"),"energiedirect");
								testData.put("UserName", newUserCredentials.split(";")[0]);
								testData.put("Password", newUserCredentials.split(";")[1]);
							}
						}		
						reportName = scenarioName+"_"+testCases[testCase]+"_"+testData.get("bpNumber")+"_"+testData.get("TestCaseNumber");
						testData.put("testReportLoc", CONSTANTS.reportsLocation+reportName+".docx");
						testData.put("testDataLoc", CONSTANTS.testDataLoc);
						testData.put("testCaseName", testCases[testCase]);
						testReporting.createReportDataHeader(reportName,testData.get("SprintNumber")+" "+testData.get("EpicNumber")+" "+testData.get("TestCaseNumber"),scenarioName+"_"+testCases[testCase],testData.get("Description"),testData.get("Expected Result"),"UI171010","BP Number : "+testData.get("bpNumber")+", User Name : "+testData.get("UserName")+", Password : "+testData.get("Password"),"NA","NA"+" "+"NA",this);
						testPassed = true;
						stopScript = false;						
						for(int testStep=0;testStep<testSteps.length;testStep++){
							System.out.println(testSteps[testStep]);
							String[] testStepInformation = testSteps[testStep].split("\\.");
							if(testStepExecutionInfo[testStep].equalsIgnoreCase(CONSTANTS.YES)&&(!stopScript)){
								try{
									Object className = Class.forName("Classes."+testStepInformation[0]).newInstance();
									this.methodArguments = methodArguments[testStep];
									Method stepName = className.getClass().getMethod(testStepInformation[1], testData.getClass(),this.getClass());
									stepName.invoke(className, testData,this);
								}
								catch(Throwable t){
									System.out.println("Method Failed : "+testSteps[testStep]);		
									System.out.println(t.toString());
									testPassed = false;
									stopScript = true;
								}
							}
						}
						if(this.seleniumDriver!=null){
							try{
								this.seleniumDriver.quit();
							}
							catch(Throwable t){
								System.out.println("Session closed already");
							}
						}
						testReporting.createTableOfContents(testPassed,this);
					}	
				}
			}			
		}
		catch(Throwable t){
			System.out.println(t.toString());
			if(this.seleniumDriver!=null){
				try{
					this.seleniumDriver.quit();
				}
				catch(Throwable t2){
					System.out.println("Session closed already");
				}
			}
		}
		finally{
			if(this.seleniumDriver!=null){
				try{
					this.seleniumDriver.quit();
				}
				catch(Throwable t3){
					System.out.println("Session closed already");
				}
			}
		}
		
	
	}
	public void takeScreenShot(String screenshotName,ExecutionManager executionManager) {
		destDir = "C:/ED100Online/screenshots/"+scenarioName;
		String destFile ="C:/ED100Online/screenshots/"+scenarioName+ "/" + screenshotName+"_"+"RFT"+".png";
		try {
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle screenRectangle=new Rectangle(screenSize);
			Robot robot=new Robot();
			BufferedImage image=robot.createScreenCapture(screenRectangle);
			ImageIO.write(image, "png", new File(destFile));
			image.flush();

		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println(destFile);
		testReporting.addScreenshot(destFile,executionManager);
	}
}

