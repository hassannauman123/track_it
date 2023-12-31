package com.track_it;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.logic.comparators.CompareSubscriptionFrequency;
import com.track_it.logic.comparators.CompareSubscriptionName;
import com.track_it.logic.comparators.CompareSubscriptionPayment;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.frequencies.Frequency;
import com.track_it.application.SetupParameters;
import com.track_it.util.TestUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.List;

public class SubscriptionSortTest {


    private SubscriptionHandler subHandle;
    private List<SubscriptionObj> listOfSubs;

    @Before
    public void setTestHandle() {

        TestUtils.changeDatabase();
        subHandle = SetupParameters.getSubscriptionHandler();
        TestUtils.create10Sub(subHandle);  //Create 10  subs, so we can test sortng them
        listOfSubs = subHandle.getAllSubscriptions(); // Get subscriptions from database
    }

    @Test
    public void testSortByName() {

        Collections.sort(listOfSubs, new CompareSubscriptionName()); //Sort by name

        for (int i = 0; i < listOfSubs.size() - 1; i++) {

            String firstName = listOfSubs.get(i).getName();
            String secondName = listOfSubs.get(i + 1).getName();
            assertTrue("FAILED sortName test\n. " + firstName + " was before " + secondName + ". After sorting by name", firstName.toLowerCase().compareTo(secondName.toLowerCase()) <= 0);
        }


        System.out.println("PASSED the sort by name test");
    }


    @Test
    public void testSortByPayment() {

        Collections.sort(listOfSubs, new CompareSubscriptionPayment()); //Sort by payment amount

        for (int i = 0; i < listOfSubs.size() - 1; i++) {
            int firstPay = listOfSubs.get(i).getTotalPaymentInCents();
            int secondPay = listOfSubs.get(i + 1).getTotalPaymentInCents();
            assertTrue("FAILED sortPayment test. " + firstPay + " was before " + secondPay + ". After sorting by payment", firstPay <= secondPay);
        }


        System.out.println("PASSED the sort by payment test ");
    }


    @Test
    public void testSortFrequency() {
        Collections.sort(listOfSubs, new CompareSubscriptionFrequency()); // Sort by frequency amount

        for (int i = 0; i < listOfSubs.size() - 1; i++) {
            String firstFrequencyName = listOfSubs.get(i).getPaymentFrequency();
            String secondFrequencyName = listOfSubs.get(i + 1).getPaymentFrequency();
            Frequency firstFrequency = subHandle.getFrequencyObject(listOfSubs.get(i));
            Frequency secondFrequency = subHandle.getFrequencyObject(listOfSubs.get(i + 1));
            assertTrue("FAILED frequency test. " + firstFrequencyName + " was before " + secondFrequencyName + ". After sorting by frequency", firstFrequency.daysBetweenPayment() <= secondFrequency.daysBetweenPayment());
        }

        System.out.println("PASSED the sort by payment frequency test");

    }


}
