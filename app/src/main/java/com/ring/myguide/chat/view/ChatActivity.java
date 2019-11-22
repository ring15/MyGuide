package com.ring.myguide.chat.view;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.ring.myguide.R;
import com.ring.myguide.base.BaseActivity;
import com.ring.myguide.chat.ChatContract;
import com.ring.myguide.chat.presenter.ChatPresenter;
import com.ring.myguide.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends BaseActivity<ChatPresenter, ChatContract.View>
        implements ChatContract.View {

    private static final String TAG = "ChatActivity";

    //请求数据条数
    private int pagesize = 15;

    //标题
    private TextView mTitleText;
    //刷新
    private SwipeRefreshLayout mRefreshLayout;
    //对话框列表
    private RecyclerView mRecyclerView;
    //文本输入框
    private EditText mMessageEdit;
    //没有对话时
    private LinearLayout mNoMessage;

    //传递过来的其他用户信息
    private User mOtherUser;
    //获取当前登录用户信息
    private User mUser;


    protected EMConversation conversation;
    private ExecutorService fetchQueue;

    private ChatAdapter mAdapter;

    @Override
    protected int getIdResource() {
        return R.layout.activity_chat;
    }

    @Override
    protected ChatPresenter getPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void findView() {
        mTitleText = findViewById(R.id.tv_title);
        mRefreshLayout = findViewById(R.id.layout_refresh);
        mRecyclerView = findViewById(R.id.recycler_message);
        mMessageEdit = findViewById(R.id.et_message);
        mNoMessage = findViewById(R.id.layout_no_message);
    }

    @Override
    protected void init() {
        mOtherUser = (User) getIntent().getSerializableExtra("user");
        mTitleText.setText(mOtherUser.getNickname());
        mAdapter = new ChatAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.init());
        fetchQueue = Executors.newSingleThreadExecutor();
//        onConversationInit();
        mPresenter.init();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_send:
                //发送消息
                mPresenter.sendMessage(mMessageEdit.getText().toString().trim());
                break;
        }
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUser(User user) {
        mUser = user;
        if (mOtherUser != null) {
            mAdapter.setUser(user);
            mAdapter.setOtherUser(mOtherUser);
            onConversationInit();
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setNoUser() {
        mNoMessage.setVisibility(View.VISIBLE);
        mRefreshLayout.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void sendTextMessage(String content) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, mOtherUser.getUserName());
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        EMClient.getInstance().chatManager().saveMessage(message);
        mMessageEdit.setText("");
        onConversationInit();
    }

    private void onConversationInit() {
        conversation = EMClient.getInstance().chatManager().getConversation(mOtherUser.getNickname(),
                EMConversation.EMConversationType.Chat,
                true);
        conversation.markAllMessagesAsRead();
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
        if (conversation != null && conversation.getAllMessages().size() > 0) {
            mRecyclerView.smoothScrollToPosition(conversation.getAllMessages().size() - 1);
            mPresenter.updateMessageList(mOtherUser,
                    ((EMTextMessageBody) conversation.getLastMessage().getBody()).getMessage(),
                    conversation.getLastMessage().getMsgTime());
            mAdapter.setEMMessages(conversation.getAllMessages());
            //更新数据you
            mAdapter.notifyDataSetChanged();
            mNoMessage.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mNoMessage.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
        }
//        fetchQueue.execute(() -> {
//            try {
//                EMClient.getInstance().chatManager().fetchHistoryMessages(
//                        mOtherUser.getUserName(), EMConversation.EMConversationType.Chat, pagesize, "");
////                conversation = EMClient.getInstance().chatManager().getConversation(mOtherUser.getNickname(), EMConversation.EMConversationType.Chat, true);
////                final List<EMMessage> msgs = conversation.getAllMessages();
////                int msgCount = msgs != null ? msgs.size() : 0;
////                if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
////                    String msgId = null;
////                    if (msgs != null && msgs.size() > 0) {
////                        msgId = msgs.get(0).getMsgId();
////                    }
////                    conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
//////                    mAdapter.setEMMessages(conversation.getAllMessages());
//////                    //更新数据
//////                    mAdapter.notifyDataSetChanged();
////                }
//            } catch (HyphenateException e) {
//                e.printStackTrace();
//            }
//        });
    }

    /**
     * 设置消息列表数据源
     */
    private void setMessageList() {
        Map<String, EMConversation> map = EMClient.getInstance().chatManager().getAllConversations();
        //获取当前用户发送的信息
        EMConversation conversation = map.get(mUser.getNickname());
        //获取聊天用户发送的信息
        EMConversation otherConversation = map.get(mOtherUser.getNickname());
        EMConversation emConversation = EMClient.getInstance().chatManager().getConversation(mOtherUser.getNickname(), EMConversation.EMConversationType.Chat, true);
        List<EMMessage> messages = new ArrayList<>(emConversation.getAllMessages());
//        if (conversation != null) {
//            messages.addAll(conversation.getAllMessages());
//        }
//        if (otherConversation != null) {
//            messages.addAll(otherConversation.getAllMessages());
//        }
        mAdapter.setEMMessages(messages);
        //更新数据
        mAdapter.notifyDataSetChanged();
    }

}
