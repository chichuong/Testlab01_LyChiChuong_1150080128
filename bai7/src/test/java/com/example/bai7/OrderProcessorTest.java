package com.example.bai7;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TestNG test class cho OrderProcessor.
 *
 * ===== CYCLOMATIC COMPLEXITY (CC) =====
 * Decision nodes: D1, D2, D3, D4, D5, D6, D7, D8 => 8 decisions
 * CC = 8 + 1 = 9 (cách đếm decision + 1)
 * CC = E - N + 2P (cách đếm edges - nodes + 2)
 *
 * ===== 9 BASIS PATHS =====
 * Path 1: D1=T → Exception "Gio hang trong"
 * Path 2: D1=F, D2=F, D5=F, D6=F, D7=T, D8=T → subtotal + 30000 (ship online)
 * Path 3: D1=F, D2=T, D3=T, D5=F, D6=F, D7=T, D8=T → SALE10 + ship online
 * Path 4: D1=F, D2=T, D3=F, D4=T, D5=F, D6=F, D7=T, D8=T → SALE20 + ship online
 * Path 5: D1=F, D2=T, D3=F, D4=F → Exception "Ma giam gia khong hop le"
 * Path 6: D1=F, D2=F, D5=T, D7=T, D8=T → GOLD member + ship online
 * Path 7: D1=F, D2=F, D5=F, D6=T, D7=T, D8=T → PLATINUM member + ship online
 * Path 8: D1=F, D2=F, D5=F, D6=F, D7=F → total >= 500k, no ship
 * Path 9: D1=F, D2=F, D5=F, D6=F, D7=T, D8=F → ship COD
 *
 * ===== MC/DC cho D2 && D3 =====
 * Dieu kien tong hop de vao nhanh SALE10: A && B && C
 * A = couponCode != null
 * B = !couponCode.isEmpty()
 * C = couponCode.equals("SALE10")
 *
 * | TC | A | B | C | Ket qua | Cap doc lap |
 * |------|---|---|---|---------|---------------------|
 * | MC1 | T | T | T | T | (baseline) |
 * | MC2 | F | - | - | F | {MC1,MC2} → A doc lap|
 * | MC3 | T | F | - | F | {MC1,MC3} → B doc lap|
 * | MC4 | T | T | F | F | {MC1,MC4} → C doc lap|
 */
public class OrderProcessorTest {

    private OrderProcessor processor;

    @BeforeMethod
    public void setUp() {
        processor = new OrderProcessor();
    }

    // =====================================================================
    // BASIS PATH TEST CASES
    // =====================================================================

    /**
     * Basis Path 1: D1=T (items == null)
     * items=null → IllegalArgumentException("Gio hang trong")
     */
    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Gio hang trong")
    public void testBP1_itemsNull_throwsException() {
        processor.calculateTotal(null, null, "SILVER", "CARD");
    }

    /**
     * Basis Path 1b: D1=T (items rong)
     * items=[] → IllegalArgumentException("Gio hang trong")
     */
    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Gio hang trong")
    public void testBP1b_itemsEmpty_throwsException() {
        processor.calculateTotal(new ArrayList<>(), null, "SILVER", "CARD");
    }

    /**
     * Basis Path 2: D1=F, D2=F, D5=F, D6=F, D7=T, D8=T
     * items=[100000], coupon=null, member="SILVER", payment="CARD"
     * subtotal=100000, discount=0, memberDiscount=0, total=100000
     * total < 500000, payment != COD → +30000
     * Expected: 130000
     */
    @Test
    public void testBP2_noCoupon_noMember_shipOnline() {
        List<Item> items = Arrays.asList(new Item(100_000));
        double result = processor.calculateTotal(items, null, "SILVER", "CARD");
        Assert.assertEquals(result, 130_000.0);
    }

