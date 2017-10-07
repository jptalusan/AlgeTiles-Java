package com.freelance.jptalusan.algetiles.Utilities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.freelance.jptalusan.algetiles.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jptalusan on 10/6/17.
 */

public class CustomEquationDialogFactor extends DialogFragment implements View.OnClickListener {
    //Create class properties
    protected Spinner equationsSpinner;;
    CustomEquationDialogFactorListener listener;
    protected String SelectedCategory = "";
    protected int index = -1;

    //protected Button ok;
    //protected Button cancel;
    public interface CustomEquationDialogFactorListener {
        void onButtonClicked(int index);
    }


    public CustomEquationDialogFactor() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CustomEquationDialogFactor newInstance(String title) {
        CustomEquationDialogFactor frag = new CustomEquationDialogFactor();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (CustomEquationDialogFactorListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_custom_question_factor, container, false);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ok).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        equationsSpinner = view.findViewById(R.id.equationsSpinner);
        LoadSpinnerData();
    }

    private void LoadSpinnerData()
    {
        //Should parse here:
        Log.d("const fac: ", "eq: " + Constants.EQUATIONS.get(2));

        ArrayAdapter<String> equationsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, formatEquations(Constants.EQUATIONS));
        equationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        equationsSpinner.setAdapter(equationsAdapter);
        equationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedCategory = String.format("{0}", equationsSpinner.getItemAtPosition(position));
                index = position;
                Log.d("Custom factor: ", SelectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<String> formatEquations(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();
        int cnt = 1;
        for (String s : input)
        {
            Log.d("custom fac: ", s);
            String[] vals = s.split(",");
            ArrayList<Integer> temp = new ArrayList<>();
            for (String val : vals)
            {
                temp.add(Integer.parseInt(val));
            }
            ArrayList<Integer> expanded = AlgorithmUtilities.expandingVars(temp);
            String equation = "[" + cnt + "] " + setupQuestionString(expanded);//expanded[0] + "x\xB2 + " + expanded[1] + "x + " + expanded[2];
            output.add(equation);
            ++cnt;
        }
        return output;
    }

    private SpannableStringBuilder setupQuestionString(ArrayList<Integer> vars)
    {
        String output = "";
        //vars = (ax^2 + bx + c)
        int ax2 = vars.get(0);
        int bx = vars.get(1);
        int c = vars.get(2);

        if (ax2 != 0) {
            output += ax2 + "x2";
        }
        if (bx != 0) {
            output += "+" + bx + "x+";
        }
        if (c != 0) {
            output += c;
        }

        output = output.replace(" ", "");
        output = output.replace("+-", "-");
        output = output.replace("+", " + ");
        output = output.replace("-", " - ");

        Log.d("Custom dialog factor.", "output: " + output);
        SpannableStringBuilder cs = new SpannableStringBuilder(output);
        int indexOfSuperScript = output.indexOf("x2") + 1;
        cs.setSpan(new SuperscriptSpan(), indexOfSuperScript, indexOfSuperScript + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        cs.setSpan(new RelativeSizeSpan(0.75f), indexOfSuperScript, indexOfSuperScript + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return cs;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ok:
                Log.d("Custom", "ok");
                listener.onButtonClicked(index);
                this.dismiss();
                break;
            case R.id.cancel:
                Log.d("Custom", "cancel");
                this.dismiss();
                listener.onButtonClicked(-1);
                break;
        }
    }
}
