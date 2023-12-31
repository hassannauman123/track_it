package com.track_it.logic;


import com.track_it.domainobject.SubscriptionObj;
import com.track_it.domainobject.SubscriptionTag;
import com.track_it.logic.exceptions.SubscriptionTagException;
import com.track_it.persistence.SubscriptionTagPersistence;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionTagHandler {

    private final int MIN_NAME_TAG_LENGTH;
    private final int MAX_NAME_TAG_LENGTH;
    private final String TAG_SPLIT_CRITERIA;

    private final SubscriptionTagPersistence subscriptionTagPersistence;

    public SubscriptionTagHandler(String inputTagSplitCriteria, int inputMinNameLength, int inputMaxNameLength, SubscriptionTagPersistence inputDatabase) {
        this.subscriptionTagPersistence = inputDatabase;
        this.TAG_SPLIT_CRITERIA = inputTagSplitCriteria;
        this.MIN_NAME_TAG_LENGTH = inputMinNameLength;
        this.MAX_NAME_TAG_LENGTH = inputMaxNameLength;
    }


    public void changeSubTags(SubscriptionObj inputSub) {
        this.subscriptionTagPersistence.changeSubscriptionTags(inputSub);
    }


    public void removeSubTagsByID(int subID) {
        this.subscriptionTagPersistence.removeAllTagsBySubID(subID);
    }

    public void removeUnusedTags() {
        this.subscriptionTagPersistence.removeUnusedTags();
    }

    public void addTag(SubscriptionTag tagToAdd) {
        this.subscriptionTagPersistence.addTagToPersistence(tagToAdd);
    }

    public List<SubscriptionTag> getTagsForSubscription(SubscriptionObj inputSub) {
        return subscriptionTagPersistence.getTagsForSubscription(inputSub);
    }


    //Return all tags in database
    public List<SubscriptionTag> getAllSubTags() {
        return this.subscriptionTagPersistence.getAllTags();
    }


    public void validateTag(SubscriptionTag inputTag) throws SubscriptionTagException {
        validateTagName(inputTag.getName());
    }

    public void validateTagName(String inputName) throws SubscriptionTagException {

        if (inputName == null) {
            throw new SubscriptionTagException("A tag was invalid!");
        } else if (inputName.length() < MIN_NAME_TAG_LENGTH) {
            throw new SubscriptionTagException("A Tag was too short!");
        } else if (inputName.length() > MAX_NAME_TAG_LENGTH) {
            throw new SubscriptionTagException("Tag max " + MAX_NAME_TAG_LENGTH + " chars long");
        }
    }


    public String getSplitCriteria() {
        return TAG_SPLIT_CRITERIA;
    }

    public int getMaxTagNameLength() {
        return MAX_NAME_TAG_LENGTH;
    }



    //Convert a string to a list of subscription tags
    public List<SubscriptionTag> stringToTags(String inputTagString) throws SubscriptionTagException {
        List<SubscriptionTag> tagList = new ArrayList<SubscriptionTag>();
        String splitTagString[] = inputTagString.split(TAG_SPLIT_CRITERIA);


        for (String newTag : splitTagString) {
            if (!newTag.trim().equals("")) // Only bother adding non blank tags
            {
                validateTagName(newTag);

                //Check if that tag has already been added to list (this prevents identical tags from being added to list)
                boolean tagAlreadyAdded = false;
                for (SubscriptionTag currTag : tagList) {
                    if (currTag.getName().equals(newTag)) {
                        tagAlreadyAdded = true;
                    }
                }

                if (!tagAlreadyAdded) {
                    tagList.add(new SubscriptionTag((newTag)));
                }
            }
        }

        return tagList;
    }

}
