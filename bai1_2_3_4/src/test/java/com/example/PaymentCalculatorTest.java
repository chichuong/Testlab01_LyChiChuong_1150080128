package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PaymentCalculatorTest {

    @Test
    public void child_0_17() {
        assertEquals(50, PaymentCalculator.calculate(PaymentCalculator.PatientType.CHILD, 0));
        assertEquals(50, PaymentCalculator.calculate(PaymentCalculator.PatientType.CHILD, 17));
    }

    @Test
    public void male_18_35() {
        assertEquals(100, PaymentCalculator.calculate(PaymentCalculator.PatientType.MALE, 18));
        assertEquals(100, PaymentCalculator.calculate(PaymentCalculator.PatientType.MALE, 35));
    }

    @Test
    public void male_36_50() {
        assertEquals(120, PaymentCalculator.calculate(PaymentCalculator.PatientType.MALE, 36));
        assertEquals(120, PaymentCalculator.calculate(PaymentCalculator.PatientType.MALE, 50));
    }

    @Test
    public void male_51_145() {
        assertEquals(140, PaymentCalculator.calculate(PaymentCalculator.PatientType.MALE, 51));
        assertEquals(140, PaymentCalculator.calculate(PaymentCalculator.PatientType.MALE, 145));
    }

    @Test
    public void female_18_35() {
        assertEquals(80, PaymentCalculator.calculate(PaymentCalculator.PatientType.FEMALE, 18));
        assertEquals(80, PaymentCalculator.calculate(PaymentCalculator.PatientType.FEMALE, 35));
    }

    @Test
    public void female_36_50() {
        assertEquals(110, PaymentCalculator.calculate(PaymentCalculator.PatientType.FEMALE, 36));
        assertEquals(110, PaymentCalculator.calculate(PaymentCalculator.PatientType.FEMALE, 50));
    }

    @Test
    public void female_51_145() {
        assertEquals(140, PaymentCalculator.calculate(PaymentCalculator.PatientType.FEMALE, 51));
        assertEquals(140, PaymentCalculator.calculate(PaymentCalculator.PatientType.FEMALE, 145));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_age_negative() {
        PaymentCalculator.calculate(PaymentCalculator.PatientType.MALE, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_age_too_high() {
        PaymentCalculator.calculate(PaymentCalculator.PatientType.FEMALE, 146);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_child_over_17() {
        PaymentCalculator.calculate(PaymentCalculator.PatientType.CHILD, 18);
    }
}
