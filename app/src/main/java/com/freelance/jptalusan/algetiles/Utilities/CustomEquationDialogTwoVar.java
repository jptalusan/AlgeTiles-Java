package com.freelance.jptalusan.algetiles.Utilities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.freelance.jptalusan.algetiles.R;

/**
 * Created by jptalusan on 10/6/17.
 */

public class CustomEquationDialogTwoVar extends DialogFragment implements View.OnClickListener {
    //Create class properties
    protected EditText x_value_1;
    protected EditText y_value_1;
    protected EditText one_value_1;
    protected EditText x_value_2;
    protected EditText y_value_2;
    protected EditText one_value_2;
    CustomEquationDialogTwoVarListener listener;

    //protected Button ok;
    //protected Button cancel;
    public interface CustomEquationDialogTwoVarListener {
        void onButtonClicked(int[] vars);
    }


    public CustomEquationDialogTwoVar() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CustomEquationDialogTwoVar newInstance(String title) {
        CustomEquationDialogTwoVar frag = new CustomEquationDialogTwoVar();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (CustomEquationDialogTwoVarListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_custom_question_two_var, container, false);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ok).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);

        //Initialize the properties
        x_value_1 = view.findViewById(R.id.x_value_1);
        y_value_1 = view.findViewById(R.id.y_value_1);
        one_value_1 = view.findViewById(R.id.one_value_1);

        x_value_2 = view.findViewById(R.id.x_value_2);
        y_value_2 = view.findViewById(R.id.y_value_2);
        one_value_2 = view.findViewById(R.id.one_value_2);
    }

        @Override
    public void onClick(View v) {

            switch (v.getId()) {
            case R.id.ok:
                Log.d("Custom", "ok");
                handleOkClick();
                this.dismiss();
                break;
            case R.id.cancel:
                Log.d("Custom", "cancel");
                this.dismiss();
                listener.onButtonClicked(new int[1]);
                break;
        }
    }

    private void handleOkClick() {

        int[] questions = new int[6];
        questions[0] = Constants.UNDEFINED;
        questions[1] = Constants.UNDEFINED;
        questions[2] = Constants.UNDEFINED;
        questions[3] = Constants.UNDEFINED;
        questions[4] = Constants.UNDEFINED;
        questions[5] = Constants.UNDEFINED;

        if (!(x_value_1.getText().toString().trim().isEmpty()))
        {
            questions[0] = TileUtilities.tryParseInt(x_value_1.getText().toString());
        }

        if (!(y_value_1.getText().toString().trim().isEmpty()))
        {
            questions[1] = TileUtilities.tryParseInt(y_value_1.getText().toString());
        }

        if (!(one_value_1.getText().toString().trim().isEmpty()))
        {
            questions[2] = TileUtilities.tryParseInt(one_value_1.getText().toString());
        }

        if (!(x_value_2.getText().toString().trim().isEmpty()))
        {
            questions[3] = TileUtilities.tryParseInt(x_value_2.getText().toString());
        }

        if (!(y_value_2.getText().toString().trim().isEmpty()))
        {
            questions[4] = TileUtilities.tryParseInt(y_value_2.getText().toString());
        }

        if (!(one_value_2.getText().toString().trim().isEmpty()))
        {
            questions[5] = TileUtilities.tryParseInt(one_value_2.getText().toString());
        }

        if (questions[0] != Constants.UNDEFINED &&
                questions[1] != Constants.UNDEFINED &&
                questions[2] != Constants.UNDEFINED &&
                questions[3] != Constants.UNDEFINED &&
                questions[4] != Constants.UNDEFINED &&
                questions[5] != Constants.UNDEFINED )
        {
            listener.onButtonClicked(questions);
        } else {
            Toast.makeText(getActivity(), "Invalid, please enter values again.", Toast.LENGTH_SHORT).show();
        }
    }
}
