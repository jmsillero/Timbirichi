package com.timbirichi.eltimbirichi.presentation.view.activity;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseActivity;
import com.timbirichi.eltimbirichi.presentation.view.fragment.CategoryFragment;
import com.timbirichi.eltimbirichi.presentation.view.fragment.CoverPageFragment;
import com.timbirichi.eltimbirichi.presentation.view.fragment.FavoriteFragment;
import com.timbirichi.eltimbirichi.presentation.view.fragment.ProductFragment;
import com.timbirichi.eltimbirichi.presentation.view_model.CategoryViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.DatabaseViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.CategoryViewModelFactory;
import com.timbirichi.eltimbirichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CoverPageFragment coverPageFragment;
    FavoriteFragment favoriteFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    SubCategory catSelected;

    ProductFragment productFragment;

    String findText = null;

    public static final int CODE_DATABASE_UPDATE = 2003;

    @Inject
    CategoryViewModelFactory categoryViewModelFactory;
    CategoryViewModel categoryViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setResult(RESULT_OK);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catSelected = new SubCategory();
        catSelected.setId(SubCategory.CATEGORY_LASTED);

        initButterNife();
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.app_name));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupCategoryViewModel();
        categoryViewModel.getCategories();

        navigationView.setNavigationItemSelectedListener(this);

        openCoverPageFragment();
        updateMetaInformation();

    }

    private void setupCategoryViewModel(){
        categoryViewModel = ViewModelProviders.of(this, categoryViewModelFactory).get(CategoryViewModel.class);

        categoryViewModel.categories.observe(this, new Observer<Response<List<Category>>>() {
            @Override
            public void onChanged(@Nullable Response<List<Category>> listResponse) {
                switch (listResponse.status){
                    case LOADING:
                        // todo: Mostrar cargando todavia
                        break;

                    case SUCCESS:

                        break;

                    case ERROR:
                        showErrorDialog(getString(R.string.loading_category_error));
                        break;
                }
            }
        });
    }

    private void openCoverPageFragment(){
        coverPageFragment = CoverPageFragment.newInstance();
        coverPageFragment.setCoverPageCallback(new CoverPageFragment.CoverPageCallback() {
            @Override
            public void onOpenCategories() {
                openCategoryFragment(categoryViewModel.categories.getValue().data);
            }

            @Override
            public void openUpdateActivity() {
                openUpdateActivity();
            }

            @Override
            public void openDetailActivity(Product prod) {
                 MainActivity.this.openDetailActivity(prod);
            }

            @Override
            public void openCategorySelected(SubCategory cat) {
                 openProductsFragment(cat, null);
                 catSelected = cat;
            }

            @Override
            public void openLastNewProductFragment() {
                SubCategory category = new SubCategory();
                category.setId(SubCategory.CATEGORY_LASTED);
                openProductsFragment(category, null);
                catSelected = category;
            }

            @Override
            public void openFavorites() {
                openFavoriteFragment(null);
            }

        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, coverPageFragment, CoverPageFragment.TAG);
        ft.commit();
    }

    private void openCategoryFragment(List<Category> cats){
        CategoryFragment categoryFragment = CategoryFragment.newInstance(cats);
        categoryFragment.setCategoryFragmentCallback(new CategoryFragment.CategoryFragmentCallback() {
            @Override
            public void onCategorySelected(SubCategory category) {
                openProductsFragment(category, null);
                MainActivity.this.catSelected = catSelected;
            }
        });

        openFragment(categoryFragment, R.id.fragment_container, true);
    }


    private void openProductsFragment(SubCategory category, String findText){
        productFragment = ProductFragment.newInstance(category, findText);
        productFragment.setProductFragmentCallback(new ProductFragment.ProductFragmentCallback() {
            @Override
            public void onProductClick(Product prod) {
                openDetailActivity(prod);
            }
        });

        if(productFragment.isVisible()){
            openFragment(productFragment, R.id.fragment_container, false);
        }else{
            openFragment(productFragment, R.id.fragment_container, true);
        }


    }

    private void openFavoriteFragment(String findText){

        toolbar.getMenu().add(getString(R.string.clear))
                .setIcon(R.drawable.ic_clear)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        favoriteFragment = FavoriteFragment.newInstance(findText);
        openFragment(favoriteFragment, R.id.fragment_container, true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =   findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                openProductsFragment(catSelected, query);
                findText = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(findText != null && !findText.isEmpty() && newText.isEmpty()){
                    openProductsFragment(catSelected, null);
                    findText = null;
                }

                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(findText == null || findText.isEmpty()){
                    openProductsFragment(catSelected, null);
                    findText = null;
                }
                return false;
            }
        });


//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // todo: El update meta information lo puedo hacer en el coverPageFragment
    // todo: Y poner la informacion al final de la pagina...
    private void updateMetaInformation(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_DATABASE_UPDATE:
                if(resultCode == RESULT_OK){
                    updateMetaInformation();
                    openCoverPageFragment();
                }
                break;
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle products_navigation view item clicks here.
        int id = item.getItemId();
        switch (id){

            case R.id.nav_home:
                openCoverPageFragment();
                break;

            case R.id.nav_category:
                if(categoryViewModel.categories.getValue().status == Status.SUCCESS){
                    openCategoryFragment(categoryViewModel.categories.getValue().data);
                } else{
                    showErrorDialog(getString(R.string.loading_category_error));
                }

                break;

            case R.id.nav_update:
                openUpdateActivity();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    private void openDetailActivity(Product prod){
        Intent intent = new Intent(this, DetailActivity.class);
        Utils.productSelected = prod;
        startActivity(intent);
    }

    private void openUpdateActivity(){
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(UpdateActivity.EXTRA_EXIST_DATABASE, true);
        startActivityForResult(intent, CODE_DATABASE_UPDATE);
    }



}
