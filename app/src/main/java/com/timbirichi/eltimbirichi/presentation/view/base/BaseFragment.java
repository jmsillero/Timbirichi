package com.timbirichi.eltimbirichi.presentation.view.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timbirichi.eltimbirichi.ElTimbirichiApplication;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.dagger.component.DaggerFragmentComponent;
import com.timbirichi.eltimbirichi.dagger.component.FragmentComponent;
import com.timbirichi.eltimbirichi.dagger.module.FragmentModule;

import butterknife.ButterKnife;


/**
 * Created by JM on 11/18/2017.
 */

public abstract class BaseFragment extends Fragment {
    protected View view;
    protected FragmentCallback fragmentCallback;

    public final static int PRODUCT_FRAGMENT = 0;
    public final static int OTHER_FRAGMENT = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), null);
        initInject();
        ButterKnife.bind(this, view);

        return view;
    }


    protected FragmentComponent getFragementComponent(){
        return DaggerFragmentComponent.builder()
                .applicationComponent(ElTimbirichiApplication.getComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    protected void openFragment(BaseFragment fragment, int id_container, boolean isBackStack){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(id_container, fragment);
        if (isBackStack){
            ft.addToBackStack(null);
        }
        ft.commit();

    }

    protected void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(R.string.app_name);

        builder.setIcon(R.drawable.ic_error);

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setFragmentCallback(FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }



    protected abstract  int getLayoutId();
    protected abstract  void initInject();

    public interface FragmentCallback{
        void onFragmentStart(int fragmentType);
        void onFindProducts(String text);
    }
}
