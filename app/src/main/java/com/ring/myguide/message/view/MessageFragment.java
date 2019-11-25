package com.ring.myguide.message.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseFragment;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;
import com.ring.myguide.message.MessageContract;
import com.ring.myguide.message.presenter.MessagePresenter;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends BaseFragment<MessagePresenter, MessageContract.View>
        implements MessageContract.View , View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //标为已读按钮
    private TextView mReadText;
    //全部删除按钮
    private TextView mDeleteText;
    //没有登录时
    private LinearLayout mNotLoginLayout;
    //没有消息时
    private LinearLayout mNoMessageLayout;
    //刷新
    private SwipeRefreshLayout mRefreshLayout;
    //对话列表
    private RecyclerView mMessageRecyclerView;

    private MessageAdapter mMessageAdapter;


    private OnFragmentInteractionListener mListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.init();
        mPresenter.getMessageList();
    }

    @Override
    protected int getIdResource() {
        return R.layout.fragment_message;
    }

    @Override
    protected void findView(View view) {
        mReadText = view.findViewById(R.id.tv_read);
        mDeleteText = view.findViewById(R.id.tv_delete);
        mNotLoginLayout = view.findViewById(R.id.layout_not_login);
        mNoMessageLayout = view.findViewById(R.id.layout_no_message);
        mRefreshLayout = view.findViewById(R.id.refresh_message);
        mMessageRecyclerView = view.findViewById(R.id.recycler_message);
    }

    @Override
    protected void init() {
        mMessageAdapter = new MessageAdapter(getContext());
        mMessageRecyclerView.setAdapter(mMessageAdapter);
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.getMessageList());
        mReadText.setOnClickListener(this);
        mDeleteText.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_read:
                //所有未读消息数清零
                EMClient.getInstance().chatManager().markAllConversationsAsRead();
                mPresenter.getMessageList();
                setRedPoitVisible(Uri.parse("gone"));
                break;
            case R.id.tv_delete:
                //删除对话列表
                mPresenter.deleteMessageList();
                //所有未读消息数清零
                EMClient.getInstance().chatManager().markAllConversationsAsRead();
                mPresenter.getMessageList();
                setRedPoitVisible(Uri.parse("gone"));
                break;
        }
    }

    @Override
    protected MessagePresenter getPresenter() {
        return new MessagePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void setMenuClickable(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setRedPoitVisible(Uri uri) {
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

    public void onMessageReceived(List<EMMessage> messages) {
        //收到消息
        mPresenter.updateMessageList(messages);
    }

    public void onCmdMessageReceived(List<EMMessage> messages) {
        //收到透传消息
    }

    public void onMessageRead(List<EMMessage> messages) {
        //收到已读回执
    }

    public void onMessageDelivered(List<EMMessage> message) {
        //收到已送达回执
    }

    public void onMessageRecalled(List<EMMessage> messages) {
        //消息被撤回
    }

    public void onMessageChanged(EMMessage message, Object change) {
        //消息状态变动
    }

    @Override
    public void setUser(User user) {
        mNotLoginLayout.setVisibility(View.GONE);
        setMenuClickable(Uri.parse("true"));

    }

    @Override
    public void setNoUser() {
        mReadText.setClickable(false);
        mDeleteText.setClickable(false);
        mNoMessageLayout.setVisibility(View.GONE);
        mNotLoginLayout.setVisibility(View.VISIBLE);
        setMenuClickable(Uri.parse("false"));
    }

    @Override
    public void setMessageList(LinkedList<MessageList> messageList) {
        mMessageAdapter.setMessageLists(messageList);
        mMessageAdapter.notifyDataSetChanged();
        mNoMessageLayout.setVisibility(View.GONE);
        mNotLoginLayout.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setNoMessageList() {
        mNoMessageLayout.setVisibility(View.VISIBLE);
        mNotLoginLayout.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
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
