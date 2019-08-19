package Classes;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import resources.Classes.SAPHelper;
import utilityFunctions.CONSTANTS;
import utilityFunctions.ExcelTestDataManager;
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

import cucumber.api.java.sk.Tak;
/**
 * Description   : Functional Test Script
 * @author UI171010
 */
public class SAP extends SAPHelper
{
	public void testMain(Object[] args) 
	{
		
	}
	public boolean Login(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Login into SAP", "User should be able to login into SAP",executionManager);
		try{	
			String[] loginArguments = executionManager.methodArguments.split(";");
			if(!Logout(testData,executionManager)){
				startApp("saplogon");
			}
			sapLogon720window().activate();
			sleep(1);
			TestObject[] listViewTable = sapLogon720window().find(atList(atDescendant(".class", "SysListView32")));
			System.out.println(listViewTable.length);
			( (SelectGuiSubitemTestObject) listViewTable[0]).doubleClick(atCell(atRow(atText(loginArguments[0])),atColumn(atText("Name"))));
			if(loginArguments[1].toLowerCase().contains(CONSTANTS.energieDirect.toLowerCase())){
				CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiTextField", PropertyType.Name, "RSYST-MANDT", "050", window_sap());
			}
			else{
				CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiTextField", PropertyType.Name, "RSYST-MANDT", "040", window_sap());
			}
			CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiTextField", PropertyType.Name, "RSYST-BNAME", CONSTANTS.sapUserName, window_sap());
			CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiPasswordField", PropertyType.Name, "RSYST-BCODE", CONSTANTS.sapPassword, window_sap());
			if(loginArguments[2].toLowerCase().contains("nl")){
				CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiTextField", PropertyType.Name, "RSYST-LANGU", "NL", window_sap());
			}
			else{
				CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiTextField", PropertyType.Name, "RSYST-LANGU", "EN", window_sap());
			}
			button_enter().press();
			
			// If system message gets displayed
			if(dialog_systemMessages().exists() && button_cancel().exists()){
				System.out.println("In If Block 1");
				button_cancel().press();
				sleep(2);
			}

			// Copy right pop up comes, accept it
			else if(dialog_copyright().exists() && button_continue().exists()){
				System.out.println("In If Block 4");
				button_continue().press();
				sleep(2);
			}
			else if(dialog_systemMessages().exists() && button_cancel().exists()){
				System.out.println("In If Block 3");
				button_cancel().press();
				sleep(2);
			}
			// When Information window appears
			else if(dialog_information().exists() && button_continue2().exists()){
				System.out.println("In If Block 5");
				button_continue2().press();
				sleep(2);
			}
			// If system message gets displayed
			else if(dialog_systemMessages().exists() && button_cancel().exists()){
				button_cancel().press();
				sleep(2);
			}
			else{
				executionManager.takeScreenShot("Login into SAP failed", executionManager);
				executionManager.testReporting.updateActualResults("User login into SAP failed",CONSTANTS.FAIL,executionManager);
				executionManager.testPassed = false;
				executionManager.stopScript = true;
				return false;
			}
			executionManager.takeScreenShot("SAP Login successful", executionManager);
			executionManager.testReporting.updateActualResults("User logged into "+loginArguments[0],CONSTANTS.PASS,executionManager);

		}
		catch(Throwable t){
			executionManager.takeScreenShot("Login into SAP failed", executionManager);
			executionManager.testReporting.updateActualResults("Login into SAP failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean Logout(LinkedHashMap<String, String> testData,ExecutionManager executionManager)
	{
		boolean isLogout = false;
		try
		{
			if(comboBox_okcd().exists())
			{
				searchTCode("/nex");
				isLogout = true;
			}
		}catch(Throwable t)
		{

			logError("Ëxception = "+t.toString());
			isLogout = false;
		}
		return isLogout;
	}
	public boolean searchTCode(String tCode)
	{
		String tCodeSAP = tCode;
		try
		{ 
			comboBox_okcd().setText(tCodeSAP);
			button_enter().press();
			return true;
		}catch(Throwable t)
		{
			logError("Ëxception = "+t.toString());
			return false;
		}
	}
	public boolean VerifyProduct(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Verify the Given products in SAP", "Product Verification in SAP should be successful",executionManager);
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
			searchTCode("/necenv_bp");
			CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiCTextField", PropertyType.Name, "ISU_TIMESL-AB", "",window_sap());
			CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiCTextField", PropertyType.Name, "ISU_TIMESL-BIS", "",window_sap());
			//Name:  EKUN_EXT-PARTNER
			CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiCTextField", PropertyType.Name, "EKUN_EXT-PARTNER", "",window_sap());
			CF_Parent.EnterText(Platform.SAP, PropertyType.Class, "GuiCTextField", PropertyType.Name, "EKUN_EXT-PARTNER", testData.get("bpNumber"),window_sap());
			executionManager.takeScreenShot("Business Partner Search Information", executionManager);
			button_enter().press();
			TestObject[] contractLables = (TestObject[])panel_usr().find(atList(atChild(".class", "GuiLabel","Text","Utility contract")));
			executionManager.takeScreenShot("Contracts of Customer", executionManager);
			String productNotAvailable = "";
			for (String productToVerify : productsForVerification) {
				boolean productFound = false;
				for(int contractLabel=0;contractLabel<contractLables.length;contractLabel++){
					contractLables = (TestObject[])panel_usr().find(atList(atChild(".class", "GuiLabel","Text","Utility contract")));
					((SAPGuiTextTestObject)contractLables[contractLabel]).doubleClick(atPoint(4, 4));
					CF_Parent.ClickTab(Platform.SAP, PropertyType.Class, "GuiTab", PropertyType.Id, "/tabpTAB03", window_sap());
					Thread.sleep(2000);
					TestObject[] crmProducts = window_sap().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.SAP),"GuiTextField",CF_Parent.getPropertyType(PropertyType.Name,Platform.SAP),new RegularExpression(".*EVERH-CRM_PRODUCT.*", false)), false);
					if(crmProducts.length==0){
						crmProducts = window_sap().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.SAP),"GuiTextField",CF_Parent.getPropertyType(PropertyType.Name,Platform.SAP),new RegularExpression(".*EVERH-CRM_PRODUCT.*", false)), false);
					}
					for(int crmProduct=0;crmProduct<crmProducts.length;crmProduct++){
						if(crmProducts[crmProduct].getProperty("Text").toString().equals(productToVerify)){
							productFound = true;
							executionManager.takeScreenShot("Product "+productToVerify+" found", executionManager);
							break;
						}
					}
					if(productFound){
						button_back().click();
						break;
					}
					else{
						button_back().click();
					}
				}
				if(!productFound){
					if(productNotAvailable.equals("")){
						productNotAvailable = productToVerify;
					}
					else{
						productNotAvailable = productNotAvailable + "," +productToVerify;
					}
				}
			}
			if(productNotAvailable.equals("")){
				executionManager.testReporting.updateActualResults("Product verification in SAP Successful for products "+productsForVerification,CONSTANTS.PASS,executionManager);
			}
			else{
				executionManager.testReporting.updateActualResults("Product verification in SAP Failed for products "+productNotAvailable,CONSTANTS.FAIL,executionManager);
			}
		}
		catch(Throwable t){
			executionManager.takeScreenShot("Product verification in WebUI failed", executionManager);
			executionManager.testReporting.updateActualResults("Product verification in WebUI Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean VerifySwitchDocs(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Verify the Given switch doc status in SAP", "Switch Doc Verification in SAP should be successful",executionManager);
		try{
			searchTCode("/neswtmon01");
			String eanNumbers[] = ExcelTestDataManager.readExcelColumn(CONSTANTS.testDataLoc, testData.get("testCaseName"), "EANNumbers");
			for (String eanNumber : eanNumbers) {
				
			}
		}
		catch(Throwable t){
			executionManager.takeScreenShot("Switch Doc verification in SAP failed", executionManager);
			executionManager.testReporting.updateActualResults("Switch Doc verification in SAP Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
}

