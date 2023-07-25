package com.track_it;


import com.track_it.application.SetupParameters;
import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.SubscriptionHandler;
import com.track_it.logic.exceptions.SubscriptionTagException;
import com.track_it.persistence.SubscriptionPersistence;
import com.track_it.persistence.SubscriptionTagPersistence;
import com.track_it.util.TestUtils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


//This has been updated to use mockito mocks for the database to test unit.

public class UnitTagTest {

    private SubscriptionHandler subHandle;
    private SubscriptionTagPersistence tagDBMock;
    private SubscriptionPersistence subDBMock;

    //Setup a fake database each time
    @Before
    public void setTestHandle() {
        //Mock the databases
        tagDBMock = mock(SubscriptionTagPersistence.class);
        subDBMock = mock(SubscriptionPersistence.class);

        //Inject mocked database
        SetupParameters.initializeDatabase(subDBMock, tagDBMock);
        subHandle = SetupParameters.getSubscriptionHandler();

    }


    @Test
    public void addSubWithTagsTest() {


        int customID = 1;
        String tag1 = "tag_1";
        String tag2 = "tag_2";

        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazons", 104, subHandle.getFrequencyNameList().get(0));
        newSubToAdd.setID(customID);

        subHandle.setTags(newSubToAdd, tag1 + " " + tag2);
        subHandle.addSubscription(newSubToAdd);


        //Set mock rules
        when(tagDBMock.getTagsForSubscription(newSubToAdd)).thenReturn(newSubToAdd.getTagList());
        when(subDBMock.getSubscriptionByID(customID)).thenReturn(newSubToAdd);


        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());
        List<SubscriptionTag> returnedTags = subReturnedFromDatabase.getTagList();


        assertTrue("Failed addSubWithTagsTest: added a sub with 2 tags, and received a sub back with " + returnedTags.size() + " number of tags ", returnedTags.size() == 2);

        for (SubscriptionTag currTag : returnedTags) {
            String currTagName = currTag.getName();
            assertTrue("Failed addSubWithTagsTest: a sub was added with tags named " + tag1 + " " + tag2 + " but received " + currTagName.length(), currTagName.equals(tag1) || currTagName.equals(tag2));
        }

        System.out.println("PASSED: addSubWithTagsTest test ");

    }

    @Test
    public void multipleSubsTagsTest() {


        //Tags we will add
        String[] addTags = {"tag_one", "tag_two", "tag_3", "tag_4", "tag_5"};

        //Used for mocking
        String stringOfAllTags = "";
        for (String currTagName : addTags) {
            stringOfAllTags += currTagName + "  ";
        }
        //Mock the database
        List<SubscriptionTag> allTheTagsWeAdded = subHandle.getTagHandler().stringToTags(stringOfAllTags);
        when(tagDBMock.getAllTags()).thenReturn(allTheTagsWeAdded);


        //Add a sub with 2 tags
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));
        subHandle.setTags(newSubToAdd, addTags[0] + " " + addTags[1]);
        subHandle.addSubscription(newSubToAdd);

        //Add another sub with 3 tags
        newSubToAdd = new SubscriptionObj("WareHouse", 300, subHandle.getFrequencyNameList().get(0));
        subHandle.setTags(newSubToAdd, addTags[2] + " " + addTags[3] + " " + addTags[4]);
        subHandle.addSubscription(newSubToAdd);


        // Get all tags from database
        List<SubscriptionTag> returnedTags = subHandle.getTagHandler().getAllSubTags();


        assertTrue("Failed tag test! Did not get back 5 tags when added across 2 different sub, " + returnedTags.size() + " was the returned size.", returnedTags.size() == addTags.length);


        for (SubscriptionTag currTag : returnedTags) {

            boolean match = false;
            for (String tagAdded : addTags) {
                if (tagAdded.equals(currTag.getName())) {
                    match = true;
                }
            }

            assertTrue("Failed multipleSubsTagsTest: added tags to subs, but got subs back from database that did not match any added tags", match);
        }


        System.out.println("PASSED: multipleSubsTagsTest test ");

    }


    @Test
    public void removeTagTest() {
        SubscriptionObj newSubToAdd = new SubscriptionObj("Amazon", 1000, subHandle.getFrequencyNameList().get(0));

        String[] addTags = {"tag_one", "tag_two"};
        subHandle.setTags(newSubToAdd, addTags[0] + " " + addTags[1]);
        subHandle.addSubscription(newSubToAdd);

        //Mock Database
        when(subDBMock.getSubscriptionByID(newSubToAdd.getID())).thenReturn(newSubToAdd);
        when(tagDBMock.getTagsForSubscription(newSubToAdd)).thenReturn(newSubToAdd.getTagList());


        SubscriptionObj subReturnedFromDatabase = subHandle.getSubscriptionByID(newSubToAdd.getID());

        subReturnedFromDatabase.setTagList(new ArrayList<SubscriptionTag>());
        subHandle.editWholeSubscription(subReturnedFromDatabase.getID(), subReturnedFromDatabase);


        //Get all return tags
        List<SubscriptionTag> returnedTags = subReturnedFromDatabase.getTagList();

        assertTrue("FAILED removeTagTest: failed to remove tags added to a subscription", returnedTags.size() == 0);


        System.out.println("PASSED: removeTagTag test ");

    }


    @Test
    public void invalidTagTest() {

        //No point in mocking database as it is never used


        String invalidTags = "";

        for (int i = 0; i < subHandle.getMaxTags() + 1; i++) {
            invalidTags += "tag" + i + " ";
        }

        // Add 1 too many tags to sub
        SubscriptionObj newSubToAdd = new SubscriptionObj("WareHouse", 300, subHandle.getFrequencyNameList().get(0));
        subHandle.setTags(newSubToAdd, invalidTags);


        boolean exceptionThrown = false;
        try {
            subHandle.validateTagList(newSubToAdd.getTagList()); // try to validate list of subs
        } catch (SubscriptionTagException e) {
            exceptionThrown = true;
        }

        assertTrue("FAILED invalidTagsTes, tried to validate too many tags, but exception not thrown", exceptionThrown);


        System.out.println("PASSED: invalidTagTest test ");
    }

}
