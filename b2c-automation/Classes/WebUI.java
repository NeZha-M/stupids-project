package Classes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import utilityFunctions.CONSTANTS;
import utilityFunctions.ExcelTestDataManager;
import ComponentFns.CF_Parent;
import ComponentFns.ExecutionManager;
import ComponentFns.CF_Parent.Platform;
import ComponentFns.CF_Parent.PropertyType;

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

public class WebUI extends ExecutionManager
{
	public boolean Login(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Login into WebUI online portal", "User should be able to login into WebUI portal",executionManager);
		try{	
			if(executionManager.methodArguments.toLowerCase().contains("chrome")){
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					startBrowser("Chrome", CONSTANTS.webUIAccURL);
				}
				else{
					startBrowser("Chrome", CONSTANTS.webUITestURL);
				}
			}
			else{
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					startBrowser(CONSTANTS.webUIAccURL);
				}
				else{
					startBrowser(CONSTANTS.webUITestURL);
				}
			}
			takeScreenShot("Launch WebUI Portal", executionManager);
			if(testData.get("Brand").toLowerCase().contains("ed")){
				CF_Parent.SendText(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Id, "sap-client-r", "050", webUIHtmlDocument());
			}
			CF_Parent.SendText(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Id, "sap-user-r", CONSTANTS.sapUserName, webUIHtmlDocument());
			CF_Parent.SendText(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Id, "sap-password-r", CONSTANTS.sapPassword, webUIHtmlDocument());
			takeScreenShot("WebUI Login Details", executionManager);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, "LOGON_BUTTON", webUIHtmlDocument());
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id,new RegularExpression(".*Z_UTIL_IC.*", false ), webUIHtmlDocument())){
				takeScreenShot("User login successful", executionManager);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*Z_UTIL_IC.*", false ), webUIHtmlDocument());
			}
			else{
				System.out.println("Login is not successful");
				executionManager.seleniumDriver.findElement(By.xpath("//*[contains(text(), 'Client, name, or password is not correct; log on again')]"));
				executionManager.testReporting.updateActualResults("SAP Credentials used for login into WebUI are incorrect",CONSTANTS.FAIL,executionManager);
				executionManager.testPassed = false;
				executionManager.stopScript = true;
				return false;
			}
			executionManager.testReporting.updateActualResults("User successfully logged into WebUI",CONSTANTS.PASS,executionManager);
		}
		catch(Throwable t){
			takeScreenShot("Login into web portal failed", executionManager);
			executionManager.testReporting.updateActualResults("Login into WebUI Portal failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean VerifyProduct(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Verify the Given products in WebUI", "Product Verification in WebUI should be successful",executionManager);
		try{	
			ArrayList<String> productsForVerification = new ArrayList<String>();
			if(executionManager.methodArguments.contains("Ele")){
				String[] readProduct = ExcelTestDataManager.readExcelColumn(CONSTANTS.testDataLoc, testData.get("testCaseName"), "Product_E");
				productsForVerification.add(readProduct[0]);
			}
			else if(executionManager.methodArguments.contains("Gas")){
				String[] readProduct = ExcelTestDataManager.readExcelColumn(CONSTANTS.testDataLoc, testData.get("testCaseName"), "Product_G");
				productsForVerification.add(readProduct[0]);
			}
			else if(executionManager.methodArguments.contains("Both")){
				String[] readProduct = ExcelTestDataManager.readExcelColumn(CONSTANTS.testDataLoc, testData.get("testCaseName"), "Product_E");
				productsForVerification.add(readProduct[0]);
				readProduct = ExcelTestDataManager.readExcelColumn(CONSTANTS.testDataLoc, testData.get("testCaseName"), "Product_G");
				productsForVerification.add(readProduct[0]);
			}
			else{
				executionManager.testReporting.updateActualResults("Unknown Method arguments for product verification",CONSTANTS.FAIL,executionManager);
				return false;
			}
			ClearAndEndTransations();
			Thread.sleep(2000);
			takeScreenShot("Transactions Cleared", executionManager);
			SearchBusinessParnter(testData, executionManager);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Overview", webUIHtmlDocument());
			CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*account_addr_short.*", false), webUIHtmlDocument());
			takeScreenShot("Overview Page", executionManager);
			TestObject[] productsAvailable = webUIHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.SPAN",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*contracts_Table.*ORDERED_PROD.*", false)), false);
			ArrayList<String> productsFromWebUI = new ArrayList<String>();
			for (TestObject productAvailable : productsAvailable) {
				productsFromWebUI.add(productAvailable.getProperty(".text").toString());
			}
			String productNotAvailable = "";
			for (String productForVerification : productsForVerification) {
				if(!productsFromWebUI.contains(productForVerification)){
					if(productNotAvailable.equals("")){
						productNotAvailable = productForVerification;
					}
					else{
						productNotAvailable = productNotAvailable + "," +productForVerification;
					}
				}
			}
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Contract Management", webUIHtmlDocument());
			Thread.sleep(2000);
			takeScreenShot("Contract Details of the Customer", executionManager);
			RegularExpression regularExpression = new RegularExpression(".*contractlist_table.*division_desc.*", false);
			TestObject[] productDescriptions = webUIHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),regularExpression), false);
			for (int productDescription=0;productDescription<productDescriptions.length;productDescription++) {
				productDescriptions = webUIHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),regularExpression), false);
				((GuiTestObject)productDescriptions[productDescription]).click();
				String connectionType = productDescriptions[productDescription].getProperty(".text").toString();
				Thread.sleep(1000);
				takeScreenShot("Summary_"+connectionType, executionManager);
				CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Contract Data", webUIHtmlDocument(),10,10);
				Thread.sleep(1000);
				takeScreenShot("Contract Data_"+connectionType, executionManager);
				CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Product", webUIHtmlDocument(),10,10);
				Thread.sleep(1000);
				takeScreenShot("Product_"+connectionType.toString(), executionManager);
				CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Status", webUIHtmlDocument(),10,10);
				Thread.sleep(1000);
				takeScreenShot("Status_"+connectionType, executionManager);
			}
			if(productNotAvailable.equals("")){
				executionManager.testReporting.updateActualResults("Product verification in WebUI Successful for products "+productsForVerification,CONSTANTS.PASS,executionManager);
			}
			else{
				executionManager.testReporting.updateActualResults("Product verification in WebUI Failed for products "+productNotAvailable,CONSTANTS.FAIL,executionManager);
			}
		}
		catch(Throwable t){
			takeScreenShot("Product verification in WebUI failed", executionManager);
			executionManager.testReporting.updateActualResults("Product verification in WebUI Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public void ClearAndEndTransations(){
		CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Clear Interaction", webUIHtmlDocument());
		sleep(2);
		getScreen().inputKeys("{ENTER}");
		CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "End", webUIHtmlDocument());
	}
	public boolean SearchBusinessParnter(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*search_struct.partner.*", false), webUIHtmlDocument());
		sleep(1);
		CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*search_struct.partner.*", false), webUIHtmlDocument());
		CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*search_struct.partner.*", false), testData.get("bpNumber"), webUIHtmlDocument());
		CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Search", webUIHtmlDocument(),10,10);
		CF_Parent.iLoopCount = 3;
		if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class,"Html.TABLE", PropertyType.Text, "Connection Object", webUIHtmlDocument())){
			takeScreenShot("Search Results for Business Partner", executionManager);
		}
		else{
			takeScreenShot("Search Results for Business Partner", executionManager);
			executionManager.testReporting.updateActualResults("No Search results found for given business partner",CONSTANTS.PASS,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			return false;
		}
		return true;
	}
	public boolean ChangeProduct(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Change product from WebUI Portal", "Product change should be successful form WebUI",executionManager);
		try{
			ClearAndEndTransations();
			if(!SearchBusinessParnter(testData, executionManager)){
				return false;
			}
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Contract Management", webUIHtmlDocument());
			Thread.sleep(2000);
			takeScreenShot("Contract Details of the Customer", executionManager);
			int productChangeIndex = 0;
			boolean productFound = false;
			RegularExpression regularExpression = null;
			regularExpression = new RegularExpression(".*contractlist_table.*division_desc.*", false);
			TestObject[] productDescriptions = webUIHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),regularExpression), false);
			if(executionManager.methodArguments.contains("Ele")){
				for (TestObject productDescription : productDescriptions) {
					productChangeIndex++;
					if(productDescription.getProperty(".text").equals("Elektra")){
						productFound = true;
						((GuiTestObject)productDescription).click();
						Thread.sleep(2000);
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContractTable_sel_"+productChangeIndex+"-rowsel.*", false), webUIHtmlDocument());
					}
				}
			}
			else if(executionManager.methodArguments.contains("Gas")){
				for (TestObject productDescription : productDescriptions) {
					productChangeIndex++;
					if(productDescription.getProperty(".text").equals("Gas")){
						productFound = true;
						((GuiTestObject)productDescription).click();
						Thread.sleep(2000);
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContractTable_sel_"+productChangeIndex+"-rowsel.*", false), webUIHtmlDocument());
					}
				}
			}
			else{
				executionManager.testReporting.updateActualResults("Unknown connection type to change product",CONSTANTS.FAIL,executionManager);
				return false;
			}
			if(productFound){
				Thread.sleep(2000);
				takeScreenShot("Connection selected for product change", executionManager);
			}
			else{
				executionManager.testReporting.updateActualResults("Given connection type is not found for product chagne",CONSTANTS.FAIL,executionManager);
				executionManager.stopScript = true;
			} 
			CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ChangeContracts.*", false), webUIHtmlDocument(),10,10);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*process_ddlb.*", false), webUIHtmlDocument());
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Product change", webUIHtmlDocument());
			takeScreenShot("Product Change selection", executionManager);
			Thread.sleep(1000);
			CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id,  new RegularExpression(".*start_process.*", false), webUIHtmlDocument(),10,10);
			Thread.sleep(2000);
			CF_Parent.iLoopCount = 3;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id,  new RegularExpression(".*start_process.*", false), webUIHtmlDocument())){
				CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id,  new RegularExpression(".*start_process.*", false), webUIHtmlDocument(),10,10);
				Thread.sleep(2000);
			}
			takeScreenShot("Process Started", executionManager);
			regularExpression = new RegularExpression(".*ordered_prod.*", false);
			testData.put("OldProduct", CF_Parent.ReadEditBoxText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, regularExpression, webUIHtmlDocument()));
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.IMG", PropertyType.Alt, "Open Input Help", webUIHtmlDocument());
			Thread.sleep(2000);
			takeScreenShot("Select New Product Pop up", executionManager);
			regularExpression = new RegularExpression(".*popup-name.*", false);
			RootTestObject rootObj = RootTestObject.getRootTestObject();
			TestObject[] popUpDocument = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.URL,Platform.WEB),regularExpression));
			for(int htmlDocTrail = 0;htmlDocTrail<10;htmlDocTrail++){
				if(popUpDocument.length==0){
					popUpDocument = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.URL,Platform.WEB),regularExpression));
				}
				else{
					break;
				}
				Thread.sleep(2);
			}
			regularExpression = new RegularExpression(".*search_param01.*", false);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, regularExpression, popUpDocument[0]);
			if(executionManager.methodArguments.contains("Gas")){
				getScreen().inputKeys("02");getScreen().inputKeys("{ENTER}");
				Thread.sleep(1000);getScreen().inputKeys("{ENTER}");
			}
			else{
				getScreen().inputKeys("01");getScreen().inputKeys("{ENTER}");
				Thread.sleep(1000);getScreen().inputKeys("{ENTER}");
			}
			//CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.ContentText, "Search", popUpDocument[0]); //Problem with this statement, RFT unable to recognize
			Thread.sleep(2000);
			takeScreenShot("Product available for Selection", executionManager);
			regularExpression = new RegularExpression(".*product_table.*product_id.*", false);
			productDescriptions = popUpDocument[0].find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.SPAN",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),regularExpression), false);
			ArrayList<String> availableProducts = new ArrayList<String>();
			for (TestObject productDescription : productDescriptions) {
				if(productDescription.getProperty(".text").equals(testData.get("OldProduct")) || productDescription.getProperty(".text").toString().contains("BD") 
						|| productDescription.getProperty(".text").toString().contains("M") || productDescription.getProperty(".text").toString().contains("E_H_BV")){
					System.out.println("Existing product");
				}
				else{
					availableProducts.add(productDescription.getProperty(".text").toString());
				}
			}
			((GuiTestObject)popUpDocument[0]).click();
			Thread.sleep(1000);
			getScreen().inputKeys("%{F4}");
			Random rand = new Random();
			int randomProduct = rand.nextInt(availableProducts.size());
			testData.put("NewProduct", availableProducts.get(randomProduct));
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*item_struct.ordered_prod.*", false), testData.get("NewProduct"), webUIHtmlDocument());
			if(testData.get("NewProduct").contains("Z")){
				Calendar calendarTodayInstance = Calendar.getInstance();
				int presentMonth = calendarTodayInstance.get(Calendar.MONTH) + 1;
				int presentYear = calendarTodayInstance.get(Calendar.YEAR);
				Calendar tempCalendarInstance = Calendar.getInstance();
				tempCalendarInstance.clear();
				tempCalendarInstance.set(Calendar.YEAR, presentYear);
				tempCalendarInstance.set(Calendar.MONTH, presentMonth);
				tempCalendarInstance.set(Calendar.DAY_OF_MONTH, 1);
				Date firstDateOfNextMonth = new Date(tempCalendarInstance.getTimeInMillis());
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
				String nextMonthFirstDay = dateFormatter.format(firstDateOfNextMonth);
				System.out.println(nextMonthFirstDay);
				CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*item_struct.isurqcontsrt.*", false), nextMonthFirstDay, webUIHtmlDocument());
				CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id, new RegularExpression(".*item_z_cont_start_or.*", false), nextMonthFirstDay, webUIHtmlDocument());

			}
			takeScreenShot("New Product Selected", executionManager);
			CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*finish_process.*", false), webUIHtmlDocument(),10,10);
			Thread.sleep(5000);
			takeScreenShot("Save Process", executionManager);
			Thread.sleep(5000);
			CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.DIV", PropertyType.Id, new RegularExpression(".*messarea.*", false), webUIHtmlDocument(),10,10);
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.DIV", PropertyType.Text, "Process Replacement Product Change was executed successfully", webUIHtmlDocument())){
				takeScreenShot("Product Change success screen", executionManager);
				CF_Parent.ClickButtonPoint(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Contract Data", webUIHtmlDocument(),10,10);
				executionManager.testReporting.updateActualResults("Product Change successful from WebUI, new Product "+testData.get("NewProduct"),CONSTANTS.PASS,executionManager);
				if(executionManager.methodArguments.contains("Gas")){
					ExcelTestDataManager.updateTestDataSheet(testData.get("testDataLoc"), testData.get("testCaseName"), "Product_G",testData.get("NewProduct"), true);
				}
				else{
					ExcelTestDataManager.updateTestDataSheet(testData.get("testDataLoc"), testData.get("testCaseName"), "Product_E",testData.get("NewProduct"), true);
				}
			}
			else{
				takeScreenShot("Product Change error message", executionManager);
				executionManager.testReporting.updateActualResults("Product Change not successful from WebUI",CONSTANTS.FAIL,executionManager);
			}
		}
		catch(Throwable t){
			takeScreenShot("Product Change from WebUI Portal failed", executionManager);
			executionManager.testReporting.updateActualResults("Change product from WebUI Portal failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
}