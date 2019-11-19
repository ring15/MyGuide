package com.ring.myguide;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.NetUtils;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test_register:
                RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("user_name", "123");
                params.put("password", "123");
                service.doRegisterUser(params)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    String result = responseBody.string();
                                    Toast.makeText(TestActivity.this, result, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("result", e.getMessage());
                                Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.btn_test_login:
                RetrofitService service1 = RetrofitUtil.getInstance().createService(RetrofitService.class);
                LinkedHashMap<String, String> params1 = new LinkedHashMap<>();
                params1.put("user_name", "123");
                params1.put("password", "123");
                service1.doLogin(params1)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    String result = responseBody.string();
                                    Log.i("MainActivity", result);
                                    JSONObject root = JSON.parseObject(result);    //解析根节点
                                    String status = root.getString("status");   //解析状态
                                    if (status.equals("success")) {
                                        EMClient.getInstance().login("123", "123", new EMCallBack() {//回调
                                            @Override
                                            public void onSuccess() {
                                                EMClient.getInstance().groupManager().loadAllGroups();
                                                EMClient.getInstance().chatManager().loadAllConversations();
                                                Log.i("MainActivity", "登录聊天服务器成功！");
                                            }

                                            @Override
                                            public void onProgress(int progress, String status) {

                                            }

                                            @Override
                                            public void onError(int code, String message) {
                                                Log.d("main", "登录聊天服务器失败！");
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("result", e.getMessage());
                                Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.btn_test_send_msg:
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage("chat", "456");
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                break;
            case R.id.btn_test_logout:
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        Log.i("logout", "onSuccess");

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        Log.i("logout", "onProgress");

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.i("logout", "onError:" + message);

                    }
                });
                break;
        }
    }

    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.i("message", "getFrom:" + messages.get(0).getFrom());
            Log.i("message", "getMsgId:" + messages.get(0).getMsgId());
            Log.i("message", "getTo:" + messages.get(0).getTo());
            Log.i("message", "getUserName:" + messages.get(0).getUserName());
            Log.i("message", "getMsgTime:" + messages.get(0).getMsgTime());
            if (messages.get(0).getBody() instanceof EMTextMessageBody) {
                Log.i("message", "getMsg:" + ((EMTextMessageBody) messages.get(0).getBody()).getMessage());
            }
            //getFrom:456
            //getMsgId:670600474121996292
            //getTo:123
            //getUserName:456
            //getMsgTime:1573701140025
            //getBody:txt:"chat"
            //getMsg:chat
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.i("message", "onCmdMessageReceived");
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.i("message", "onMessageRead");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            Log.i("message", "onMessageDelivered");
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
            Log.i("message", "onMessageRecalled");
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.i("message", "onMessageChanged");
        }
    };


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        Log.i("connect", "USER_REMOVED");
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        Log.i("connect", "USER_LOGIN_ANOTHER_DEVICE");
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(TestActivity.this)) {
                            Log.i("connect", "connect_error");
                            //连接不到聊天服务器
                        } else {
                            Log.i("connect", "net_error");
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
