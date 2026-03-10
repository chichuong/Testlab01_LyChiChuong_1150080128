package com.example.bai7;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Feature("OrderProcessor - calculateTotal")
public class OrderProcessorTest {

    private OrderProcessor processor;

    @BeforeMethod
    public void setUp() {
        processor = new OrderProcessor();
    }

    // =====================================================================
    // BASIS PATH TEST CASES
    // =====================================================================

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Gio hang trong")
    @Story("Basis Path")
    @Severity(SeverityLevel.CRITICAL)
    @Description("BP1: D1=T (items==null) -> throw IllegalArgumentException")
    public void testBP1_itemsNull_throwsException() {
        processor.calculateTotal(null, null, "SILVER", "CARD");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Gio hang trong")
    @Story("Basis Path")
    @Severity(SeverityLevel.CRITICAL)
    @Description("BP1b: D1=T (items rong) -> throw IllegalArgumentException")
    public void testBP1b_itemsEmpty_throwsException() {
        processor.calculateTotal(new ArrayList<>(), null, "SILVER", "CARD");
    }

    @Test
    @Story("Basis Path")
    @Severity(SeverityLevel.NORMAL)
    @Description("BP2: D1=F, D2=F, D5=F, D6=F, D7=T, D8=T -> no coupon, SILVER, CARD, subtotal=100k => 130k")
    public void testBP2_noCoupon_noMember_shipOnline() {
        List<Item> items = Arrays.asList(new Item(100_000));
        double result = processor.calculateTotal(items, null, "SILVER", "CARD");
        Assert.assertEquals(result, 130_000.0);
    }

    @Test
    @Story("Basis Path")
    @Severity(SeverityLevel.NORMAL)
    @Description("BP3: D2=T, D3=T -> SALE10, SILVER, CARD, subtotal=200k => 210k")
    public void testBP3_couponSALE10_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE10", "SILVER", "CARD");
        Assert.assertEquals(result, 210_000.0);
    }

