package com.example.weather_app.base;

import androidx.fragment.app.Fragment;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/*xutils加载网络数据的步骤
* 1.声明整体模块
*  2.执行网络请求操作
* */
public class BaseFragment extends Fragment implements Callback.CommonCallback<String>{
//Callback回调接口
    public void loadData(String path){
//        使用RequestParams将请求网址路径包装
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }
//    获取数据成功时，会回调的接口
    @Override
    public void onSuccess(String result) {

    }
//  获取数据失败时，会调用的接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }
//  取消请求时，会调用的接口
    @Override
    public void onCancelled(CancelledException cex) {

    }
//  请求完成后，会回调的接口
    @Override
    public void onFinished() {

    }
}
