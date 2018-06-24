package com.timbirichi.eltimbirichi.presentation.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.timbirichi.eltimbirichi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDialog extends DialogFragment {

    public static final String EXTRA_PHOTOS = "com.timbitichi.eltimbirichi.with_photos";
    public static final String EXTRA_MIN = "com.timbitichi.eltimbirichi.min";
    public static final String EXTRA_MAX = "com.timbitichi.eltimbirichi.max";
    public static final String EXTRA_NEW_PRODUCT = "com.timbitichi.eltimbirichi.is_new";

    int min, max;
    boolean photos, newProduct;

//    @BindView(R.id.btn_filter)
//    Button btnFilter;

    @BindView(R.id.et_min)
    TextInputEditText etMin;

    @BindView(R.id.et_max)
    TextInputEditText etMax;

    @BindView(R.id.sw_images)
    Switch swImage;

    @BindView(R.id.sw_new)
    Switch swNew;


    public static FilterDialog newInstance(boolean photos, int min, int max, boolean newProduct) {

        Bundle args = new Bundle();

        args.putBoolean(EXTRA_PHOTOS, photos);
        args.putInt(EXTRA_MIN, min);
        args.putInt(EXTRA_MAX, max);
        args.putBoolean(EXTRA_NEW_PRODUCT, newProduct);



        FilterDialog fragment = new FilterDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            min = getArguments().getInt(EXTRA_MIN);
            max = getArguments().getInt(EXTRA_MAX);
            photos = getArguments().getBoolean(EXTRA_PHOTOS);
            newProduct = getArguments().getBoolean(EXTRA_NEW_PRODUCT);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.dialog_filter, null);

        ButterKnife.bind(this, v);
        setupUi();

        builder.setView(v);

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }



    private void setupUi(){
        if (min != 0){
            etMin.setText(Integer.toString(min));
        }

        if (max != 0){
            etMax.setText(Integer.toString(max));
        }

        swImage.setChecked(photos);
        swNew.setChecked(newProduct);
    }
}
