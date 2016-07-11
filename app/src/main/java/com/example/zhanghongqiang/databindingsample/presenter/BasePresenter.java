package com.example.zhanghongqiang.databindingsample.presenter;


import com.example.zhanghongqiang.databindingsample.ui.BaseActivity;

/**
 * Created by zhanghongqiang
 * Date:2015/12/17 Time:10:08
 * ToDo：代理的基类,
 */
public class BasePresenter<GV extends IFBaseView> {

    public BaseActivity context;
    protected GV F;

    /**
     * TODO 这里用是否用Activity待考证
     */

    public BasePresenter(BaseActivity context, GV F) {
        this.context = context;
        this.F = F;
    }
}
