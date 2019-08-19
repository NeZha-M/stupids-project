package Classes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import resources.Classes.TibcoHelper;
import utilityFunctions.CONSTANTS;
import ComponentFns.CF_Parent;
import ComponentFns.CF_Parent.Platform;
import ComponentFns.CF_Parent.PropertyType;
import ComponentFns.ExecutionManager;

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

import cucumber.api.java.bm.Tetapi;

/**
 * Description   : Functional Test Script
 * @author UI171010
 */
public class Tibco extends ExecutionManager
{
	public void testMain(Object[] args) 
	{
		
	}
	public boolean Login(LinkedHashMap<String, String> testData,ExecutionManager executionManager)
	{
		executionManager.testReporting.updateStepInfo("Login into Tibco", "User should be able to login into Tibco",executionManager);
		try{	
			if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
				startBrowser("Internet Explorer",CONSTANTS.tibcoAccURL);
			}
			else{
				startBrowser("Internet Explorer",CONSTANTS.tibcoTestURL);
			}
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "txtUserId", "ui745626", tibcoHtmlDocument());
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Id, new RegularExpression(".*jsx_0_13.*", false), tibcoHtmlDocument());
			sleep(1);
			if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.DIV", PropertyType.Id, new RegularExpression(".*Greenfield.*ACC.*", false), tibcoHtmlDocument());
			}
			else{
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.DIV", PropertyType.Id, new RegularExpression(".*Greenfield.*TST.*", false), tibcoHtmlDocument());
			}
			takeScreenShot("Tibco login credentials", executionManager);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, "Login", tibcoHtmlDocument());
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Title, new RegularExpression(".*logout.*", false), tibcoHtmlDocument())){
				takeScreenShot("Login successful", executionManager);
				executionManager.testReporting.updateActualResults("Login successful",CONSTANTS.PASS,executionManager);
			}
			else{
				takeScreenShot("Login into Tibco portal failed", executionManager);
				executionManager.testReporting.updateActualResults("Login into Tibco Portal failed",CONSTANTS.FAIL,executionManager);
				executionManager.testPassed = false;
				executionManager.stopScript = true;
			}
		}
		catch(Throwable t){
			takeScreenShot("Login into Tibco portal failed", executionManager);
			executionManager.testReporting.updateActualResults("Login into Tibco Portal failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean SearchProcedure(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Search Procedure code in Tibco "+executionManager.methodArguments, "Given procedure code should be searched",executionManager);
		try{
			String[] procedureInformation = executionManager.methodArguments.split(";");
			CF_Parent.ignoreLefOffset = true;
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.DIV", PropertyType.ContentText, "Procedures", tibcoHtmlDocument());
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Title, new RegularExpression(".*find items.*", false), tibcoHtmlDocument());
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "txtStartsWith", procedureInformation[0], tibcoHtmlDocument());
			Thread.sleep(2000);
			CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+procedureInformation[0]+".*"+procedureInformation[1]+".*",false), tibcoHtmlDocument(),10,10);
			Thread.sleep(1000);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Title, "filter list contents", tibcoHtmlDocument());
			if(procedureInformation[2].equalsIgnoreCase("bpNumber")){
				CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.ClassName, new RegularExpression(".*combo_text.*", false), "Case Description", tibcoHtmlDocument());
				CF_Parent.iValidObjectIndex = 1;
				CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.ClassName, new RegularExpression(".*textbox.*", false), testData.get("bpNumber"), tibcoHtmlDocument());
			}
			// Other ways of searching procedure code
			else{
				CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.ClassName, new RegularExpression(".*combo_text.*", false), testData.get("SearchParameter"), tibcoHtmlDocument());
				CF_Parent.iValidObjectIndex = 1;
				CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.ClassName, new RegularExpression(".*textbox.*", false), testData.get("SearchValue"), tibcoHtmlDocument());
			}
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, "Apply", tibcoHtmlDocument());
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, "OK", tibcoHtmlDocument());
			CF_Parent.ignoreLefOffset = true;
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.TextNode", PropertyType.Text, "Started Date/Time", tibcoHtmlDocument());
			sleep(2);
			CF_Parent.ignoreLefOffset = true;
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.TextNode", PropertyType.Text, "Started Date/Time", tibcoHtmlDocument());
			String searchResult = testData.get("bpNumber");
			if(!procedureInformation[2].equalsIgnoreCase("bpNumber")){
				searchResult = testData.get("SearchValue");
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.TABLE",PropertyType.Text, new RegularExpression(".*"+searchResult+".*", false), tibcoHtmlDocument())){
				takeScreenShot("Procedure searched successfully", executionManager);
				executionManager.testReporting.updateActualResults("Prodcedure code "+executionManager.methodArguments+" search successful",CONSTANTS.PASS,executionManager);
			}
			else{
				executionManager.testReporting.updateActualResults("Prodcedure code "+executionManager.methodArguments+" search resulted in 0 records",CONSTANTS.FAIL,executionManager);
				return false;
			}
		}
		catch(Throwable t){
			takeScreenShot("Unalbed to search procedure code", executionManager);
			executionManager.testReporting.updateActualResults("Prodcedure code "+executionManager.methodArguments+" search failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			t.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean VerifyProcedureStatus(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Verify Procedure code status in Tibco", "Given procedure code status should be verified",executionManager);
		try{
			String[] procedureInformation = executionManager.methodArguments.split(";");
			boolean procedureCodeStatusCorrect = false;
			int numberOfIterations = Integer.parseInt(procedureInformation[1]);
			for(int iteration=0;iteration<numberOfIterations;iteration++){
				if(procedureCodeStatusCorrect){
					CF_Parent.iValidObjectIndex = 1;
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Title, "refresh list contents", tibcoHtmlDocument());
					if(procedureInformation[2].equalsIgnoreCase("bpNumber")){
						CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("bpNumber")+".*",false), tibcoHtmlDocument(),10,10);
					}
					else{
						CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("SearchValue")+".*",false), tibcoHtmlDocument(),10,10);
					}
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, "Summary", tibcoHtmlDocument());
					break;
				}
				if(procedureInformation[2].equalsIgnoreCase("bpNumber")){
					CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("bpNumber")+".*",false), tibcoHtmlDocument(),10,10);
				}
				else{
					CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("SearchValue")+".*",false), tibcoHtmlDocument(),10,10);
				}
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, "Summary", tibcoHtmlDocument());
				TestObject[] caseSummaryObjects = tibcoHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.DIV",CF_Parent.getPropertyType(PropertyType.ClassName,Platform.WEB),"jsx30block"), false);
				String caseSummary = "Fail";
				for (TestObject caseSummaryObject : caseSummaryObjects) {
					if(caseSummaryObject.getProperty(".text").toString().startsWith("Case Summary Case Description")){
						caseSummary = caseSummaryObject.getProperty(".text").toString();
					}
				}
				if(caseSummary.contains("Status: "+procedureInformation[0])){
					procedureCodeStatusCorrect = true;
				}
				else{
					Thread.sleep(10000);
				}
			}		
			takeScreenShot("Procedure code information", executionManager);
			if(procedureCodeStatusCorrect){
				executionManager.testReporting.updateActualResults("Prodcedure code is in "+procedureInformation[0]+" status",CONSTANTS.PASS,executionManager);
			}
			else{
				executionManager.testReporting.updateActualResults("Prodcedure code is in "+procedureInformation[0]+" status",CONSTANTS.FAIL,executionManager);
				executionManager.testPassed = false;
				executionManager.stopScript = true;
			}
		}
		catch(Throwable t){
			takeScreenShot("Unalbed to verify procedure code status", executionManager);
			executionManager.testReporting.updateActualResults("Prodcedure code "+executionManager.methodArguments+" status verification failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			t.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean HandleProcedure(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Handle the given procedure or wait for certain status to reach", "Given procedure code should reach the required state",executionManager);
		try{
			String[] procedureInformation = executionManager.methodArguments.split(";");
			boolean procedureCodeStateReached = false;
			int numberOfIterations = Integer.parseInt(procedureInformation[1]);
			ArrayList<String> handleStates = new ArrayList<String>(Arrays.asList(procedureInformation[3].split(",")));
			for(int iteration=0;iteration<numberOfIterations;iteration++){
				if(procedureCodeStateReached){
					CF_Parent.iValidObjectIndex = 1;
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Title, "refresh list contents", tibcoHtmlDocument());
					if(procedureInformation[2].equalsIgnoreCase("bpNumber")){
						CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("bpNumber")+".*",false), tibcoHtmlDocument(),10,10);
					}
					else{
						CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("SearchValue")+".*",false), tibcoHtmlDocument(),10,10);
					}					
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, "Outstanding", tibcoHtmlDocument());
					break;
				}
				if(procedureInformation[2].equalsIgnoreCase("bpNumber")){
					CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("bpNumber")+".*",false), tibcoHtmlDocument(),10,10);
				}
				else{
					CF_Parent.DoubleClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Text, new RegularExpression(".*"+testData.get("SearchValue")+".*",false), tibcoHtmlDocument(),10,10);
				}		
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, "Outstanding", tibcoHtmlDocument());
				CF_Parent.iValidObjectIndex = 2;
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Title, "refresh list contents", tibcoHtmlDocument());
				try{
					TestObject[] outstandingResults = tibcoHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.TABLE",CF_Parent.getPropertyType(PropertyType.ClassName,Platform.WEB),"jsx30matrix_rowtable"), false);
					StatelessGuiSubitemTestObject outstandingResultTable =(StatelessGuiSubitemTestObject)((GuiTestObject)outstandingResults[outstandingResults.length-1]);
					ITestDataTable outStanding= (ITestDataTable) (outstandingResultTable.getTestData("contents"));
					for(int rowNumber=0;rowNumber<outStanding.getRowCount();rowNumber++){
						if(procedureInformation[0].equals(outStanding.getCell(rowNumber, 1)+"|"+outStanding.getCell(rowNumber, 2))){
							takeScreenShot("Procedure reached required state", executionManager);
							procedureCodeStateReached = true;
							break;
						}
						if(handleStates.contains(outStanding.getCell(rowNumber, 1)+"|"+outStanding.getCell(rowNumber, 2))){
							ReleaseTibcoStep(outStanding.getCell(rowNumber, 1)+";"+outStanding.getCell(rowNumber, 2), executionManager);
							iteration = 0;
						}
					}
				}
				catch(Throwable t){
					System.out.println("No Outstanding table");
				}
				Thread.sleep(5000);
			}		
			if(procedureCodeStateReached){
				executionManager.testReporting.updateActualResults("Prodcedure code is in "+procedureInformation[0]+" status",CONSTANTS.PASS,executionManager);
			}
			else{
				executionManager.testReporting.updateActualResults("Prodcedure code is in "+procedureInformation[0]+" status",CONSTANTS.FAIL,executionManager);
				executionManager.testPassed = false;
				executionManager.stopScript = true;
			}
		}
		catch(Throwable t){
			takeScreenShot("Unalbed to verify procedure code status", executionManager);
			executionManager.testReporting.updateActualResults("Prodcedure code "+executionManager.methodArguments+" status verification failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			t.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean ReleaseTibcoStep(String tibcoResult,ExecutionManager executionManager){
		try{
			String[] tibcoProcedureDetails = tibcoResult.split(";");
			TestObject[] outstandingResults = tibcoHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.TABLE",CF_Parent.getPropertyType(PropertyType.ClassName,Platform.WEB),"jsx30matrix_rowtable"), false);
			StatelessGuiSubitemTestObject outstandingResultTable =(StatelessGuiSubitemTestObject)((GuiTestObject)outstandingResults[outstandingResults.length-1]);
			ITestDataTable outStanding= (ITestDataTable) (outstandingResultTable.getTestData("contents"));
			for(int rowNumber=0;rowNumber<outStanding.getRowCount();rowNumber++){
				if(outStanding.getCell(rowNumber, 1).toString().equals(tibcoProcedureDetails[0])&&outStanding.getCell(rowNumber, 2).toString().equals(tibcoProcedureDetails[1])){
					outstandingResultTable.doubleClick(atCell(atRow(atIndex(rowNumber)),atColumn(atIndex(2))));
					try{
						CF_Parent.iLoopCount = 3;
						CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, "Release", tibcoHtmlDocument());
						takeScreenShot("Tibco step name to be released", executionManager);
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, "Release", tibcoHtmlDocument());
					}
					catch(Throwable t){
						CF_Parent.iLoopCount = 4;
						CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, "releaseButton", placeHolderDocument());
						takeScreenShot("Tibco step name to be released", executionManager);
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, "Release", placeHolderDocument());
					}
					break;
				
				}

			}
			takeScreenShot(tibcoResult+" released successfully", executionManager);
			return true;
		}
		catch(Throwable t){
			takeScreenShot("Releasing Tibco Message "+tibcoResult+" failed", executionManager);
			return false;
		}
	}
}