    @Test
    @Story("Basis Path")
    @Severity(SeverityLevel.NORMAL)
    @Description("BP4: D2=T, D3=F, D4=T -> SALE20, SILVER, CARD, subtotal=200k => 190k")
    public void testBP4_couponSALE20_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE20", "SILVER", "CARD");
        Assert.assertEquals(result, 190_000.0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Ma giam gia khong hop le")
    @Story("Basis Path")
    @Severity(SeverityLevel.CRITICAL)
    @Description("BP5: D2=T, D3=F, D4=F -> coupon INVALID -> throw exception")
    public void testBP5_invalidCoupon_throwsException() {
        List<Item> items = Arrays.asList(new Item(200_000));
        processor.calculateTotal(items, "INVALID", "SILVER", "CARD");
    }

    @Test
    @Story("Basis Path")
    @Severity(SeverityLevel.NORMAL)
    @Description("BP6: D5=T -> GOLD member, no coupon, CARD, subtotal=200k => 220k")
    public void testBP6_goldMember_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, null, "GOLD", "CARD");
        Assert.assertEquals(result, 220_000.0);
    }

    @Test
    @Story("Basis Path")
    @Severity(SeverityLevel.NORMAL)
    @Description("BP7: D6=T -> PLATINUM member, no coupon, CARD, subtotal=200k => 210k")
    public void testBP7_platinumMember_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, null, "PLATINUM", "CARD");
        Assert.assertEquals(result, 210_000.0);
    }

    @Test
    @Story("Basis Path")
    @Severity(SeverityLevel.NORMAL)
    @Description("BP8: D7=F -> total>=500k, no ship fee. subtotal=600k => 600k")
    public void testBP8_totalAbove500k_noShip() {
        List<Item> items = Arrays.asList(new Item(600_000));
        double result = processor.calculateTotal(items, null, "SILVER", "CARD");
        Assert.assertEquals(result, 600_000.0);
    }

    @Test
    @Story("Basis Path")
    @Severity(SeverityLevel.NORMAL)
    @Description("BP9: D7=T, D8=F -> COD ship, subtotal=100k => 120k")
    public void testBP9_noCoupon_noMember_shipCOD() {
        List<Item> items = Arrays.asList(new Item(100_000));
        double result = processor.calculateTotal(items, null, "SILVER", "COD");
        Assert.assertEquals(result, 120_000.0);
    }

    // =====================================================================
    // MC/DC TEST CASES cho D2 && D3
    // =====================================================================

    @Test
    @Story("MC/DC - D2 va D3")
    @Severity(SeverityLevel.NORMAL)
    @Description("MC/DC TC1: A=T, B=T, C=T -> True (vao nhanh SALE10). coupon=SALE10 => 210k")
    public void testMCDC1_allTrue_entersSALE10() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE10", "SILVER", "CARD");
        Assert.assertEquals(result, 210_000.0);
    }

    @Test
    @Story("MC/DC - D2 va D3")
    @Severity(SeverityLevel.NORMAL)
    @Description("MC/DC TC2: A=F -> couponCode=null, khong vao D2. Cap {MC1,MC2} => A doc lap")
    public void testMCDC2_AisFalse_couponNull() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, null, "SILVER", "CARD");
        Assert.assertEquals(result, 230_000.0);
    }

    @Test
    @Story("MC/DC - D2 va D3")
    @Severity(SeverityLevel.NORMAL)
    @Description("MC/DC TC3: A=T, B=F -> couponCode empty, khong vao D2. Cap {MC1,MC3} => B doc lap")
    public void testMCDC3_BisFalse_couponEmpty() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "", "SILVER", "CARD");
        Assert.assertEquals(result, 230_000.0);
    }

    @Test
    @Story("MC/DC - D2 va D3")
    @Severity(SeverityLevel.NORMAL)
    @Description("MC/DC TC4: A=T, B=T, C=F -> coupon=SALE20, khong vao D3. Cap {MC1,MC4} => C doc lap")
    public void testMCDC4_CisFalse_couponNotSALE10() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE20", "SILVER", "CARD");
        Assert.assertEquals(result, 190_000.0);
    }

    // =====================================================================
    // EXTRA TEST CASES - tang Branch Coverage JaCoCo >= 90%
    // =====================================================================

    @Test
    @Story("Branch Coverage")
    @Severity(SeverityLevel.NORMAL)
    @Description("GOLD member + SALE10 + COD, subtotal=200k => (200k-20k)*0.95=171k +20k=191k")
    public void testExtra_goldMember_sale10_COD() {
        List<Item> items = Arrays.asList(new Item(200_000));
        // subtotal=200000, discount=20000, memberDiscount=(200000-20000)*0.05=9000
        // total=200000-20000-9000=171000, <500000, COD => +20000
        double result = processor.calculateTotal(items, "SALE10", "GOLD", "COD");
        Assert.assertEquals(result, 191_000.0);
    }

    @Test
    @Story("Branch Coverage")
    @Severity(SeverityLevel.NORMAL)
    @Description("PLATINUM member + SALE20 + CARD, subtotal=200k => (200k-40k)*0.9=144k +30k=174k")
    public void testExtra_platinumMember_sale20_CARD() {
        List<Item> items = Arrays.asList(new Item(200_000));
        // subtotal=200000, discount=40000, memberDiscount=(200000-40000)*0.10=16000
        // total=200000-40000-16000=144000, <500000, CARD => +30000
        double result = processor.calculateTotal(items, "SALE20", "PLATINUM", "CARD");
        Assert.assertEquals(result, 174_000.0);
    }

    @Test
    @Story("Branch Coverage")
    @Severity(SeverityLevel.NORMAL)
    @Description("GOLD member, subtotal lon, total>=500k -> khong ship. subtotal=600k => 570k")
    public void testExtra_goldMember_noShip() {
        List<Item> items = Arrays.asList(new Item(600_000));
        // subtotal=600000, discount=0, memberDiscount=600000*0.05=30000
        // total=570000 >= 500000 => no ship
        double result = processor.calculateTotal(items, null, "GOLD", "CARD");
        Assert.assertEquals(result, 570_000.0);
    }

    @Test
    @Story("Branch Coverage")
    @Severity(SeverityLevel.NORMAL)
    @Description("PLATINUM member + COD, subtotal=100k => (100k)*0.9=90k +20k=110k")
    public void testExtra_platinumMember_COD() {
        List<Item> items = Arrays.asList(new Item(100_000));
        // subtotal=100000, discount=0, memberDiscount=100000*0.10=10000
        // total=90000 < 500000, COD => +20000
        double result = processor.calculateTotal(items, null, "PLATINUM", "COD");
        Assert.assertEquals(result, 110_000.0);
    }
}
