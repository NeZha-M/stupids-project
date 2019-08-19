package Classes;

import java.util.LinkedHashMap;

import org.openqa.selenium.By;

import utilityFunctions.CONSTANTS;
import utilityFunctions.TestDataAutomation;
import ComponentFns.CF_Parent;
import ComponentFns.ExecutionManager;
import ComponentFns.CF_Parent.Platform;
import ComponentFns.CF_Parent.PropertyType;

import com.rational.test.ft.object.interfaces.BrowserTestObject;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.RootTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.value.RegularExpression;
import com.thoughtworks.selenium.webdriven.commands.Close;


public class MPR extends ExecutionManager
{
	public boolean Login(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Login into MPR online portal", "User should be able to login into MPR portal",executionManager);
		try{	
			if(executionManager.methodArguments.toLowerCase().equals("chrome")){
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					startBrowser("Chrome", CONSTANTS.mprAccURL);
				}
				else{
					startBrowser("Chrome", CONSTANTS.mprTestURL);
				}
			}
			else{
				if(executionManager.methodArguments.toLowerCase().contains(CONSTANTS.acceptanceEnvironment.toLowerCase())){
					startBrowser(CONSTANTS.mprAccURL);
				}
				else{
					startBrowser(CONSTANTS.mprTestURL);
				}
			}
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Name, new RegularExpression(".*LoginForm.*UserName.*", false), CONSTANTS.mprUserName, mprHtmlDocument());
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.password", PropertyType.Name, new RegularExpression(".*LoginForm.*Password.*", false), CONSTANTS.mprPassword, mprHtmlDocument());
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Name, new RegularExpression(".*LoginForm.*ButtonLogin.*", false), mprHtmlDocument());
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.ContentText, "Cockpit", mprHtmlDocument())){
				takeScreenShot("MPR Login successful", executionManager);
				executionManager.testReporting.updateActualResults("User successfully logged into MPR",CONSTANTS.PASS,executionManager);
			}
			else{
				takeScreenShot("User login into MPR failed", executionManager);
				executionManager.testReporting.updateActualResults("User not logged in into MPR",CONSTANTS.PASS,executionManager);
				executionManager.testPassed = false;
				executionManager.stopScript = true;
			}
		}
		catch(Throwable t){
			takeScreenShot("Login into mpr portal failed", executionManager);
			executionManager.testReporting.updateActualResults("Login into MPR Portal failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true;
	}
	public boolean SearchBusinessPartner(LinkedHashMap<String, String> testData,ExecutionManager executionManager) throws Throwable{
		executionManager.testReporting.updateStepInfo("Search Given Business Partner in MPR", "Business Partner Search should be successful",executionManager);
		try{   
			String businessPartnerType = "";
			TestDataAutomation dataAutomation = null;
			if(testData.get("Brand").toLowerCase().contains(CONSTANTS.energieDirect.toLowerCase())){
				CF_Parent.iLoopCount = 4;
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*SwitchToED.*", false), mprHtmlDocument())){
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*SwitchToED.*", false), mprHtmlDocument());
				}
			}
			else{
				if(executionManager.methodArguments.toLowerCase().contains("acc")){
					dataAutomation = new TestDataAutomation("Acceptance", false, "essent");
				}
				else{
					dataAutomation = new TestDataAutomation("Test", false, "essent");
				}
			}
			if(!testData.get("Brand").toLowerCase().contains(CONSTANTS.energieDirect.toLowerCase())){
				businessPartnerType = dataAutomation.GetBusinessPartnerType(testData.get("bpNumber"));
				if(businessPartnerType.equals("HH") || businessPartnerType.equals("SOHO")){
					CF_Parent.iLoopCount = 4;
					if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*SwitchToHH.*", false), mprHtmlDocument())){
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*SwitchToHH.*", false), mprHtmlDocument());
					}
				}
				else if(businessPartnerType.equals("SME")||businessPartnerType.equals("RENT")||businessPartnerType.equals("SPEC")){
					{
						CF_Parent.iLoopCount = 4;
						if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*SwitchToSME.*", false), mprHtmlDocument())){
							CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*SwitchToSME.*", false), mprHtmlDocument());
						}
					}
				}
			}
			CF_Parent.EnterText(Platform.WEB, PropertyType.Class, "Html.INPUT.text", PropertyType.Id,new RegularExpression( ".*SearchAccount.*", false), testData.get("bpNumber"), mprHtmlDocument());
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*SearchFormPanel.*Search.*", false), mprHtmlDocument());
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*SearchResultsPanel.*DataGridCustomers", false), mprHtmlDocument()))
			{
				executionManager.takeScreenShot("Business Partner Search is successful", executionManager);
				executionManager.testReporting.updateActualResults("Business Partner Search is successful",CONSTANTS.PASS,executionManager);
				return true;
			}
			else{
				executionManager.takeScreenShot("Business Partner Search fail", executionManager);
				executionManager.testReporting.updateActualResults("Business Partner Search fail",CONSTANTS.FAIL,executionManager);
				executionManager.stopScript = true;
				executionManager.testPassed = false;
				return false;
			}
		}
		catch(Throwable t){
			takeScreenShot("Business Partner Search failed", executionManager);
			executionManager.testReporting.updateActualResults("Business Partner Search failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
	}
	public boolean NavigateInMPR(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Click on  "+testData.get("LinkName")+" in MPR", "Given link should be navigated to the required page",executionManager);
		try{
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(testData.get("RegularExpression"), false), mprHtmlDocument());
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, testData.get("PageName"), mprHtmlDocument())){
				takeScreenShot("Navigation successful", executionManager);
				executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
				browser_htmlBrowser().inputKeys("{ExtPgDn}");
				CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.iLoopCount = 30;
				return true;
			}
			else{
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, testData.get("PageName"), mprHtmlDocument())){
					takeScreenShot("Navigation successful", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
					browser_htmlBrowser().inputKeys("{ExtPgDn}");
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return true;
				}
				else{
					takeScreenShot("Navigation failed", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Failed",CONSTANTS.FAIL,executionManager);
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return false;
				}
			}
		}
		catch(Throwable t){
			takeScreenShot("Clicking on link "+testData.get("LinkName")+" Failed", executionManager);
			executionManager.testReporting.updateActualResults("Clicking on link "+testData.get("LinkName")+" Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			System.out.println(t.toString());
			return false;
		}
	}
	public boolean LookAndFeelMPR(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		try{
			String businessPartnerType = "";
			TestDataAutomation dataAutomation = null;

			if(executionManager.methodArguments.toLowerCase().contains("acc")){
				System.out.println(""+executionManager.methodArguments.toLowerCase().contains("acc"));
				dataAutomation = new TestDataAutomation("Acceptance", false, testData.get("Brand"));
			}
			else{
				dataAutomation = new TestDataAutomation("Test", false, testData.get("Brand"));
			}


			businessPartnerType = dataAutomation.GetBusinessPartnerType(testData.get("bpNumber"));
			System.out.println(""+businessPartnerType);

			clickOnSearchResultPanelAddress(testData, executionManager);

			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*_ContentBodyPlaceHolder.*_ActionLinkEditProduct.*", false), mprHtmlDocument())){
				testData.put("LinkName","Product Link");
				testData.put("RegularExpression", ".*_ContentBodyPlaceHolder.*_ActionLinkEditProduct.*");
				testData.put("PageName", "Aanpassen product");
				this.NavigateInMPR(testData, executionManager);
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkAddService.*", false), mprHtmlDocument())){
				testData.put("LinkName","Nieuw Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkAddService.*");
				testData.put("PageName", "Product toevoegen");
				this.NavigateInMPR(testData, executionManager);
			}
			
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkPricePlanDetails.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Tarief Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkPricePlanDetails.*");
				testData.put("PageName", "Tarief");
				this.NavigateInMPR(testData, executionManager);
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkAddInteraction.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Klantcontact Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkAddInteraction.*");
				testData.put("PageName", "Klantcontact toevoegen");
				this.NavigateInMPR(testData, executionManager);
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkKernArchive.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Kerarchief Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkKernArchive.*");
				testData.put("PageName", "Overzicht KERN Archief");
				this.NavigateInMPR(testData, executionManager);	
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Campaigns");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*");
				testData.put("PageName", "Campagne lijst");
				this.ClickCampaigns(testData, executionManager,"klant");
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Campaigns");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*");
				testData.put("PageName", "Campagne lijst");
				this.ClickCampaigns(testData, executionManager,"connection");
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkEndContract.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Beëindiging Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkEndContract.*");
				testData.put("PageName", "Beëindiging");
				this.NavigateInMPR(testData, executionManager);
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkUpdateBudgetBillPlan.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Termijnbedrag Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkUpdateBudgetBillPlan.*");
				testData.put("PageName", "Aanpassen termijnbedrag");
				this.NavigateInMPR(testData, executionManager);
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkAddMeterReadings.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Meterstanden Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkAddMeterReadings.*");
				testData.put("PageName", "Meterstanden en verbruiken");
				this.NavigateInMPR(testData, executionManager);
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkUsageManager.*", false), mprHtmlDocument())){
				testData.put("LinkName", "VerbruiksMng Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkUsageManager.*");
				testData.put("PageName", "Verbruiksmanager");
				this.NavigateInMPR(testData, executionManager);
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkFutureAndHistoricContracts.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Overzicht servicepakket E");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkFutureAndHistoricContracts.*");
				testData.put("PageName", "Contract servicepakket E");
				this.ClickOverzicht(testData, executionManager,"electric");
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkFutureAndHistoricContracts.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Overzicht servicepakket G");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkFutureAndHistoricContracts.*");
				testData.put("PageName", "Contract servicepakket G");
				this.ClickOverzicht(testData, executionManager,"gas");	
			}

			if(businessPartnerType.equals("HH") || businessPartnerType.equals("SOHO")){
				CF_Parent.iLoopCount = 4;
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.ClassName, "mpr-hyperlink HyperLink PageLink", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkMove.*", false), mprHtmlDocument())){
					testData.put("LinkName", "Verhuizen");
					testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkMove.*");
					testData.put("PageName", "Verhuizen");
					this.NavigateInMPR(testData, executionManager);
				}

			}
			if(businessPartnerType.equals("SME")||businessPartnerType.equals("RENT")||businessPartnerType.equals("SPEC")){
				CF_Parent.iLoopCount = 4;
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.ClassName, "mpr-hyperlink HyperLink PageLink", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkMove.*", false), mprHtmlDocument())){
					testData.put("LinkName", "Verhuizen");
					testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkMove.*");
					testData.put("PageName", "Verhuisregistratie");
					this.NavigateInMPR(testData, executionManager);
				}

			}
			browser_htmlBrowser().inputKeys("{PGUP}");
			//second page navigation 
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*DataGridCustomers.*CustomerName.*", false), mprHtmlDocument())){
				CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*DataGridCustomers.*CustomerName.*", false), mprHtmlDocument());
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*DataGridCustomers.*CustomerName.*", false),mprHtmlDocument());
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, "Overzicht klant"+" "+ testData.get("bpNumber"), mprHtmlDocument()))	
					takeScreenShot("Navigation to customer level page successful", executionManager);

			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkNewConnection.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Nieuweaansl Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkNewConnection.*");
				testData.put("PageName", "Voeg een nieuwe aansluiting toe");
				this.NavigateInMPR(testData, executionManager);
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkNewCR.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Nieuwe CR");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkNewCR.*");
				testData.put("PageName", "Voeg een nieuwe contractrekening toe");
				this.NavigateInMPR(testData, executionManager);
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkAddInteraction.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Klantcontact Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkAddInteraction.*");
				testData.put("PageName", "Klantcontact toevoegen");
				this.NavigateInMPR(testData, executionManager);
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkKernArchive.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Kerarchief Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkKernArchive.*");
				testData.put("PageName", "Overzicht KERN Archief");
				this.NavigateInMPR(testData, executionManager);
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Campaigns");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*");
				testData.put("PageName", "Campagne lijst");
				this.ClickCampaigns(testData, executionManager,"klant");
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Campaigns");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*CampaignGrid_LinkCampaignList.*");
				testData.put("PageName", "Campagne lijst");
				this.ClickCampaigns(testData, executionManager,"connection");

			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.ContentText,"Betalingen", mprHtmlDocument())){
				testData.put("LinkName", "Betalingen");
				testData.put("PropertyValue", "Betalingen");
				testData.put("PageName", "Betalingen");
				this.clickBetalingsgegevensLinks(testData, executionManager,"betalingsgegevenslinks");
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.ContentText,"Facturen", mprHtmlDocument())){
				testData.put("LinkName", "Facturen");
				testData.put("PropertyValue", "Facturen");
				testData.put("PageName", "Facturen");
				this.clickBetalingsgegevensLinks(testData, executionManager,"betalingsgegevenslinks" );

			}
			if(businessPartnerType.equals("HH") || businessPartnerType.equals("SOHO")){
				CF_Parent.iLoopCount = 4;
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkUpdateCustomer.*", false), mprHtmlDocument())){
					testData.put("LinkName", "Klant Aanpassen  Link");
					testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkUpdateCustomer.*");
					testData.put("PageName", "Aanpassen Klantgegevens");
					this.ClickCampaigns(testData, executionManager, "klant");
				}
			}
			if(businessPartnerType.equals("SME")||businessPartnerType.equals("RENT")||businessPartnerType.equals("SPEC")){
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkUpdateCustomer.*", false), mprHtmlDocument())){
					testData.put("LinkName", "Klant Aanpassen  Link");
					testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkUpdateCustomer.*");
					testData.put("PageName", "Aanpassen contractgegevens");
					this.ClickCampaigns(testData, executionManager, "klant");
				}
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkUpdateCustomer.*", false), mprHtmlDocument())){
					testData.put("LinkName", "Klant Aanpassen  Link");
					testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkUpdateCustomer.*");
					testData.put("PageName", "Aanpassen Klantgegevens");
					this.ClickCampaigns(testData, executionManager, "connection");
				}
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Title, "Aanpassen contract gegevens", mprHtmlDocument())){
				testData.put("LinkName", "Aanpassen contract gegevens Link");
				testData.put("PropertyValue", "Aanpassen contract gegevens");
				testData.put("PageName", "Aanpassen contractgegevens");
				this.clickBetalingsgegevensLinks(testData, executionManager, "aanpassensmelinks");	
			}

			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkEditCorrespondenceAddress.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Aanpassen Correspondence Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkEditCorrespondenceAddress.*");
				testData.put("PageName", "Aanpassen correspondentieadres");
				this.NavigateInMPR(testData, executionManager);
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*_LinkPageLink.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Aanpassen Betalingsgegevens  Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*_LinkPageLink.*");
				testData.put("PageName", "Aanpassen betalingsgegevens");
				this.NavigateInMPR(testData, executionManager);
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkChangePreferences.*", false), mprHtmlDocument())){
				sleep(3);
				testData.put("LinkName", "Digitale Aanpassen Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkChangePreferences.*");
				testData.put("PageName", "Aanpassen Klantgegevens");
				this.NavigateInMPR(testData, executionManager);	
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ActionLinkAddressHistory.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Overzicht Adres Link");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*ActionLinkAddressHistory.*");
				testData.put("PageName", "Adres overzicht");
				this.NavigateInMPR(testData, executionManager);	
			}
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkTextBundle.*", false), mprHtmlDocument())){
				testData.put("LinkName", "Teskstbundel");
				testData.put("RegularExpression", ".*ContentBodyPlaceHolder.*LinkTextBundle.*");
				testData.put("PageName", "Taalkeuze");
				this.ClickTekstbundelLink(testData, executionManager);
			}	
		}
		catch(Throwable t){
			takeScreenShot("LookAndFeelMPR navigation failed", executionManager);
			executionManager.testReporting.updateActualResults("LookAndFeelMPR navigation failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
		return true ;
	}
	public boolean clickOnSearchResultPanelAddress(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Navigate to page having all navigation link in MPR", "Navigation link page Search should be successful",executionManager);
		try{
			TestObject[] searchPanel=mprHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.IMG",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*ContentBodyPlaceHolder.*SearchResultsPanel.*DataGridCustomers.*Icon.*", false)),false);
			if(searchPanel== null || searchPanel.length==0)
				return false;
			else 
			{
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.IMG", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*SearchResultsPanel.*DataGridCustomers.*Icon.*", false),mprHtmlDocument());
				TestObject[] searchAddressPanel=mprHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.TABLE",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(".*ContentBodyPlaceHolder.*SearchResultsPanel.*DataGridCustomers.*ChildTemplate.*DataGridConnections.*", false)),false);
				if(searchAddressPanel== null || searchAddressPanel.length==0)
					return false;
				else
				{
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.IMG", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*SearchResultsPanel.*DataGridCustomers.*ChildTemplate.*DataGridConnections.*", false), mprHtmlDocument());
					if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, "Overzicht aansluiting"+" "+ testData.get("bpNumber"), mprHtmlDocument())){
						executionManager.takeScreenShot("Navigation link page in MPR ", executionManager);
						executionManager.testReporting.updateActualResults("Navigation link page in MPR Search is successful",CONSTANTS.PASS,executionManager);
						return true;
					}
					else 
						return false ;

				}
			}
		}
		catch(Throwable t){
			takeScreenShot("Navigation link page failed ", executionManager);
			executionManager.testReporting.updateActualResults("Navigation link page failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			executionManager.stopScript = true;
			System.out.println(t.toString());
			return false;
		}
	}
	public boolean ClickCampaigns(LinkedHashMap<String, String> testData,ExecutionManager executionManager,String campaginType){
		executionManager.testReporting.updateStepInfo("Click on Link "+testData.get("LinkName")+" in MPR", "Given link should be navigated to the required page",executionManager);
		try{
			TestObject[] campaignLinks = mprHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(testData.get("RegularExpression"), false)), false);
			if(campaignLinks == null || campaignLinks.length==0){
				return false;
			}
			else{
				if(campaginType.toLowerCase().equals("klant")){
					((GuiTestObject)campaignLinks[0]).click();
				}
				else if(campaginType.toLowerCase().equals("connection")){
					((GuiTestObject)campaignLinks[1]).click();
				}
				else{
					((GuiTestObject)campaignLinks[0]).click();
					campaignLinks =mprHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(testData.get("RegularExpression"), false)), false);
					((GuiTestObject)campaignLinks[1]).click();
				}
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, testData.get("PageName"), mprHtmlDocument())){
				takeScreenShot("Navigation successful", executionManager);
				executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
				browser_htmlBrowser().inputKeys("{ExtPgDn}");
				CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.iLoopCount = 30;
				return true;
			}
			else{
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, testData.get("PageName"), mprHtmlDocument())){
					takeScreenShot("Navigation successful", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
					browser_htmlBrowser().inputKeys("{ExtPgDn}");
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return true;
				}
				else{
					takeScreenShot("Navigation failed", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Failed",CONSTANTS.FAIL,executionManager);
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return false;
				}
			}
		}
		catch(Throwable t){
			takeScreenShot("Clicking on link "+testData.get("LinkName")+" Failed", executionManager);
			executionManager.testReporting.updateActualResults("Clicking on link "+testData.get("LinkName")+" Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			System.out.println(t.toString());
			return false;
		}
	}
	public boolean clickBetalingsgegevensLinks(LinkedHashMap<String, String> testData,ExecutionManager executionManager,String LinkType){
		executionManager.testReporting.updateStepInfo("Click on  "+testData.get("LinkName")+" in MPR", "Given link should be navigated to the required page",executionManager);
		try{

			if(LinkType.toLowerCase().equals("betalingsgegevenslinks")){
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.ContentText, testData.get("PropertyValue"), mprHtmlDocument());
			}
			else if(LinkType.toLowerCase().equals("aanpassensmelinks"))
			{
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Title, testData.get("PropertyValue"), mprHtmlDocument());
			}	

			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, testData.get("PageName"), mprHtmlDocument())){
				takeScreenShot("Navigation successful", executionManager);
				executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
				browser_htmlBrowser().inputKeys("{ExtPgDn}");
				CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.iLoopCount = 30;
				return true;
			}
			else{

				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, testData.get("PageName"), mprHtmlDocument())){
					takeScreenShot("Navigation successful", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
					browser_htmlBrowser().inputKeys("{ExtPgDn}");
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return true;
				}

				else{

					takeScreenShot("Navigation failed", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Failed",CONSTANTS.FAIL,executionManager);
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return false;

				}
			}

		}
		catch(Throwable t){
			takeScreenShot("Clicking on link "+testData.get("LinkName")+" Failed", executionManager);
			executionManager.testReporting.updateActualResults("Clicking on link "+testData.get("LinkName")+" Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			System.out.println(t.toString());
			return false;
		}

	}
	public boolean ClickTekstbundelLink(LinkedHashMap<String, String> testData,ExecutionManager executionManager){
		executionManager.testReporting.updateStepInfo("Click on  "+testData.get("LinkName")+" in MPR", "Given link should be navigated to the required page",executionManager);
		try{
			CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.A", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*LinkTextBundle.*", false), mprHtmlDocument());
			RootTestObject rootObj = RootTestObject.getRootTestObject();
			TestObject[] htmlDocuments = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.Title,Platform.WEB),new RegularExpression(".*Run Model.*", false)));
			boolean tesktbundelDocumentFound = false;
			for(int htmlDocTrail = 0;htmlDocTrail<10;htmlDocTrail++){
				if(htmlDocuments.length==0){
					htmlDocuments = rootObj.find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.HtmlDocument",CF_Parent.getPropertyType(PropertyType.Title,Platform.WEB),new RegularExpression(".*Run Model.*", false)));
				}
				else{
					tesktbundelDocumentFound = true;
					break;
				}
			}
			if(!tesktbundelDocumentFound){
				executionManager.testReporting.updateActualResults("Tesktbundel Link  Failed to open the page ",CONSTANTS.FAIL,executionManager);
				executionManager.testPassed = false;
				return false;
			}
			else{
				CF_Parent.iLoopCount = 4;
				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.TABLE", PropertyType.ContentText,"Taalkeuze", htmlDocuments[0])){
					takeScreenShot("Navigation successful", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
					if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.ContentText,"Ok", htmlDocuments[0]))
					{
						CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.BUTTON", PropertyType.ContentText, "Ok", htmlDocuments[0]);
					}
					CF_Parent.iLoopCount = 30;
					browser_htmlBrowser(((GuiTestObject)htmlDocuments[0]),DEFAULT_FLAGS).activate();
					browser_htmlBrowser(((GuiTestObject)htmlDocuments[0]),DEFAULT_FLAGS).inputKeys("^{F4}");
					return true;
				}
				else{
					takeScreenShot("Navigation failed", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Failed",CONSTANTS.FAIL,executionManager);
					return false;
				}
			}
		}
		catch(Throwable t){
			takeScreenShot("Clicking on link "+testData.get("LinkName")+" Failed", executionManager);
			executionManager.testReporting.updateActualResults("Clicking on link "+testData.get("LinkName")+" Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			System.out.println(t.toString());
			return false;
		}
	}
	public boolean ClickOverzicht(LinkedHashMap<String, String> testData,ExecutionManager executionManager,String overzichtType){
		executionManager.testReporting.updateStepInfo("Click on Link "+testData.get("LinkName")+" in MPR", "Given link should be navigated to the required page",executionManager);
		try{
			TestObject[] OverzichtLinks = mprHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(testData.get("RegularExpression"), false)), false);
			if(OverzichtLinks == null || OverzichtLinks.length==0){
				return false;
			}
			else{
				if(overzichtType.toLowerCase().equals("electric")){
					((GuiTestObject)OverzichtLinks[0]).click();

				}
				else if(overzichtType.toLowerCase().equals("gas")){
					((GuiTestObject)OverzichtLinks[1]).click();
				}
				else{
					((GuiTestObject)OverzichtLinks[0]).click();
					OverzichtLinks =mprHtmlDocument().find(atDescendant(CF_Parent.getPropertyType(PropertyType.Class,Platform.WEB),"Html.A",CF_Parent.getPropertyType(PropertyType.Id,Platform.WEB),new RegularExpression(testData.get("RegularExpression"), false)), false);
					((GuiTestObject)OverzichtLinks[1]).click();
				}
			}
			CF_Parent.iLoopCount = 4;
			if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.ContentText, testData.get("PageName"), mprHtmlDocument())){

				takeScreenShot("Navigation successful", executionManager);
				executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
				CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
				CF_Parent.iLoopCount = 30;
				return true;
			}
			else{

				if(CF_Parent.ObjectExists(Platform.WEB, PropertyType.Class, "Html.SPAN", PropertyType.Text, testData.get("PageName"), mprHtmlDocument())){
					takeScreenShot("Navigation successful", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Successful",CONSTANTS.PASS,executionManager);
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return true;
				}
				else{
					takeScreenShot("Navigation failed", executionManager);
					executionManager.testReporting.updateActualResults("Navigation Failed",CONSTANTS.FAIL,executionManager);
					CF_Parent.HoverControl(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.ClickButton(Platform.WEB, PropertyType.Class, "Html.INPUT.button", PropertyType.Id, new RegularExpression(".*ContentBodyPlaceHolder.*ButtonOverview.*", false),mprHtmlDocument() );
					CF_Parent.iLoopCount = 30;
					return false;
				}
			}
		}
		catch(Throwable t){
			takeScreenShot("Clicking on link "+testData.get("LinkName")+" Failed", executionManager);
			executionManager.testReporting.updateActualResults("Clicking on link "+testData.get("LinkName")+" Failed",CONSTANTS.FAIL,executionManager);
			executionManager.testPassed = false;
			System.out.println(t.toString());
			return false;
		}
	}
}

