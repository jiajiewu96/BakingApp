package com.example.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.AppExecutors;
import com.example.bakingapp.R;
import com.example.bakingapp.data.FavoritesViewModel;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.ui.adapters.IngredientsListAdapter;
import com.example.bakingapp.ui.adapters.StepAdapter;
import com.example.bakingapp.ui.fragmentInterfaces.CommonFragmentInterfaces;
import com.example.bakingapp.utils.Consts;
import com.example.bakingapp.widget.IngredientsWidgetProvider;

import java.util.ArrayList;

public class RecipeStepListFragment extends Fragment implements StepAdapter.StepSelectedListener {
    private CommonFragmentInterfaces mTitleInterface;
    private IngredientsListAdapter mIngredientsListAdapter;
    private StepAdapter mStepAdapter;
    private ImageView mFavoriteImageView;
    private FavoritesViewModel mFavoritesViewModel;
    private FragmentActivity mFragmentActivity;

    private OnStepClickedListener onStepClickedListener;
    private Recipe mRecipe;
    private Context mContext;
    private ImageView mPinImageView;

    public interface OnStepClickedListener {
        void onStepClicked(ArrayList<Step> steps, int position);
    }

    public RecipeStepListFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onStepClickedListener = (OnStepClickedListener) context;
            mTitleInterface = (CommonFragmentInterfaces) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " Must implement onStepClickedListener");
        }
        mContext = getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentActivity = getActivity();
        FavoritesViewModel.Factory factory = new FavoritesViewModel.Factory(mFragmentActivity.getApplication());
        mFavoritesViewModel = ViewModelProviders.of(mFragmentActivity,factory).get(FavoritesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ActionBar actionBar = ((AppCompatActivity) mFragmentActivity).getSupportActionBar();

        mFavoriteImageView = rootView.findViewById(R.id.iv_favorite_button);
        mPinImageView = rootView.findViewById(R.id.iv_pin_button);

        setButtonOnClicks();

        setupRecyclerViews(rootView);

        Bundle bundle = getArguments();
        if(bundle!=null){
            mRecipe = (Recipe) bundle.getParcelable(Consts.RECIPE_KEY);
            actionBar.show();
            mTitleInterface.onFragmentChangedListener(mRecipe.getName());
            mIngredientsListAdapter.setIngredients(mRecipe.getIngredients());
            mStepAdapter.setSteps(mRecipe.getSteps());
        }
        checkForRecipeInDB();
        return rootView;
    }

    private void setButtonOnClicks() {

        mPinImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), IngredientsWidgetProvider.class);

            }
        });

        mFavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecipe.getFavorite() != Consts.FLAG_IS_FAVORITED){
                    mRecipe.setFavorite(Consts.FLAG_IS_FAVORITED);
                    setFavoriteSelectedImage();
                    AppExecutors.getInstance().diskIO().execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    mFavoritesViewModel.addFavorite(mRecipe);
                                }
                            }
                    );
                }else{
                    mRecipe.setFavorite(Consts.FLAG_IS_NOT_FAVORITED);
                    setFavoriteUnselectedImage();
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mFavoritesViewModel.removeFavorite(mRecipe);
                        }
                    });
                }
            }
        });
    }

    private void checkForRecipeInDB() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(mFavoritesViewModel.checkIfRecipeInDb(mRecipe)){
                    mRecipe.setFavorite(Consts.FLAG_IS_FAVORITED);
                    setFavoriteSelectedImage();
                }else{
                    mRecipe.setFavorite(Consts.FLAG_IS_NOT_FAVORITED);
                    setFavoriteUnselectedImage();
                }
            }
        });
    }

    private void setupRecyclerViews(View rootView) {
        RecyclerView ingredientsRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_ingredients);
        RecyclerView stepsRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_steps);

        mIngredientsListAdapter = new IngredientsListAdapter();
        ingredientsRecycler.setAdapter(mIngredientsListAdapter);

        mStepAdapter = new StepAdapter(this);
        stepsRecycler.setAdapter(mStepAdapter);

        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity());
        ingredientsRecycler.setLayoutManager(ingredientsLayoutManager);
        stepsRecycler.setLayoutManager(stepsLayoutManager);

        ingredientsRecycler.setNestedScrollingEnabled(true);
        stepsRecycler.setNestedScrollingEnabled(true);
    }

    private void setFavoriteUnselectedImage() {
        mFavoriteImageView.setImageResource(R.drawable.ic_unfavorite);
    }

    private void setFavoriteSelectedImage() {
        mFavoriteImageView.setImageResource(R.drawable.ic_favorite);
    }

    @Override
    public void onStepSelected(ArrayList<Step> steps, int position) {
        onStepClickedListener.onStepClicked(steps, position);
    }
}
