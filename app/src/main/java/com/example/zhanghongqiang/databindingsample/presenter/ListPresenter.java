package com.example.zhanghongqiang.databindingsample.presenter;



import com.example.zhanghongqiang.databindingsample.ui.BaseActivity;
import com.example.zhanghongqiang.databindingsample.view.IFListview;

/**
 * Created by zhanghongqiang on 16/3/25  上午10:20
 * ToDo:列表的适配器
 */
public abstract class ListPresenter<T> extends BasePresenter<IFListview> {
    /**
     * TODO 这里用是否用Activity待考证
     *
     * @param context
     * @param F
     */
    public ListPresenter(BaseActivity context, IFListview F) {
        super(context, F);
    }

    public abstract void reLoadData();

    public void refreshComplete() {
    }

}
