package com.laptopfix.laptopfixrun.Activities.Customer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.laptopfix.laptopfixrun.Fragment.Customer.HomeServiceFragment;
import com.laptopfix.laptopfixrun.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChangeAddressActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener, TextWatcher,
        OnCompleteListener<FindAutocompletePredictionsResponse>, SuggestionsAdapter.OnItemViewClickListener,
        View.OnClickListener{

    private Toolbar toolbar;
    private MaterialSearchBar searchBarPlaces;
    private AutocompleteSessionToken token;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private List<String> suggestionList;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        showToolbar(getString(R.string.address), true);

        searchBarPlaces = findViewById(R.id.searchBarPlaces);
        btnSave = findViewById(R.id.btnSave);
        if(!Places.isInitialized()){
            Places.initialize(this, getString(R.string.google_server_key));
        }
        placesClient = Places.createClient(this);
        token = AutocompleteSessionToken.newInstance();
        suggestionList = new ArrayList<>();
        searchBarPlaces.setOnSearchActionListener(this);
        searchBarPlaces.addTextChangeListener(this);
        btnSave.setOnClickListener(this);
    }

    private void showToolbar(String title, boolean upButton) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(searchBarPlaces.getText().length() == 0){
            searchBarPlaces.clearSuggestions();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        final FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                .setCountry("mx")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(s.toString())
                .build();
        placesClient.findAutocompletePredictions(predictionsRequest)
                .addOnCompleteListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(searchBarPlaces.getText().length() == 0){
            searchBarPlaces.clearSuggestions();
            suggestionList.clear();
        }
    }

    @Override
    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
        if(task.isSuccessful()){
            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
            if(predictionsResponse != null){
                predictionList = predictionsResponse.getAutocompletePredictions();
                suggestionList.clear();
                for(int i = 0; i < predictionList.size(); i++){
                    AutocompletePrediction prediction = predictionList.get(i);
                    suggestionList.add(prediction.getFullText(null).toString());
                }
                updateSuggestion();
            }
        }else {
            Log.d("ERROR", ""+task.getException().getMessage());
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        if(buttonCode == MaterialSearchBar.BUTTON_NAVIGATION){
            //Opening or closing a navigation drawer
        }else if(buttonCode == MaterialSearchBar.BUTTON_BACK){
            searchBarPlaces.disableSearch();
            searchBarPlaces.clearSuggestions();
        }
    }

    @Override
    public void OnItemClickListener(int position, View v) {
        if(position >= predictionList.size()){
            return;
        }
        final String suggestion = searchBarPlaces.getLastSuggestions().get(position).toString();
        searchBarPlaces.setText(suggestion);
        searchBarPlaces.clearSuggestions();
        suggestionList.clear();
        updateSuggestion();
    }

    @Override
    public void OnItemDeleteListener(int position, View v) {
        suggestionList.remove(position);
        if(suggestionList.size() != 0){
            updateSuggestion();
        }
    }

    public void updateSuggestion(){
        searchBarPlaces.updateLastSuggestions(suggestionList);
        if(!searchBarPlaces.isSuggestionsVisible()) {
            searchBarPlaces.showSuggestionsList();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
