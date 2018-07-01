package com.timbirichi.eltimbirichi.presentation.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.presentation.adapter.ProvinceSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterDialog extends DialogFragment {

    public static final String EXTRA_PHOTOS = "com.timbitichi.eltimbirichi.with_photos";
    public static final String EXTRA_MIN = "com.timbitichi.eltimbirichi.min";
    public static final String EXTRA_MAX = "com.timbitichi.eltimbirichi.max";
    public static final String EXTRA_NEW_PRODUCT = "com.timbitichi.eltimbirichi.is_new";
    public static final String EXTRA_PROVINCES = "com.timbitichi.eltimbirichi.provinces";
    public static final String EXTRA_PROVINCE = "com.timbitichi.eltimbirichi.province";

    int min, max;
    boolean photos, newProduct;

    List<Province> provinces;

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

    @BindView(R.id.sp_province)
    Spinner spProvince;

    Province provinceSelected;
    long province;

    @NonNull
    FilterDialogCallback filterDialogCallback;


    public static FilterDialog newInstance(boolean photos, int min, int max,
                                           boolean newProduct, long province, List<Province> provinceList) {

        Bundle args = new Bundle();

        args.putBoolean(EXTRA_PHOTOS, photos);
        args.putInt(EXTRA_MIN, min);
        args.putInt(EXTRA_MAX, max);
        args.putBoolean(EXTRA_NEW_PRODUCT, newProduct);
        args.putParcelableArrayList(EXTRA_PROVINCES, new ArrayList<Parcelable>(provinceList));
        args.putLong(EXTRA_PROVINCE, province);



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
            provinces = getArguments().getParcelableArrayList(EXTRA_PROVINCES);
            province = getArguments().getLong(EXTRA_PROVINCE);

            provinceSelected = new Province();
            provinceSelected.setId(Province.NO_PROVINCE);
            provinceSelected.setName(getString(R.string.select));
            provinces.add(0, provinceSelected);
        }
    }

    public void setFilterDialogCallback(FilterDialogCallback filterDialogCallback) {
        this.filterDialogCallback = filterDialogCallback;
    }

    private void fillProvinceSpinner(){
        final ProvinceSpinnerAdapter adapter = new ProvinceSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,
                provinces);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvince.setAdapter(adapter);

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    provinceSelected = adapter.getItem(position);
                } else {
                    provinceSelected = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btn_filter)
    public void onBtnFiltertClick(){

        int min = Integer.parseInt(etMin.getText().toString().length() > 0 ? etMin.getText().toString() : "0");
        int max = Integer.parseInt(etMax.getText().toString().length() > 0 ? etMax.getText().toString() : "0");
        boolean photos = swImage.isChecked();
        boolean isNew = swNew.isChecked();


        long prov = Province.NO_PROVINCE;
        if(provinceSelected != null){
            prov = provinceSelected.getId();
        }


        String error = "";
        boolean hasError = false;


        if (min > max  && min != 0){
            error += getString(R.string.extra_filter_max_range_error) + "\n";
            hasError = true;

        }

        if(min == max && min != 0){
            error += getString(R.string.extra_filter_equals_range_error) + "\n";
            hasError = true;
        }

        if(hasError){
            filterDialogCallback.onError(error);
        } else{
            boolean changed = min != this.min || max != this.max
                    || photos != this.photos
                    || isNew != this.newProduct
                    || prov != province;

            filterDialogCallback.onFilter(photos, min, max, isNew, prov, changed);
            dismiss();




        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.dialog_filter, null);

        ButterKnife.bind(this, v);
        setupUi();

        for(Province prov : provinces){
            if(prov.getId() == province){
                provinceSelected = prov;
            }
        }
        fillProvinceSpinner();


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

        int index = 0;
        for (Province prov : provinces){
            if(prov.getId() == province){
                spProvince.setSelection(index);
            }
            index ++;
        }
    }


    public interface FilterDialogCallback {
        void onFilter(boolean photos, int min, int max, boolean newProduct, long province, boolean changed);
        void onError(String error);
    }
}
