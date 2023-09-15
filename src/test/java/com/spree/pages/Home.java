package com.spree.pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Home {
	//http://demo.spreecommerce.org/products/... something
	private WebDriver driver;
	Actions act;
	
	public Home(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath="//div[@id='spree-header']//figure[1]/a/img")
	WebElement logo;
	@FindBy(xpath="//ul[@class='nav h-100 main-nav-bar']/li[1]/a")
	WebElement women;
	@FindBy(xpath="//ul[@class='nav h-100 main-nav-bar']/li[2]/a")
	WebElement men;
	@FindBy(xpath="//ul[@class='nav h-100 main-nav-bar']/li[3]/a")
	WebElement sportswear;
	@FindBy(xpath="//div[@class='navbar-right-search-menu']")
	WebElement search;
	@FindBy(id="search-dropdown")
	WebElement searchbar;
	@FindBy(xpath="//div[contains(@class,'dropdown navbar-right-dropdown')]")
	WebElement profile;
	@FindBy(id="link-to-cart")
	WebElement cart;
	@FindBy(xpath="//*[@id='link-to-cart']/following-sibling::*")
	WebElement locSettings;
	

	private String getUrlStem() {return driver.getCurrentUrl().split("\\?")[0];}

	public boolean verifyUrl() {return getUrlStem().equals("http://demo.spreecommerce.org/");}
	
	public boolean clickLogo() throws InterruptedException {
		logo.click();
		Thread.sleep(1000);
		return getUrlStem().equals("http://demo.spreecommerce.org/");
	}
	

	public boolean clickWomen() {women.click(); return getUrlStem().equals("http://demo.spreecommerce.org/t/categories/women"); }
	public boolean clickWomenCat(String cat) {
		act.moveToElement(women).build().perform();
		driver.findElement(
				By.xpath("//div[@aria-label='Desktop navigation']/ul/li[1]//ul/li/a[text()='"+cat+"']"))
		.click();
		return getUrlStem().equals("http://demo.spreecommerce.org/t/categories/women/"+cat.toLowerCase().replace(' ', '-'));
	}

	public boolean clickMen() { men.click(); return getUrlStem().equals("http://demo.spreecommerce.org/t/categories/men");}
	public boolean clickMenCat(String cat) {
		act.moveToElement(men).build().perform();
		driver.findElement(
				By.xpath("//div[@aria-label='Desktop navigation']/ul/li[2]//ul/li/a[text()='"+cat+"']"))
		.click();
		return getUrlStem().equals("http://demo.spreecommerce.org/t/categories/men/"+cat.toLowerCase().replace(' ', '-'));
	}

	public boolean clickSportswear() {sportswear.click();return getUrlStem().equals("http://demo.spreecommerce.org/t/categories/sportswear");}
	public boolean clickSportswearCat(String cat) {
		act.moveToElement(sportswear).build().perform();
		driver.findElement(
				By.xpath("//div[@aria-label='Desktop navigation']/ul/li[3]//ul/li/a[text()='"+cat+"']"))
		.click();
		return getUrlStem().equals("http://demo.spreecommerce.org/t/categories/sportswear/"+cat.toLowerCase().replace(' ', '-'));
	}
	

	public boolean search(String searchterm) throws InterruptedException {
		search.click();
		Thread.sleep(1000);
		if (!searchbar.getAttribute("class").contains("show")) return false;
		driver.findElement(By.id("keywords")).sendKeys(searchterm);
		driver.findElement(By.xpath("//*[@id='keywords']/following-sibling::*")).click();
		Thread.sleep(3000);
		return driver.getCurrentUrl().startsWith("http://demo.spreecommerce.org/products?keywords="+searchterm.replace(" ", "+"));
	}

	public boolean login() {
		profile.click();
		driver.findElement(By.linkText("LOGIN")).click();
		return getUrlStem().equals("http://demo.spreecommerce.org/login");
	}
	public boolean signup() {
		profile.click();
		driver.findElement(By.linkText("SIGN UP")).click();
		return getUrlStem().equals("http://demo.spreecommerce.org/signup");
	}

	public boolean switchLanguage(String lang) throws InterruptedException {
		locSettings.click();
		Select langPick = new Select(driver.findElement(By.id("switch_to_locale")));
		langPick.selectByValue(lang);
		Thread.sleep(2000);
		if (lang.equals("de"))
			return women.getText().equals("DAMEN");
		if (lang.equals("fr"))
			return women.getText().equals("FEMMES");
		return women.getText().equals("WOMEN");
	}
	public boolean switchCurrency(String curr) {
		locSettings.click();
		Select currPick = new Select(driver.findElement(By.id("switch_to_currency")));
		currPick.selectByValue(curr);
		return true;
	}
}
