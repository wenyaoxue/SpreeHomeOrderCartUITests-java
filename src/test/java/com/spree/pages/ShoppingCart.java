package com.spree.pages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ShoppingCart {
	//http://demo.spreecommerce.org/cart
	private WebDriver driver;
	
	public ShoppingCart(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//main/div/nav/ol/li[2]")
	WebElement breadcrumb;

	@FindBy(xpath = "//h1")
	WebElement title;

	@FindBy(id = "order_coupon_code")
	WebElement couponfield;
	@FindBy(id = "shopping-cart-coupon-code-button")
	WebElement couponbtn;
	@FindBy(id = "coupon_status")
	WebElement couponmsg;

	
	@FindBy(xpath = "//a[text()='Continue shopping']")
	WebElement shoplink;
	@FindBy(id = "checkout-link")
	WebElement checkoutlink;

	
	@FindBy(xpath = "//h2")
	List<WebElement> productNames;
	@FindBy(xpath = "//h2//following-sibling::ul")
	List<WebElement> productDetails;
	@FindBy(xpath = "//div[@class='shopping-cart-item-price d-none d-lg-table-cell']")
	List<WebElement> productPrices;
	@FindBy(xpath = "//div[@class='shopping-cart-item-price d-none d-lg-table-cell']/following-sibling::*[1]//button[1]")
	List<WebElement> productDecBtns;
	@FindBy(xpath = "//div[@class='shopping-cart-item-price d-none d-lg-table-cell']/following-sibling::*[1]//button[2]")
	List<WebElement> productIncBtns;
	@FindBy(xpath = "//div[@class='shopping-cart-item-price d-none d-lg-table-cell']/following-sibling::*[1]//input")
	List<WebElement> productQuantities;
	@FindBy(xpath = "//div[@class='shopping-cart-item-price d-none d-lg-table-cell']/following-sibling::*[3]//a")
	List<WebElement> productDeleteBtns;
	@FindBy(xpath="//div[@data-hook='cart_item_total']")
	List<WebElement> productTotals;
	
	@FindBy(xpath = "//div[contains(@class, 'shopping-cart-total-amount col align-self-end')]")
	WebElement subtotal;
	
	public boolean verifyUrl() {return driver.getCurrentUrl().equals("http://demo.spreecommerce.org/cart");}	
	public boolean verifyBreadcrumb() {return breadcrumb.getText().trim().contains("Shopping Cart");}
	public boolean verifyTitle() {return title.getText().trim().toLowerCase().contains("your shopping cart");}	
	
	public boolean addPromoCode(String promocode) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order_coupon_code")));
		couponfield.sendKeys(promocode);
		couponbtn.click();
		Thread.sleep(1000);
		return (!couponmsg.isDisplayed());
	}
		
	public boolean clickShop() throws InterruptedException {
		shoplink.click(); Thread.sleep(2000); return driver.getCurrentUrl().equals("http://demo.spreecommerce.org/products");}
	public boolean clickCheckout() throws InterruptedException {
		checkoutlink.click(); Thread.sleep(2000); return driver.getCurrentUrl().equals("http://demo.spreecommerce.org/checkout");}
		
	public int findProduct(String productName) {
		for (int i = 0; i < productNames.size(); i++)
			if (productNames.get(i).getText().equals(productName))
				return i;
		//product not found
		return -1;
	}
	public boolean deleteItem(String productName) throws InterruptedException {
		int i = findProduct(productName);
		if (i == -1) return false; //product can't be found, can't be deleted
		productDeleteBtns.get(i).click(); 
		Thread.sleep(2000);
		return findProduct(productName) == -1; //if not -1, product was found, not deleted
	}
	public boolean verifyDetails(String productName, Map<String, String> details) {
		int i = findProduct(productName);
		if (i == -1) return false;
		//details eg "SIZE"->"S", "COLOR"->"GREY"
		for (String detailName: details.keySet()) {
			if (!(productDetails.get(i).getText().contains(detailName + ": " + details.get(detailName))))
				return false;
		}
		return true;
	}
	
	private double costToDouble(WebElement cost) { return Double.parseDouble(cost.getText().replace(",","").substring(1)); }
	public boolean verifyCosts() { //p*q = P; sum P = ST
		double subtot = 0;
		for (int i = 0; i < productNames.size(); i++) {
			if ((costToDouble(productPrices.get(i)) * Integer.parseInt(productQuantities.get(i).getAttribute("value"))) != costToDouble(productTotals.get(i)))
				return false;
			else
				subtot += costToDouble(productTotals.get(i));
		}
		return Math.round(subtot*100)/100. == costToDouble(subtotal);
	}
	
	public boolean incQ(String productName) throws InterruptedException {
		int i = findProduct(productName);
		if (i == -1) return false;
		int prvQ = Integer.parseInt(productQuantities.get(i).getAttribute("value"));
		productIncBtns.get(i).click();
		Thread.sleep(3000);
		return prvQ+1 == Integer.parseInt(productQuantities.get(i).getAttribute("value"));
	}
	public boolean decQ(String productName) throws InterruptedException {
		int i = findProduct(productName);
		if (i == -1) return false;
		int prvQ = Integer.parseInt(productQuantities.get(i).getAttribute("value"));
		productDecBtns.get(i).click();
		Thread.sleep(3000);
		return prvQ-1 == Integer.parseInt(productQuantities.get(i).getAttribute("value"));
	}
	public boolean setQ(String productName, String q) throws InterruptedException {
		int i = findProduct(productName);
		if (i == -1) return false;
		productQuantities.get(i).clear();
		productQuantities.get(i).sendKeys(q);
		Thread.sleep(3000);
		return Integer.parseInt(q) == Integer.parseInt(productQuantities.get(i).getAttribute("value"));
	}

}
