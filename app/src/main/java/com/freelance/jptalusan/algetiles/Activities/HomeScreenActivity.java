package com.freelance.jptalusan.algetiles.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.freelance.jptalusan.algetiles.R;
import com.freelance.jptalusan.algetiles.Utilities.Constants;

public class HomeScreenActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private Button multiplyActivityButton;
    private Button factorActivityButton;

    private Button factorTutorialButton;
    private Button multiplyTutorialButton;
    private Button generalTutorialButton;

    private Button oneVarBtn;
    private Button twoVarBtn;

    private String activityType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        multiplyActivityButton = findViewById(R.id.multiply_button);
        multiplyActivityButton.setOnClickListener(button_click);

        factorActivityButton = findViewById(R.id.factor_button);
        factorActivityButton.setOnClickListener(button_click);

        factorTutorialButton = findViewById(R.id.factorTutorialButton);
        multiplyTutorialButton = findViewById(R.id.multiplyTutorialButton);
        generalTutorialButton = findViewById(R.id.generalTutorialButton);
        factorTutorialButton.setOnClickListener(tutorial_button_click);
        multiplyTutorialButton.setOnClickListener(tutorial_button_click);
        generalTutorialButton.setOnClickListener(tutorial_button_click);

        oneVarBtn = findViewById(R.id.one_variable_button);
        twoVarBtn = findViewById(R.id.two_variable_button);

        if (prefs.getBoolean(Constants.FIRST_TIME, true))
        {
            prefs.edit().putBoolean(Constants.FIRST_TIME, false).apply();

            Intent intent = new Intent(this, TutorialActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
//            cameFromTutorial = true;
        }
    }

    View.OnClickListener button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button view = (Button) v;
            activityType = view.getText().toString();
            multiplyActivityButton.setVisibility(View.GONE);
            factorActivityButton.setVisibility(View.GONE);
            factorTutorialButton.setVisibility(View.INVISIBLE);
            multiplyTutorialButton.setVisibility(View.INVISIBLE);
            generalTutorialButton.setVisibility(View.INVISIBLE);

            oneVarBtn.setVisibility(View.VISIBLE);
            if (Constants.FACTOR.equals(view.getText())) { //Since Factor only uses 1 variable
                twoVarBtn.setVisibility(View.INVISIBLE);
            } else {
                twoVarBtn.setVisibility(View.VISIBLE);
            }

            oneVarBtn.setOnClickListener(var_click);
            twoVarBtn.setOnClickListener(var_click);
        }
    };

    @Override
    public void onBackPressed() {
        if (oneVarBtn.getVisibility() == View.VISIBLE) {
            multiplyActivityButton.setVisibility(View.VISIBLE);
            factorActivityButton.setVisibility(View.VISIBLE);
            factorTutorialButton.setVisibility(View.VISIBLE);
            multiplyTutorialButton.setVisibility(View.VISIBLE);
            generalTutorialButton.setVisibility(View.VISIBLE);

            oneVarBtn.setVisibility(View.GONE);
            twoVarBtn.setVisibility(View.GONE);

        } else {
            finish();
        }
    }

    View.OnClickListener tutorial_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.factorTutorialButton:
                    Intent factorIntent = new Intent(getApplicationContext(), VideoActivity.class);
                    factorIntent.putExtra(Constants.VIDEO_ID, R.raw.factor_mod);
                    startActivity(factorIntent);
                    break;
                case R.id.multiplyTutorialButton:
                    Intent multiplyIntent = new Intent(getApplicationContext(), VideoActivity.class);
                    multiplyIntent.putExtra(Constants.VIDEO_ID, R.raw.multiply_mod);
                    startActivity(multiplyIntent);
                    break;
                case R.id.generalTutorialButton:
                    Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
//                    intent.PutExtra("TutoriialType", "General");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    View.OnClickListener var_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Button button = (Button) v;

            if (Constants.FACTOR.equals(activityType)) {
                Intent intent = new Intent(getApplicationContext(), FactorActivity.class);
                if (Character.getNumericValue(button.getText().charAt(0)) == 1) {
                    intent.putExtra(Constants.VARIABLE_COUNT, Character.getNumericValue(button.getText().charAt(0)));
                } else
                    Toast.makeText(getApplicationContext(), "Not implemented.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else if (Constants.MULTIPLY.equals(activityType) && Constants.ONE_VAR == Character.getNumericValue(button.getText().charAt(0))) {
                Intent intent = new Intent(getApplicationContext(), MultiplyActivity.class);
                intent.putExtra(Constants.VARIABLE_COUNT, Character.getNumericValue(button.getText().charAt(0)));
                startActivity(intent);
            } else if (Constants.MULTIPLY.equals(activityType) && Constants.TWO_VAR == Character.getNumericValue(button.getText().charAt(0))) {
                Intent intent = new Intent(getApplicationContext(), MultiplyTwoVarActivity.class);
                intent.putExtra(Constants.VARIABLE_COUNT, Character.getNumericValue(button.getText().charAt(0)));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };
}
