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

import com.spree.pages.Order;
import com.spree.pages.ShoppingCart;
public class OrderTest {
	WebDriver driver;
	Order order;

	@BeforeTest
	public void precond() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		ChromeOptions opt = new ChromeOptions();
//		opt.setHeadless(true);
		driver = new ChromeDriver(opt);
		order = new Order(driver);
		
		driver.manage().window().maximize();
		driver.get("http://demo.spreecommerce.org/products/sports-bra-low-support?taxon_id=4");		
	}
	@Test
	public void func() throws InterruptedException {
		assertTrue(order.verifyUrl());
		assertTrue(order.verifyBreadcrumb("Sports Bra Low Support"));
		assertTrue(order.verifyTitle("Sports Bra Low Support"));
		assertTrue(order.verifyPrice(63.99));
		assertTrue(order.verifyAvail());
		assertTrue(order.verifyColorAvail("grey"));
		assertTrue(order.verifyColorAvail("pink"));
		assertFalse(order.verifyColorAvail("blue"));
		order.clickColor("grey");
		assertTrue(order.verifySizeAvail("XS"));
		assertTrue(order.verifySizeAvail("S"));
		assertTrue(order.verifySizeAvail("M"));
		assertFalse(order.verifySizeAvail("L"));
		order.clickSize("M");
		
		assertTrue(order.incQ());
		assertTrue(order.decQ());
		
		assertTrue(order.addToCart());
		assertTrue(order.clickCheckout());
		driver.navigate().back();
		assertTrue(order.clickCart());
	}
}
