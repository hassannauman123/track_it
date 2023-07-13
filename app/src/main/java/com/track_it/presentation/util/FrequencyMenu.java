package com.track_it.presentation.util;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.track_it.R;
import com.track_it.logic.SubscriptionHandler;


//This class sets up an initializes a frequency Menu with the correct drop down input
public class FrequencyMenu
{

    public static void initializeMenu(final AppCompatActivity inputView, final SubscriptionHandler subHandler, final AutoCompleteTextView frequencyTarget )
    {
        // create an array adapter and pass the required parameters
        // in our case pass the context, drop down layout , and list of the frequencies.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(inputView, R.layout.list_item_frequency_menu, subHandler.getFrequencyNameList ());
        frequencyTarget.setAdapter(adapter);

    }
}
