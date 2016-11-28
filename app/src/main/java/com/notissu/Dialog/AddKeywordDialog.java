package com.notissu.Dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.notissu.R;

/**
 * Created by Sunghyeok on 2016-11-03.
 */

public class AddKeywordDialog extends DialogFragment {
    View rootView;
    Button btnKeyword;
    EditText etText;

    OnAddKeywordListner onAddKeywordListner;

    public static AddKeywordDialog newInstance() {
        Bundle args = new Bundle();

        AddKeywordDialog fragment = new AddKeywordDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("키워드 추가");

        rootView = inflater.inflate(R.layout.dialog_keyword, null, false);

        btnKeyword = (Button) rootView.findViewById(R.id.button_keyword_set);
        etText = (EditText) rootView.findViewById(R.id.edittext_keyword_set);


        btnKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String output = etText.getText().toString();
                bundle.putString("aa",output);
                onAddKeywordListner.onAdd(bundle);
                dismiss();
                /*Intent intent = new Intent();
                ComponentName componentName = new ComponentName(
                        "com.notissu",
                        "com.notissu.Activity.MainActivity");
                intent.setComponent(componentName);


                String output = etText.getText().toString();
                intent.putExtra("Keyword_name", output);
                startActivity(intent);

*/
            }
        });
     //   btnKeyword.setOnClickListener(this);



        return rootView;

    }

    public void setOnAddKeywordListner(OnAddKeywordListner onAddKeywordListner) {
        this.onAddKeywordListner = onAddKeywordListner;
    }

    public interface OnAddKeywordListner {
        void onAdd(Bundle bundle);
    }
}