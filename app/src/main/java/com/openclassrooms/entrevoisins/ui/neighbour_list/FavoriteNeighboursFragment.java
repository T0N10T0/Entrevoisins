package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Use the {@link FavoriteNeighboursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteNeighboursFragment extends Fragment {
    private List<Neighbour> mFavNeighbour;
    private NeighbourApiService mFavApiService;
    private RecyclerView mFavRecyclerView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FavoriteNeighbourFragment.
     */
    public static FavoriteNeighboursFragment newInstance() {
        FavoriteNeighboursFragment fragment = new FavoriteNeighboursFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mFavApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        Context context = view.getContext();
        mFavRecyclerView = (RecyclerView) view;
        mFavRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mFavRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initList();
        return view;
    }

    private void initList() {
        mFavNeighbour = mFavApiService.getFavoriteNeighbours();
        mFavRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mFavNeighbour));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button or favorite button when star is full
     *
     * @param event
     */
    @Subscribe
    public void onDeleteeNeighbourEvent(DeleteNeighbourEvent event) {
        mFavApiService.deleteNeighbour(event.neighbour);
        initList();
    }
}
