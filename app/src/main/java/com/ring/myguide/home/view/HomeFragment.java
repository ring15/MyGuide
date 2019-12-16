package com.ring.myguide.home.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ring.myguide.R;
import com.ring.myguide.base.BaseFragment;
import com.ring.myguide.entity.HomePage;
import com.ring.myguide.home.HomeContract;
import com.ring.myguide.home.presenter.HomePresenter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment<HomePresenter, HomeContract.View>
        implements HomeContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //刷新
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mHomeRecycler;
    //获取的城市名
    private String province = "天津";
    private HomeAdapter mAdapter;
    private HomePage mHomePage;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getIdResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void findView(View view) {
        mRefreshLayout = view.findViewById(R.id.refresh_home);
        mHomeRecycler = view.findViewById(R.id.recycler_home);
    }

    @Override
    protected void init() {
        mPresenter.getCity();
        mAdapter = new HomeAdapter(getContext());
        mAdapter.setPresenter(mPresenter);
        mAdapter.setCachePath(getActivity().getCacheDir().getPath());
        mHomeRecycler.setAdapter(mAdapter);
        mHomeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.getHomePage(province, getActivity().getCacheDir().getPath()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getHomePage(province, getActivity().getCacheDir().getPath());
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getHomePageSuccess(HomePage homePage) {
        mHomePage = homePage;
        mAdapter.setHomePage(homePage);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getHomePageFailed() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setCity(String city) {
        province = city;
    }

    public void changeCity(String city) {
        setCity(city);
        mPresenter.getHomePage(province, getActivity().getCacheDir().getPath());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
