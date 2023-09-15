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

import com.spree.pages.Home;
import com.spree.pages.Order;
import com.spree.pages.ShoppingCart;
public class HomeTest {
	WebDriver driver;
	Home home;
	

	@BeforeTest
	public void precond() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		ChromeOptions opt = new ChromeOptions();
//		opt.setHeadless(true);
		driver = new ChromeDriver(opt);
		home = new Home(driver);
		
		driver.manage().window().maximize();
		driver.get("http://demo.spreecommerce.org/");
	}
	@Test
	public void func() throws InterruptedException {
		assertTrue(home.switchLanguage("fr"));
		assertTrue(home.switchLanguage("de"));
		assertTrue(home.switchLanguage("en"));
		assertTrue(home.switchCurrency("CAD"));
		
		assertTrue(home.verifyUrl());
		assertTrue(home.clickWomen());
		assertTrue(home.clickWomenCat("Skirts"));
		assertTrue(home.clickWomenCat("Dresses"));
		assertTrue(home.clickWomenCat("Sweaters"));
		assertTrue(home.clickWomenCat("Tops and T-Shirts"));
		assertTrue(home.clickWomenCat("Jackets and Coats"));
		assertTrue(home.clickMen());
		assertTrue(home.clickMenCat("Shirts"));
		assertTrue(home.clickMenCat("T-Shirts"));
		assertTrue(home.clickMenCat("Sweaters"));
		assertTrue(home.clickMenCat("Jackets and Coats"));
		assertTrue(home.clickSportswear());
		assertTrue(home.clickSportswearCat("Tops"));
		assertTrue(home.clickSportswearCat("Sweatshirts"));
		assertTrue(home.clickSportswearCat("Pants"));
		assertTrue(home.search("a b c"));
		assertTrue(home.login());
		assertTrue(home.signup());
		assertTrue(home.clickLogo());
	}
}
