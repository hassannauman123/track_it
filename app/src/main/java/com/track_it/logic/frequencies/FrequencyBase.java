package com.track_it.logic.frequencies;

public abstract class FrequencyBase implements Frequency {

    public static final int MONTHS_PER_YEAR = 12;
    public static final int DAYS_PER_YEAR = 365;
    public static final int DAYS_PER_WEEK = 7;
    public static final int WEEKS_PER_MONTH = 4;
    public static final int WEEKS_PER_YEAR = 52;

    private final String frequencyName;

    public FrequencyBase(String setName) {
        frequencyName = setName;
    }

    @Override
    public String getFrequencyName() {
        return frequencyName;

    }

    @Override
    public boolean checkMatch(String inputCheck) {
        return frequencyName.equals(inputCheck);
    }

}