    /**
     * Basis Path 3: D1=F, D2=T, D3=T, D5=F, D6=F, D7=T, D8=T
     * items=[200000], coupon="SALE10", member="SILVER", payment="CARD"
     * subtotal=200000, discount=20000, memberDiscount=0, total=180000
     * total < 500000, payment != COD → +30000
     * Expected: 210000
     */
    @Test
    public void testBP3_couponSALE10_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE10", "SILVER", "CARD");
        Assert.assertEquals(result, 210_000.0);
    }

    /**
     * Basis Path 4: D1=F, D2=T, D3=F, D4=T, D5=F, D6=F, D7=T, D8=T
     * items=[200000], coupon="SALE20", member="SILVER", payment="CARD"
     * subtotal=200000, discount=40000, memberDiscount=0, total=160000
     * total < 500000, payment != COD → +30000
     * Expected: 190000
     */
    @Test
    public void testBP4_couponSALE20_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE20", "SILVER", "CARD");
        Assert.assertEquals(result, 190_000.0);
    }

    /**
     * Basis Path 5: D1=F, D2=T, D3=F, D4=F
     * items=[200000], coupon="INVALID" → IllegalArgumentException("Ma giam gia
     * khong hop le")
     */
    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Ma giam gia khong hop le")
    public void testBP5_invalidCoupon_throwsException() {
        List<Item> items = Arrays.asList(new Item(200_000));
        processor.calculateTotal(items, "INVALID", "SILVER", "CARD");
    }

    /**
     * Basis Path 6: D1=F, D2=F, D5=T, D7=T, D8=T
     * items=[200000], coupon=null, member="GOLD", payment="CARD"
     * subtotal=200000, discount=0, memberDiscount=200000*0.05=10000
     * total=190000 < 500000, payment != COD → +30000
     * Expected: 220000
     */
    @Test
    public void testBP6_goldMember_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, null, "GOLD", "CARD");
        Assert.assertEquals(result, 220_000.0);
    }

    /**
     * Basis Path 7: D1=F, D2=F, D5=F, D6=T, D7=T, D8=T
     * items=[200000], coupon=null, member="PLATINUM", payment="CARD"
     * subtotal=200000, discount=0, memberDiscount=200000*0.10=20000
     * total=180000 < 500000, payment != COD → +30000
     * Expected: 210000
     */
    @Test
    public void testBP7_platinumMember_shipOnline() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, null, "PLATINUM", "CARD");
        Assert.assertEquals(result, 210_000.0);
    }

    /**
     * Basis Path 8: D1=F, D2=F, D5=F, D6=F, D7=F
     * items=[600000], coupon=null, member="SILVER", payment="CARD"
     * subtotal=600000, discount=0, memberDiscount=0, total=600000
     * total >= 500000 → khong tinh phi ship
     * Expected: 600000
     */
    @Test
    public void testBP8_totalAbove500k_noShip() {
        List<Item> items = Arrays.asList(new Item(600_000));
        double result = processor.calculateTotal(items, null, "SILVER", "CARD");
        Assert.assertEquals(result, 600_000.0);
    }

    /**
     * Basis Path 9: D1=F, D2=F, D5=F, D6=F, D7=T, D8=F
     * items=[100000], coupon=null, member="SILVER", payment="COD"
     * subtotal=100000, discount=0, memberDiscount=0, total=100000
     * total < 500000, payment == COD → +20000
     * Expected: 120000
     */
    @Test
    public void testBP9_noCoupon_noMember_shipCOD() {
        List<Item> items = Arrays.asList(new Item(100_000));
        double result = processor.calculateTotal(items, null, "SILVER", "COD");
        Assert.assertEquals(result, 120_000.0);
    }

    // =====================================================================
    // MC/DC TEST CASES cho D2 && D3
    // Dieu kien tong hop: A && B && C
    // A = couponCode != null
    // B = !couponCode.isEmpty()
    // C = couponCode.equals("SALE10")
    // =====================================================================

    /**
     * MC/DC TC1: A=T, B=T, C=T → True (vao nhanh SALE10)
     * couponCode="SALE10", items=[200000], member="SILVER", payment="CARD"
     * subtotal=200000, discount=20000, total=180000, +30000 = 210000
     */
    @Test
    public void testMCDC1_allTrue_entersSALE10() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE10", "SILVER", "CARD");
        Assert.assertEquals(result, 210_000.0);
    }

    /**
     * MC/DC TC2: A=F → False (couponCode=null, khong vao D2)
     * Cap doc lap {MC1, MC2}: chi A thay doi → ket qua thay doi
     * couponCode=null, items=[200000], member="SILVER", payment="CARD"
     * subtotal=200000, discount=0, total=200000, +30000 = 230000
     */
    @Test
    public void testMCDC2_AisFalse_couponNull() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, null, "SILVER", "CARD");
        Assert.assertEquals(result, 230_000.0);
    }

    /**
     * MC/DC TC3: A=T, B=F → False (couponCode="", khong vao D2)
     * Cap doc lap {MC1, MC3}: chi B thay doi → ket qua thay doi
     * couponCode="", items=[200000], member="SILVER", payment="CARD"
     * subtotal=200000, discount=0, total=200000, +30000 = 230000
     */
    @Test
    public void testMCDC3_BisFalse_couponEmpty() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "", "SILVER", "CARD");
        Assert.assertEquals(result, 230_000.0);
    }

    /**
     * MC/DC TC4: A=T, B=T, C=F → False (couponCode="SALE20", khong vao D3)
     * Cap doc lap {MC1, MC4}: chi C thay doi → ket qua thay doi
     * couponCode="SALE20", items=[200000], member="SILVER", payment="CARD"
     * subtotal=200000, discount=40000, total=160000, +30000 = 190000
     */
    @Test
    public void testMCDC4_CisFalse_couponNotSALE10() {
        List<Item> items = Arrays.asList(new Item(200_000));
        double result = processor.calculateTotal(items, "SALE20", "SILVER", "CARD");
        Assert.assertEquals(result, 190_000.0);
    }
}
