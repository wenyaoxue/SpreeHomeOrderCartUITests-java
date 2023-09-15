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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Order {
	//http://demo.spreecommerce.org/products/... something
	private WebDriver driver;
	
	public Order(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//main/div/nav/ol/li")
	List<WebElement> breadcrumbs;
	@FindBy(xpath = "//h1")
	WebElement title;
	@FindBy(xpath = "//span[@class='price selling']")
	WebElement price;
	
	@FindBy(xpath = "//span[@class='add-to-cart-form-general-availability-value']")
	WebElement inStock;
	
	@FindBy(xpath = "//ul/li/div/span")
	List<WebElement> detailNames;
	@FindBy(xpath = "//ul/li/div/span/following-sibling::div/ol")
	List<WebElement> detailValues;
	
	@FindBy(xpath="//button[contains(@class, 'quantity-select-decrease')]")
	WebElement decQBtn;
	@FindBy(xpath="//button[contains(@class, 'quantity-select-increase')]")
	WebElement incQBtn;
	@FindBy(name="quantity")
	WebElement quantity;
	
	@FindBy(id = "add-to-cart-button")
	WebElement addBtn;
	@FindBy(xpath = "//div[contains(@class,'product-added-modal-message')]")
	WebElement addMsg;
	@FindBy(xpath = "//a[contains(text(),'Checkout')]")
	WebElement checkoutLink;
	@FindBy(xpath = "//a[contains(text(),'View cart')]")
	WebElement cartLink;
	
	public boolean verifyUrl() {return driver.getCurrentUrl().contains("http://demo.spreecommerce.org/products/");}
	public boolean verifyBreadcrumb(String productName) {return breadcrumbs.get(breadcrumbs.size()-1).getText().trim().contains(productName);}
	public boolean verifyTitle(String productName) {return title.getText().trim().contains(productName);}

	private double costToDouble(WebElement cost) { return Double.parseDouble(cost.getText().replace(",","").substring(1)); }
	public boolean verifyPrice(double tgtP) {return tgtP == costToDouble(price);}

	public boolean verifyAvail() {return inStock.getText().equals("IN STOCK");}
	
	public boolean verifyColorAvail(String color) {
		for (int i = 0; i < detailNames.size(); i++) {
			if (detailNames.get(i).getText().equals("COLOR"))
				return (detailValues.get(i).getAttribute("innerHTML").contains("aria-label=\""+color+"\""));
		}
		return false;
	}
	public void clickColor(String color) {
		for (int i = 0; i < detailNames.size(); i++) {
			if (detailNames.get(i).getText().equals("COLOR")) {
				driver.findElement(By.xpath("//input[@aria-label='"+color+"']//parent::*")).click();
//				String value = driver.findElement(By.xpath("//input[@aria-label='"+color+"']")).getAttribute("value");
			}
		}
//		return true; // would have to use javascript to check which is selected (can't see an html difference)
	}
	public boolean verifySizeAvail(String size) {
		for (int i = 0; i < detailNames.size(); i++) {
			if (detailNames.get(i).getText().equals("SIZE"))
				return (detailValues.get(i).getText().contains(size));
		}
		return false;
	}
	public void clickSize(String size) {
		for (int i = 0; i < detailNames.size(); i++) {
			if (detailNames.get(i).getText().equals("SIZE")) {
				driver.findElement(By.xpath("//label[@aria-label='"+size+"']//parent::*")).click();
//				String value = driver.findElement(By.xpath("//input[@aria-label='"+color+"']")).getAttribute("value");
			}
		}
//		return true; // would have to use javascript to check which is selected (can't see an html difference)
	}

	public boolean verifyQuantity(int q) {return q == Integer.parseInt(quantity.getAttribute("value"));}
	public boolean incQ() {
		int prvQ = Integer.parseInt(quantity.getAttribute("value"));
		incQBtn.click();
		return verifyQuantity(prvQ+1);
	}
	public boolean decQ() {
		int prvQ = Integer.parseInt(quantity.getAttribute("value"));
		decQBtn.click();
		return verifyQuantity(prvQ-1);
	}
	

	public boolean addToCart() throws InterruptedException {
		addBtn.click();
		Thread.sleep(2000);
		return addMsg.getText().contains("Added to cart successfully!")
				&& checkoutLink.isDisplayed() && cartLink.isDisplayed();
	}
	public boolean clickCheckout() {
		checkoutLink.click();
		return driver.getCurrentUrl().equals("http://demo.spreecommerce.org/checkout/registration");
	}
	public boolean clickCart() {
		cartLink.click();
		return driver.getCurrentUrl().equals("http://demo.spreecommerce.org/cart");
	}
}
