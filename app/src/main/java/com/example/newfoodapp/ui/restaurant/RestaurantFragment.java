package com.example.newfoodapp.ui.restaurant;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newfoodapp.Adapter.MyRestaurantAdapter;
import com.example.newfoodapp.EventBus.CounterCartEvent;
import com.example.newfoodapp.EventBus.HideFABCart;
import com.example.newfoodapp.EventBus.MenuInflateEvent;
import com.example.newfoodapp.R;

import org.greenrobot.eventbus.EventBus;

public class RestaurantFragment extends Fragment {

    private RestaurantViewModel mViewModel;
    Unbinder unbinder;
    @BindView(R.id.recycler_restaurant)
    RecyclerView recycler_restaurant;
    AlertDialog dialog;
    LayoutAnimationController layoutAnimationController;
    MyRestaurantAdapter adapter;

    public static RestaurantFragment newInstance() {
        return new RestaurantFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        View root = inflater.inflate(R.layout.fragment_restaurant, container, false);
        unbinder = ButterKnife.bind(this,root);
        initViews();

        mViewModel.getMessageError().observe(getViewLifecycleOwner(),message ->{

            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        mViewModel.getRestaurantListMutable().observe(getViewLifecycleOwner(),restaurantModelList -> {

            dialog.dismiss();
            adapter = new MyRestaurantAdapter(getContext(),restaurantModelList);
            recycler_restaurant.setAdapter(adapter);
            recycler_restaurant.setLayoutAnimation(layoutAnimationController);

        });

        return  root;
    }

    private void initViews() {

        EventBus.getDefault().postSticky(new HideFABCart(true)); //Hide when user goes back to this fragment
        setHasOptionsMenu(true);
        dialog = new AlertDialog.Builder(getContext()).setCancelable(false)
                .setMessage("Please wait ....").create();
        dialog.show();
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recycler_restaurant.setLayoutManager(linearLayoutManager);
        recycler_restaurant.addItemDecoration(new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation()));

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().postSticky(new CounterCartEvent(true));
        EventBus.getDefault().postSticky(new MenuInflateEvent(false));
    }
}