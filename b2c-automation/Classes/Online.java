package Classes;

import java.util.LinkedHashMap;

import utilityFunctions.CONSTANTS;
import ComponentFns.CF_Parent;
import ComponentFns.ExecutionManager;
import ComponentFns.CF_Parent.Platform;
import ComponentFns.CF_Parent.PropertyType;

import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.value.RegularExpression;

public class Online extends ExecutionManager{
	TestObject mijnHtmlDocument = null;
	public boolean Login(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Login into online portal", "User should be able to login into portal",executionManager);
		try{	
			String regularExpression = ".*Mijn Essent.*";
			if(executionManager.methodArguments.toLowerCase().contains("ed")){
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					if(executionManager.methodArguments.toLowerCase().contains("chrome")){
						startBrowser("Chrome",CONSTANTS.mijnEDAccURL);
					}
					else{
						startBrowser(CONSTANTS.mijnEDAccURL);
					}
				}
				else{
					if(executionManager.methodArguments.toLowerCase().contains("chrome")){
						startBrowser("Chrome",CONSTANTS.mijnEDTestURL);
					}
					else{
						startBrowser(CONSTANTS.mijnEDTestURL);
					}
				}
				regularExpression = ".*Mijn energiedirect.*";
			}
			else{
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					if(executionManager.methodArguments.toLowerCase().contains("chrome")){
						startBrowser("Chrome",CONSTANTS.mijnEssentAccURL);
					}
					else{
						startBrowser(CONSTANTS.mijnEssentAccURL);
					}
				}
				else{
					if(executionManager.methodArguments.toLowerCase().contains("chrome")){
						startBrowser("Chrome",CONSTANTS.mijnEssentTestURL);
					}
					else{
						startBrowser(CONSTANTS.mijnEssentTestURL);
					}
				}
			}
			RootTestObject rootObj = RootTestObject.getRootTestObject();
            TestObject[] htmlDocuments = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.Title,Platform.WEB),new RegularExpression(regularExpression, false)));
            boolean mijnEssentDocumentFound = false;
            for(int htmlDocTrail = 0;htmlDocTrail<10;htmlDocTrail++){
                   if(htmlDocuments.length==0){
                          htmlDocuments = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.Title,Platform.WEB),new RegularExpression(regularExpression, false)));
                   }
                   else{
                          mijnEssentDocumentFound = true;
                          break;
                   }
                   sleep(2);
            }
            if(!mijnEssentDocumentFound){
                   executionManager.testReporting.updateActualResults("Browser Launch Failed",CONSTANTS.FAIL,executionManager);
                   executionManager.stopScript = true;
                   executionManager.testPassed = false;
                   return false;
            }
            else{
                   mijnEssentDocumentFound = false;
                   for(int htmpPageLoadTrail=0;htmpPageLoadTrail<10;htmpPageLoadTrail++){
                          try{
                                 CF_Parent.EnterText(Platform.WEB,PropertyType.Class,"Html.INPUT.text",PropertyType.Name, "username",testData.get("UserName"), htmlDocuments[0]);
                                 mijnEssentDocumentFound = true;
                                 break;
                          }
                          catch(Throwable t){
                                 htmlDocuments = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.Title,Platform.WEB),new RegularExpression(".*Mijn Essent.*", false)));
                                 System.out.println("Element not visible");
                          }
                          sleep(2);
                   }
            }
            if(!mijnEssentDocumentFound){
            	 executionManager.testReporting.updateActualResults("User name field is not found",CONSTANTS.FAIL,executionManager);
                 executionManager.stopScript = true;
                 executionManager.testPassed = false;
                 return false;
            }
            CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "username", testData.get("UserName"), htmlDocuments[0]);
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.password", PropertyType.Name, "password", testData.get("Password"), htmlDocuments[0]);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, new RegularExpression(".*Inloggen.*", false), htmlDocuments[0]);
			CF_Parent.iLoopCount = 4;
			if(!CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Overzicht", htmlDocuments[0])){
	            htmlDocuments = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.Title,Platform.WEB),new RegularExpression(regularExpression, false)));
				if(!CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Overzicht", htmlDocuments[0])){
					takeScreenShot("Login into mpr portal failed", executionManager);
					executionManager.testReporting.updateActualResults("Login into online Portal failed",CONSTANTS.FAIL,executionManager);
					executionManager.testPassed = false;
					executionManager.stopScript = true;
				}
			}
			takeScreenShot("Login into online portal successful", executionManager);
			executionManager.testReporting.updateActualResults("Login Successful",CONSTANTS.PASS,executionManager);
		}
		catch(Throwable t){
			takeScreenShot("Login into online portal failed", executionManager);
			executionManager.testReporting.updateActualResults("Login into onlnie Portal failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}	
	public boolean InitiateiMove(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Initiate iMove from Online Portal", "iMove should be initiated from online portal",executionManager);
		try{
			//Method Arguments : ChangeEmail
			if(testData.get("Brand").toLowerCase().contains("ed")){
				this.mijnHtmlDocument = mijnEDHtmlDocument();
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Overzicht", this.mijnHtmlDocument);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Producten bekijken of wijzigen", this.mijnHtmlDocument);
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Verhuizing doorgeven of wijzigen", this.mijnHtmlDocument)){
					executionManager.takeScreenShot("Existing Products", executionManager);
				}
				CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Verhuizing doorgeven of wijzigen", this.mijnHtmlDocument);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Verhuizing doorgeven of wijzigen", this.mijnHtmlDocument);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.LABEL", PropertyType.Text, "Ik verhuis en neem de energie van energiedirect.nl mee.", this.mijnHtmlDocument);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, "Volgende", this.mijnHtmlDocument);
				Thread.sleep(2000);
				executionManager.takeScreenShot("Initiate iMove", executionManager);
				if(executionManager.methodArguments.toLowerCase().contains("changeemail")){
					if(CheckEmailFormatInIMove(testData, executionManager)){
						executionManager.testReporting.updateActualResults("Email address of the user changed", CONSTANTS.PASS, executionManager);
					}
					else{
						executionManager.testReporting.updateActualResults("Email address of the user not changed", CONSTANTS.FAIL, executionManager);
					}
				}
				CF_Parent.nonMappableObject = true;
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.TextNode", PropertyType.Text, "Vul de datum in waarop je de oude sleutel inlevert", this.mijnHtmlDocument);
				//Change Date Logic
				if(executionManager.methodArguments.toLowerCase().contains("updateaddress")){
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.TextNode", PropertyType.Text, "Vul je nieuwe adres in", this.mijnHtmlDocument);

				}
			}
			else{
				this.mijnHtmlDocument = mijnEssentHtmlDocument();
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Overzicht", this.mijnHtmlDocument);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Text, "Verhuizing doorgeven of wijzigen", this.mijnHtmlDocument);
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.INPUT.radio", PropertyType.Name, "supplyOnNewAddress", this.mijnHtmlDocument)){
					executionManager.takeScreenShot("Supply on New Address", executionManager);
				}
				CF_Parent.SelectRadioButton(Platform.WEB, PropertyType.Class, "Html.INPUT.radio", PropertyType.Name, "supplyOnNewAddress", this.mijnHtmlDocument);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, "Volgende", this.mijnHtmlDocument);
				Thread.sleep(2000);
				executionManager.takeScreenShot("Initiate iMove", executionManager);
				if(executionManager.methodArguments.toLowerCase().contains("changeemail")){
					if(CheckEmailFormatInIMove(testData, executionManager)){
						executionManager.testReporting.updateActualResults("Email address of the user changed", CONSTANTS.PASS, executionManager);
					}
					else{
						executionManager.testReporting.updateActualResults("Email address of the user not changed", CONSTANTS.FAIL, executionManager);
					}
				}
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.TextNode", PropertyType.Text, "Vul de datum in waarop u de oude sleutel inlevert", this.mijnHtmlDocument);
				//Change Date Logic
				if(executionManager.methodArguments.toLowerCase().contains("updateaddress")){
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.TextNode", PropertyType.Text, "Vul uw nieuwe adres in", this.mijnHtmlDocument);

				}
			}
			
		}
		catch(Throwable t){
			executionManager.takeScreenShot("iMove from online portal failed", executionManager);
			executionManager.testReporting.updateActualResults("iMove initiation from online portal failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean CheckEmailFormatInIMove(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		try{
			if(testData.get("Brand").toLowerCase().contains("ed")){
				this.mijnHtmlDocument = mijnEDHtmlDocument();
			}
			else{
				this.mijnHtmlDocument = mijnEssentHtmlDocument();
			}
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.ClassName, "changeLink", this.mijnHtmlDocument);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, "Wijzigen", this.mijnHtmlDocument);
			CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "email", this.mijnHtmlDocument);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "emailConfirmation", this.mijnHtmlDocument);
			executionManager.takeScreenShot("Empty Email Address Error", executionManager);
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "email", "abc", this.mijnHtmlDocument);
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "emailConfirmation", "def", this.mijnHtmlDocument);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "email", this.mijnHtmlDocument);
			sleep(1);
			executionManager.takeScreenShot("Invalid Email address errror", executionManager);
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "email", CONSTANTS.userMailAddress, this.mijnHtmlDocument);
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "emailConfirmation", "abc123test@essent.nl", this.mijnHtmlDocument);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "email", this.mijnHtmlDocument);
			sleep(1);
			executionManager.takeScreenShot("Confirmation Email not matching error", executionManager);
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "emailConfirmation", CONSTANTS.userMailAddress, this.mijnHtmlDocument);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, "email", this.mijnHtmlDocument);
			sleep(1);
			executionManager.takeScreenShot("New Email address of the user", executionManager);
			if(testData.get("Brand").toLowerCase().contains("ed")){
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, "Bevestig je e-mailadres", this.mijnHtmlDocument);
			}
			else{
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, "Bevestig uw e-mailadres", this.mijnHtmlDocument);
			}
			CF_Parent.iLoopCount = 4;
			if(!CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, "Sluiten", this.mijnHtmlDocument)){
				return false;
			}
			else{
				executionManager.takeScreenShot("Email address of the user changed", executionManager);
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.Text, "Sluiten", this.mijnHtmlDocument);
			}
		}
		catch(Throwable t){
			executionManager.takeScreenShot("Change Email failed", executionManager);
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.ClassName, "close", this.mijnHtmlDocument);
			return false;
		}
		return true;
	}
}
