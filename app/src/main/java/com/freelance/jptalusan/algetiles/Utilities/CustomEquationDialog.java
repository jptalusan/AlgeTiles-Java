package com.freelance.jptalusan.algetiles.Utilities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.freelance.jptalusan.algetiles.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jptalusan on 10/6/17.
 */

public class CustomEquationDialog extends DialogFragment implements View.OnClickListener {
    //Create class properties
    protected EditText x_value_1;
    protected EditText one_value_1;
    protected EditText x_value_2;
    protected EditText one_value_2;

    //protected Button ok;
    //protected Button cancel;
    public interface CustomEquationDialogListener {
//        void onButtonClicked(ArrayList<Integer> vars);
        void onButtonClicked(String s);
    }


    public CustomEquationDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CustomEquationDialog newInstance(String title) {
        CustomEquationDialog frag = new CustomEquationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_custom_question, container, false);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ok).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);

        //Initialize the properties
        x_value_1 = view.findViewById(R.id.x_value_1);
        one_value_1 = view.findViewById(R.id.one_value_1);

        x_value_2 = view.findViewById(R.id.x_value_2);
        one_value_2 = view.findViewById(R.id.one_value_2);
    }

        @Override
    public void onClick(View v) {
            CustomEquationDialogListener listener = (CustomEquationDialogListener) getActivity();
            switch (v.getId()) {
            case R.id.ok:
                Log.d("Custom", "ok");
                listener.onButtonClicked("ok works.");

                break;
            case R.id.cancel:
                Log.d("Custom", "cancel");
                listener.onButtonClicked("cancel works.");
                break;
        }
    }
}
