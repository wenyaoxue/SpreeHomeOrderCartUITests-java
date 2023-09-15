package com.spree.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.spree.pages.ShoppingCart;
public class ShoppingCartTest {
	WebDriver driver;
	ShoppingCart cart;
	
	@BeforeTest
	public void precond() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		ChromeOptions opt = new ChromeOptions();
//		opt.setHeadless(true);
		driver = new ChromeDriver(opt);
		cart = new ShoppingCart(driver);
		
		driver.manage().window().maximize();
		driver.get("http://demo.spreecommerce.org");
		driver.findElement(By.id("account-button")).click();
		driver.findElement(By.linkText("LOGIN")).click();
		//log in
		Thread.sleep(2000);
		driver.findElement(By.name("spree_user[email]")).sendKeys("c@s.com");
		driver.findElement(By.name("spree_user[password]")).sendKeys("123456");
		driver.findElement(By.name("commit")).click();
		driver.get("http://demo.spreecommerce.org/cart");
		

		//ALSO: (easier to do manually bc it kinda takes a long time to clear the cart and add items 
		//make sure an XS GREY Sports Bra Low Support  + 1 other item is in the cart
	}
	
	@Test
	public void func() throws InterruptedException {		
		assertTrue(cart.verifyUrl());
		assertTrue(cart.verifyBreadcrumb());
		assertTrue(cart.verifyTitle());

		assertTrue(cart.verifyCosts());
		assertTrue(cart.incQ("Sports Bra Low Support"));
		Thread.sleep(1000);
		assertTrue(cart.verifyCosts());
		assertTrue(cart.decQ("Sports Bra Low Support"));
		Thread.sleep(1000);
		assertTrue(cart.verifyCosts());
//		assertTrue(cart.setQ("Sports Bra Low Support", "5")); //sendKeys not working
		Thread.sleep(1000);
		assertTrue(cart.verifyCosts());
		
		assertFalse(cart.deleteItem("lalala"));
		assertTrue(cart.verifyDetails("Sports Bra Low Support", 
				new HashMap<String, String>() {{put("SIZE", "XS"); put("COLOR","GREY");}}));
		assertTrue(cart.deleteItem("Sports Bra Low Support"));
		
		assertFalse(cart.addPromoCode("lala"));
		
		assertTrue(cart.clickCheckout());
		driver.navigate().back();
		assertTrue(cart.clickShop());
		
	}
}
