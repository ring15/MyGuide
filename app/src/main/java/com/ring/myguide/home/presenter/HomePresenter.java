package com.ring.myguide.home.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.HomePage;
import com.ring.myguide.home.HomeContract;
import com.ring.myguide.home.model.HomeModel;
import com.ring.myguide.home.view.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ring on 2019/12/2.
 */
public class HomePresenter extends HomeContract.Presenter {

    private static final String TAG = "HomePresenter";

    private HomeContract.Model mModel;
    private String mSavePath;
    private HomePage mHomePage;
    private List<String> mImgPath = new ArrayList<>();

    public HomePresenter() {
        mModel = new HomeModel();
    }

    @Override
    public void getHomePage(String province, String savePath) {
        mSavePath = savePath;
        mModel.requestHomePage(province, new CallbackListener<HomePage>() {
            @Override
            public void onSuccess(HomePage data) {
                if (data != null) {
                    List<String> imgsPath = new ArrayList<>();
                    mHomePage = data;
//                    List<Banner> banners = data.getBanners();
//                    List<Post> foodRecommends = data.getFoodPost();
//                    List<Post> placeRecommends = data.getPlacePost();
//                    List<Post> userRecommends = data.getUserPost();
//                    if (banners != null && banners.size() > 0) {
//                        for (Banner banner : banners) {
//                            imgsPath.add(banner.getImg());
//                        }
//                    }
//                    if (foodRecommends != null && foodRecommends.size() > 0) {
//                        for (Post post : foodRecommends) {
//                            imgsPath.add(post.getAuthor().getUserImg());
//                        }
//                    }
//                    if (placeRecommends != null && placeRecommends.size() > 0) {
//                        for (Post post : placeRecommends) {
//                            imgsPath.add(post.getAuthor().getUserImg());
//                        }
//                    }
//                    if (userRecommends != null && userRecommends.size() > 0) {
//                        for (Post post : userRecommends) {
//                            imgsPath.add(post.getAuthor().getUserImg());
//                        }
//                    }
//                    if (imgsPath.size() > 0) {
//                        requestImg(imgsPath, System.currentTimeMillis(), 0);
//                    } else {
                    if (isViewAttached()) {
                        mView.get().getHomePageSuccess(data);
                    }
//                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().getHomePageFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    /**
     * 设置当前城市（缓存存在则从缓存中读取，不存在进行下一步操作，现在是直接赋值"天津"，也可以跳转到定位）
     */
    @Override
    public void getCity() {
        String city = mModel.getCityFromCache();
        if (isViewAttached()) {
            if (!TextUtils.isEmpty(city)) {
                mView.get().setCity(city);
            } else {
                mView.get().setCity("天津");
            }
        }
    }

    @Override
    public void requestImg(String img, String name, String savePath, HomeAdapter.RequestImgListener listener) {
        RequestImgModel model = new RequestImgModel();
        model.requestImg(img, savePath, name, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                listener.onSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, throwable.getMessage());
                listener.onFailed();
            }
        });
    }

//    private void requestImg(List<String> paths, long time, int i) {
//        if (paths != null && paths.size() > 0) {
//            String s = paths.get(0);
//            RequestImgModel model = new RequestImgModel();
//            model.requestImg(s, mSavePath, time + "_" + i + ".jpg", new CallbackListener<String>() {
//                @Override
//                public void onSuccess(String data) {
//                    mImgPath.add(data);
//                    paths.remove(s);
//                    requestImg(paths, time, i + 1);
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    Log.i(TAG, throwable.getMessage());
//                    mImgPath.add("");
//                    paths.remove(s);
//                    requestImg(paths, time, i + 1);
//                }
//            });
//
//        } else {
//            int n = 0;
//            List<Banner> banners = mHomePage.getBanners();
//            List<Post> foodRecommends = mHomePage.getFoodPost();
//            List<Post> placeRecommends = mHomePage.getPlacePost();
//            List<Post> userRecommends = mHomePage.getUserPost();
//            if (banners != null && banners.size() > 0) {
//                for (Banner banner : banners) {
//                    banner.setImgPath(mImgPath.get(n));
//                    n++;
//                }
//            }
////            if (foodRecommends != null && foodRecommends.size() > 0) {
////                for (Post post : foodRecommends) {
////                    post.getAuthor().setUserImgPaht(mImgPath.get(n));
////                    n++;
////                }
////            }
////            if (placeRecommends != null && placeRecommends.size() > 0) {
////                for (Post post : placeRecommends) {
////                    post.getAuthor().setUserImgPaht(mImgPath.get(n));
////                    n++;
////                }
////            }
////            if (userRecommends != null && userRecommends.size() > 0) {
////                for (Post post : userRecommends) {
////                    post.getAuthor().setUserImgPaht(mImgPath.get(n));
////                    n++;
////                }
////            }
//            if (isViewAttached()) {
//                mView.get().getHomePageSuccess(mHomePage);
//            }
//        }
//
//    }

}
