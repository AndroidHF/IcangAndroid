package com.buycolle.aicang.api;

import android.content.Intent;

import com.buycolle.aicang.MainApplication;
import com.buycolle.aicang.api.callback.ResultCallback;
import com.buycolle.aicang.api.request.OkHttpRequest;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.util.PMRsaUtil;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.KLog;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 16/1/15.
 */
public class ApiClient {
    private MainApplication mainApp;

    public ApiClient(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    private Map<String, String> getApiObj(JSONObject data, String tag) {
        Map<String, String> mps = new HashMap<>();
        mps.put("req_from", "mj-app");
        mps.put("data", RAS(data));
        KLog.d(tag + "--传的数据--", data.toString());
        return mps;
    }

    /**
     * 加密
     *
     * @param data
     * @return
     */
    private String RAS(JSONObject data) {
        PMRsaUtil pmRsaUtil = PMRsaUtil.getInstance();
        String request = "";
        try {
            request = pmRsaUtil.pubKeyEncrypt(data.toString());
            request = request.replaceAll("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }


    private void post(final ApiCallback callback, String url, JSONObject jsonObject, final String tag) {
        new OkHttpRequest.Builder()
                .url(url)
                .tag(tag)
                .params(getApiObj(jsonObject, tag))
                .post(new ResultCallback<String>() {
                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        callback.onApiStart();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        KLog.e(tag + "--onError--", e.getMessage());
                        callback.onApiFailure(request, e);
                    }

                    @Override
                    public void onResponse(String response) {
                        String resultObj = RASToJsonStr(response);
                        KLog.json(tag + "--onResponse--", resultObj);
                        try {
                            JSONObject finalResult = new JSONObject(resultObj);
                            if (finalResult != null) {
                                if (40401 == finalResult.getInt("status")) {
                                    UIHelper.t(MainApplication.getInstance(), "登录超时，请重新登录");
                                    Intent intent = new Intent(MainApplication.getInstance(), LoginActivity.class);
                                    intent.putExtra("isDoubleLogin", true);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MainApplication.getInstance().startActivity(intent);
                                } else if (-1 == finalResult.getInt("status")) {
                                    callback.onApiSuccess(resultObj);
                                } else {
                                    callback.onApiSuccess(resultObj);
                                }
                            }
                        } catch (JSONException e) {
                            UIHelper.t(MainApplication.getInstance(), "服务器返回解析错误");
                            e.printStackTrace();
                        }
                    }

                });

    }

    /**
     * 解密
     *
     * @param data
     * @return
     */
    private String RASToJsonStr(String data) {
        PMRsaUtil pmRsaUtil = PMRsaUtil.getInstance();
        String reSult = "";
        try {
            reSult = pmRsaUtil.pubKeyDecrypt(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reSult;
    }

    /**
     * 获取短信验证码
     *
     * @param data
     * @param callback
     */
    public void login_getphonecheckcodebyordinaryuser(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.LOGIN_GETPHONECHECKCODEBYORDINARYUSER, data, "获取短信验证码");
    }

    /**
     * app注册
     *
     * @param data
     * @param callback
     */
    public void login_register2ordinaryuser(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.LOGIN_REGISTER2ORDINARYUSER, data, "app注册");
    }

    /**
     * app登录
     *
     * @param data
     * @param callback
     */
    public void login_byappordinaryuser(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.LOGIN_BYAPPORDINARYUSER, data, "app登录");
    }

    /**
     * 忘记密码
     *
     * @param data
     * @param callback
     */
    public void login_forgetloginpwd(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.LOGIN_FORGETLOGINPWD, data, "忘记密码");
    }


    /**
     * 拍品商品状态
     *
     * @param data
     * @param callback
     */
    public void dirtionary_getproductstatuslistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.DIRTIONARY_GETPRODUCTSTATUSLISTBYAPP, data, "拍品商品状态");
    }

    /**
     * 拍品违反类型
     *
     * @param data
     * @param callback
     */
    public void dirtionary_getproductwftypelistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.DIRTIONARY_GETPRODUCTWFTYPELISTBYAPP, data, "拍品违反类型");
    }

    /**
     * 热门搜索
     *
     * @param data
     * @param callback
     */
    public void dirtionary_gethotsearchlistbyapp(JSONObject data, ApiCallback callback, String tag) {
        post(callback, AppUrl.DIRTIONARY_GETHOTSEARCHLISTBYAPP, data, "热门搜索" + "_" + tag);
    }

    /**
     * 获取拍品一级分类
     *
     * @param data
     * @param callback
     */
    public void productcategory_gettoplistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTCATEGORY_GETTOPLISTBYAPP, data, "获取拍品一级分类");
    }

    /**
     * 获取拍品下级分类
     *
     * @param data
     * @param callback
     */
    public void productcategory_getchildlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTCATEGORY_GETCHILDLISTBYAPP, data, "获取拍品下级分类");
    }

    /**
     * 获取个人信息
     *
     * @param data
     * @param callback
     */
    public void appuser_getinfosbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPUSER_GETINFOSBYAPP, data, " 获取个人信息");
    }

    /**
     * 修改个人信息
     *
     * @param data
     * @param callback
     */
    public void appuser_updateinfosbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPUSER_UPDATEINFOSBYAPP, data, " 修改个人信息");
    }

    /**
     * 申请成为卖家
     *
     * @param data
     * @param callback
     */
    public void appuser_submitsupplysellerbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPUSER_SUBMITSUPPLYSELLERBYAPP, data, " 申请成为卖家");
    }

    /**
     * 退出
     *
     * @param data
     * @param callback
     */
    public void login_logoutplat(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.LOGIN_LOGOUTPLAT, data, " 退出");
    }

    /**
     * 发布拍品
     *
     * @param data
     * @param callback
     */
    public void product_submitByApp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_SUBMITBYAPP, data, " 发布拍品");
    }

    /**
     * 审核状态
     *
     * @param data
     * @param callback
     */
    public void product_getCenterCheckListByApp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCENTERCHECKLISTBYAPP, data, " 审核状态");
    }

    /**
     * 拍卖中
     *
     * @param data
     * @param callback
     */
    public void product_getcenteringlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCENTERINGLISTBYAPP, data, "  拍卖中");
    }

    /**
     * 流拍
     *
     * @param data
     * @param callback
     */
    public void product_getcenterendfaillistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCENTERENDFAILLISTBYAPP, data, "流拍");
    }

    /**
     * 取消拍卖
     *
     * @param data
     * @param callback
     */
    public void product_undo(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_UNDO, data, "  取消拍卖");
    }

    /**
     * 已卖出
     *
     * @param data
     * @param callback
     */
    public void product_getcenterendlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCENTERENDLISTBYAPP, data, "  已卖出");
    }

    /**
     * 获取快递公司列表
     *
     * @param data
     * @param callback
     */
    public void express_getlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.EXPRESS_GETLISTBYAPP, data, "  获取快递公司列表");
    }

    /**
     * 提交发货信息
     *
     * @param data
     * @param callback
     */
    public void winproductorder_submitexpressinfosbysellerapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WINPRODUCTORDER_SUBMITEXPRESSINFOSBYSELLERAPP, data, "  提交发货信息");
    }

    /**
     * 顶部广告
     *
     * @param data
     * @param callback
     */
    public void topRecom_getRecomList(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.TOPRECOM_GETRECOMLIST, data, "  顶部广告");
    }

    /**
     * 顶部广告
     *
     * @param data
     * @param callback
     */
    public void appbanner_getlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPBANNER_GETLISTBYAPP, data, "  顶部广告");
    }

    /**
     * 拍卖会顶部广告
     *
     * @param data
     * @param callback
     */
    public void appbanner_getcarouselphotobyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPBANNER_GETCAROUSELPHOTOBYAPP, data, "拍卖会顶部广告");
    }

    /**
     * 首页拍品列表
     *
     * @param data
     * @param callback
     */
    public void product_getnormallistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETNORMALLISTBYAPP, data, "首页拍品列表");
    }

    /**
     * 首页商品筛选
     * @param data
     * @param callback
     */
    public void product_filter_list_by_app(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.GET_FILTER_AND_SORT_LIST_BY_APP, data, "首页商品筛选");
    }

    /**
     * 拍品详情
     *
     * @param data
     * @param callback
     */
    public void product_getpreauctdetailidbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETPREAUCTDETAILIDBYAPP, data, "拍品详情");
    }

    /**
     * 竞拍
     *
     * @param data
     * @param callback
     */
    public void productjpfollow_submitbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTJPFOLLOW_SUBMITBYAPP, data, "竞拍");
    }

    /**
     * 竞拍--正在拍卖
     *
     * @param data
     * @param callback
     */
    public void product_getauctinglistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETAUCTINGLISTBYAPP, data, "竞拍--正在拍卖");
    }

    /**
     * 竞拍--即将开始
     *
     * @param data
     * @param callback
     */
    public void product_getauctreadylistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETAUCTREADYLISTBYAPP, data, "竞拍--即将开始");
    }

    /**
     * 竞拍--已结束
     *
     * @param data
     * @param callback
     */
    public void product_getauctendlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETAUCTENDLISTBYAPP, data, "竞拍--已结束");
    }

    /**
     * 举报拍品
     *
     * @param data
     * @param callback
     */
    public void proudctreport_submitbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PROUDCTREPORT_SUBMITBYAPP, data, "举报拍品");
    }

    /**
     * 获取个人收货地址信息
     *
     * @param data
     * @param callback
     */
    public void userreceipt_getlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.USERRECEIPT_GETLISTBYAPP, data, "获取个人收货地址信息");
    }

    /**
     * 修改收货地址
     *
     * @param data
     * @param callback
     */
    public void userreceipt_saveorupdate(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.USERRECEIPT_SAVEORUPDATE, data, "修改收货地址");
    }

    /**
     * 我买到的--拍卖中
     *
     * @param data
     * @param callback
     */
    public void product_getsjoininglistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETSJOININGLISTBYAPP, data, "我买到的--拍卖中");
    }

    /**
     * 我买到的--已落拍
     *
     * @param data
     * @param callback
     */
    public void product_getsjoinsucclistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETSJOINSUCCLISTBYAPP, data, "我买到的--已落拍");
    }

    /**
     * 我买到的--未拍到
     *
     * @param data
     * @param callback
     */
    public void product_getsjoinfaillistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETSJOINFAILLISTBYAPP, data, "我买到的--未拍到");
    }

    /**
     * 个人中心--公告
     *
     * @param data
     * @param callback
     */
    public void publicnotice_getlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PUBLICNOTICE_GETLISTBYAPP, data, "个人中心--公告");
    }

    /**
     * 交易流程文章
     *
     * @param data
     * @param callback
     */
    public void copywriter_getjiaoyilcbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COPYWRITER_GETJIAOYILCBYAPP, data, "交易流程文章");
    }

    /**
     * 使用帮助说明
     *
     * @param data
     * @param callback
     */
    public void writer_getusehelpbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WRITER_GETUSEHELPBYAPP, data, "使用帮助说明");
    }

    /**
     * 关于我们
     *
     * @param data
     * @param callback
     */
    public void copywriter_getaboutusbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COPYWRITER_GETABOUTUSBYAPP, data, "关于我们");
    }

    /**
     * 发货指导
     *
     * @param data
     * @param callback
     */
    public void writer_getfahuozdbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WRITER_GETFAHUOZDBYAPP, data, "发货指导");
    }

    /**
     * 成为卖家--常见问题
     *
     * @param data
     * @param callback
     */
    public void commonquestion_getsellerlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONQUESTION_GETSELLERLISTBYAPP, data, "成为卖家--常见问题");
    }

    /**
     * 拍卖协议（竞拍规则）
     *
     * @param data
     * @param callback
     */
    public void writer_getpaimaixybyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WRITER_GETPAIMAIXYBYAPP, data, "拍卖协议（竞拍规则）");
    }

    /**
     * 拍卖详情--常见问题
     *
     * @param data
     * @param callback
     */
    public void commonquestion_getpaipinglistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONQUESTION_GETPAIPINGLISTBYAPP, data, "拍卖详情--常见问题");
    }

    /**
     * 注册协议
     *
     * @param data
     * @param callback
     */
    public void writer_getregisterbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WRITER_GETREGISTERBYAPP, data, "注册协议");
    }

    /**
     * 用户协议
     *
     * @param data
     * @param callback
     */
    public void writer_getuseragreerbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WRITER_GETUSERAGREERBYAPP, data, "用户协议");
    }

    /**
     * 身份证示例图像
     *
     * @param data
     * @param callback
     */
    public void writer_getidentitycardmodelbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WRITER_GETIDENTITYCARDMODELBYAPP, data, "身份证示例图像");
    }

    /**
     * 保存为草稿提交晒物
     *
     * @param data
     * @param callback
     */
    public void show_submittoreadybyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_SUBMITTOREADYBYAPP, data, "保存为草稿提交晒物");
    }

    /**
     * 发布晒物
     *
     * @param data
     * @param callback
     */
    public void show_submitbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_SUBMITBYAPP, data, "发布晒物");
    }

    /**
     * 重新发布晒物
     *
     * @param data
     * @param callback
     */
    public void show_resubmitbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_RESUBMITBYAPP, data, "重新发布晒物");
    }

    /**
     * 我的晒物--通过列表
     *
     * @param data
     * @param callback
     */
    public void show_getownercentersucclistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_GETOWNERCENTERSUCCLISTBYAPP, data, "我的晒物--通过列表");
    }

    /**
     * 我的晒物--未通过列表
     *
     * @param data
     * @param callback
     */
    public void show_getownercenterfaillistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_GETOWNERCENTERFAILLISTBYAPP, data, "我的晒物--未通过列表");
    }

    /**
     * 我的晒物--草稿列表
     *
     * @param data
     * @param callback
     */
    public void show_getownercenterreadylistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_GETOWNERCENTERREADYLISTBYAPP, data, "我的晒物--草稿列表");
    }

    /**
     * 首页晒物列表
     *
     * @param data
     * @param callback
     */
    public void show_getlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_GETLISTBYAPP, data, "首页晒物列表");
    }

    /**
     * 点赞/取消晒物
     *
     * @param data
     * @param callback
     */
    public void commonnote_clickzshare(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONNOTE_CLICKZSHARE, data, "点赞/取消晒物");
    }

    /**
     * 获取该拍品的问与答
     *
     * @param data
     * @param callback
     */
    public void product_getcurandtotalcommentinfosbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCURANDTOTALCOMMENTINFOSBYAPP, data, "获取该拍品的问与答");
    }

    /**
     * 提交问题或回答
     *
     * @param data
     * @param callback
     */
    public void commoncomment_submitforproductbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONCOMMENT_SUBMITFORPRODUCTBYAPP, data, "提交问题或回答");
    }

    /**
     * 我的问询
     *
     * @param data
     * @param callback
     */
    public void commoncomment_getselfsendlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONCOMMENT_GETSELFSENDLISTBYAPP, data, "我的问询");
    }

    /**
     * 我收到的问询
     *
     * @param data
     * @param callback
     */
    public void commoncomment_getselfreceivelistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONCOMMENT_GETSELFRECEIVELISTBYAPP, data, "我收到的问询");
    }

    /**
     * 根据ID获取详情
     *
     * @param data
     * @param callback
     */
    public void show_getdetailidbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_GETDETAILIDBYAPP, data, "根据ID获取详情");
    }

    /**
     * 提交问题或回答
     *
     * @param data
     * @param callback
     */
    public void commoncomment_submitforshowbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONCOMMENT_SUBMITFORSHOWBYAPP, data, "提交问题或回答");
    }

    /**
     * 提交意见反馈
     *
     * @param data
     * @param callback
     */
    public void opinion_submitbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.OPINION_SUBMITBYAPP, data, "提交意见反馈");
    }

    /**
     * 获取推送设置
     *
     * @param data
     * @param callback
     */
    public void userpushcfg_getbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.USERPUSHCFG_GETBYAPP, data, "获取推送设置");
    }

    /**
     * 设置推送配置
     *
     * @param data
     * @param callback
     */
    public void userpushcfg_setbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.USERPUSHCFG_SETBYAPP, data, " 设置推送配置");
    }

    /**
     * 收藏/关注拍品
     *
     * @param data
     * @param callback
     */
    public void commonnote_colproduct(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONNOTE_COLPRODUCT, data, "收藏/关注拍品");
    }

    /**
     * 收藏/关注拍品 --拍卖会的
     *
     * @param data
     * @param callback
     */
    public void commonnote_prproduct(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONNOTE_PRPRODUCT, data, "收藏/关注拍品--拍卖会的");
    }

    /**
     * 收藏/关注取消卖家
     *
     * @param data
     * @param callback
     */
    public void commonnote_colseller(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONNOTE_COLSELLER, data, "收藏/关注取消卖家");
    }

    /**
     * 收藏/关注取消晒物
     *
     * @param data
     * @param callback
     */
    public void commonnote_colshare(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONNOTE_COLSHARE, data, "收藏/关注取消晒物");
    }

    /**
     * 用户关注拍品的列表
     *
     * @param data
     * @param callback
     */
    public void product_getcollistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCOLLISTBYAPP, data, "用户关注拍品的列表");
    }

    /**
     * 我关注的晒物列表
     *
     * @param data
     * @param callback
     */
    public void show_getcollistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SHOW_GETCOLLISTBYAPP, data, "我关注的晒物列表");
    }

    /**
     * 获取关注收藏卖家列表
     *
     * @param data
     * @param callback
     */
    public void appuser_getcollistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPUSER_GETCOLLISTBYAPP, data, "获取关注收藏卖家列表");
    }

    /**
     * 更新极光推送ID
     *
     * @param data
     * @param callback
     */
    public void appuser_updatepushtoken(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPUSER_UPDATEPUSHTOKEN, data, "更新极光推送ID");
    }

    /**
     * 去支付
     *
     * @param data
     * @param callback
     */
    public void winproductorder_getprepayinfosbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WINPRODUCTORDER_GETPREPAYINFOSBYAPP, data, "去支付");
    }

    /**
     * 获取支付宝订单信息
     *
     * @param data
     * @param callback
     */
    public void payrecord_buildaliprepay(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PAYRECORD_BUILDALIPREPAY, data, "获取支付宝订单信息");
    }

    /**
     * 顶--晒物评论
     *
     * @param data
     * @param callback
     */
    public void commoncomment_totopshowcommentbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.COMMONCOMMENT_TOTOPSHOWCOMMENTBYAPP, data, "顶--晒物评论");
    }

    /**
     * 获取出品人全部评价
     *
     * @param data
     * @param callback
     */
    public void productevaluate_getlistallevaluate(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTEVALUATE_GETLISTALLEVALUATE, data, "获取出品人全部评价");
    }

    /**
     * 获取出品人好评评价
     *
     * @param data
     * @param callback
     */
    public void productevaluate_getlistgoodevaluate(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTEVALUATE_GETLISTGOODEVALUATE, data, " 获取出品人好评评价");
    }

    /**
     * 获取出品人差评评价
     *
     * @param data
     * @param callback
     */
    public void productevaluate_getlistbadevaluate(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTEVALUATE_GETLISTBADEVALUATE, data, " 获取出品人差评评价");
    }

    /**
     * 确认收货
     *
     * @param data
     * @param callback
     */
    public void winproductorder_surereceipt(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WINPRODUCTORDER_SURERECEIPT, data, " 确认收货");
    }

    /**
     * 提交发表评价
     *
     * @param data
     * @param callback
     */
    public void productevaluate_submitwinorder(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTEVALUATE_SUBMITWINORDER, data, " 提交发表评价");
    }

    /**
     * 卖家发布的拍品
     *
     * @param data
     * @param callback
     */
    public void product_getcursellerpushlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCURSELLERPUSHLISTBYAPP, data, "卖家发布的拍品");
    }

    /**
     * 拍品竞拍记录列表
     *
     * @param data
     * @param callback
     */
    public void productjpfollow_getcurproducthislist(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCTJPFOLLOW_GETCURPRODUCTHISLIST, data, "拍品竞拍记录列表");
    }

    /**
     * 微信支付统一下单
     *
     * @param data
     * @param callback
     */
    public void payrecord_buildwxprepay(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PAYRECORD_BUILDWXPREPAY, data, "微信支付统一下单");
    }

    /**
     * 根据ID获取拍品发布信息
     *
     * @param data
     * @param callback
     */
    public void product_gethissubmitinfosbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETHISSUBMITINFOSBYAPP, data, "根据ID获取拍品发布信息");
    }

    /**
     * 获取用户的个人消费和营业额信息
     *
     * @param data
     * @param callback
     */
    public void appuser_getusercostinfosbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPUSER_GETUSERCOSTINFOSBYAPP, data, "获取用户的个人消费和营业额信息");
    }

    /**
     * 根据ID获取收货物流信息
     *
     * @param data
     * @param callback
     */
    public void winproductorder_getreceiptinfosbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.WINPRODUCTORDER_GETRECEIPTINFOSBYAPP, data, "根据ID获取收货物流信息");
    }

    /**
     * 获取成为卖家banner列表
     *
     * @param data
     * @param callback
     */
    public void appbanner_getsellerlistbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.APPBANNER_GETSELLERLISTBYAPP, data, "获取成为卖家banner列表");
    }

    /**
     * 获取感叹号信息
     *
     * @param data
     * @param callback
     */
    public void jpushrecord_gettipbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.JPUSHRECORD_GETTIPBYAPP, data, "获取感叹号信息");
    }

    /**
     * 更新叹号信息（设置已读）
     *
     * @param data
     * @param callback
     */
    public void jpushrecord_updatetipbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.JPUSHRECORD_UPDATETIPBYAPP,data, " 更新叹号信息（设置已读）");
    }

    /**
     * 个人中心更新小红点
     */
    public void jpushrecord_deleteredtipbyapp(JSONObject data, ApiCallback callback){
        post(callback, AppUrl.JPUSHRECORD_DELETEREDTIPBYAPP, data," 更小红点信息（设置已读）");
    }

    /**
     * 推送小红点的更新
     */
    public void jpushrecord_updateredtipbyapp(JSONObject data, ApiCallback callback){
        post(callback, AppUrl.JPUSHRECORD_UPDATEREDTIPBYAPP, data," 推送更小红点信息（设置已读）");
    }

    /**
     * 获取设置了下次同样物流的物流
     *
     * @param data
     * @param callback
     */
    public void sellerfahuo_getsamefahuo(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.SELLERFAHUO_GETSAMEFAHUO, data, "获取设置了下次同样物流的物流");
    }

    /**
     * 重新上架
     *
     * @param data
     * @param callback
     */
    public void product_reupstorebyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_REUPSTOREBYAPP, data, "重新上架");
    }

    /**
     * 拍品拍卖结果查询
     *
     * @param data
     * @param callback
     */
    public void product_getcurpmresultbyapp(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.PRODUCT_GETCURPMRESULTBYAPP, data, "拍品拍卖结果查询");
    }
    /**
     * 修改密码
     *
     * @param data
     * @param callback
     */
    public void login_updateloginpwd(JSONObject data, ApiCallback callback) {
        post(callback, AppUrl.LOGIN_UPDATELOGINPWD, data, "修改密码");
    }

    /**
     * 获取首页筛选和排序内容
     */
    public void dirtionary_getFilterAndSortListByApp(JSONObject data, ApiCallback callback){
        post(callback, AppUrl.DIRTIONARY_GETFILTERANDSORTLISTBYAPP, data, "获取首页筛选框内容");
    }


    /**
     * 专题活动界面的bannner图片和底纹
     */
    public void appEvent_getEventBannerByApp(JSONObject data,ApiCallback callback){
        post(callback,AppUrl.APPEVENT_GETEVENTBANNERBYAPP,data,"专题活动的banner图和底纹");
    }

    /**
     * 专题活动界面的商品列表
     */

    public void appEvent_getEventProductListByApp(JSONObject data,ApiCallback callback){
        post(callback,AppUrl.APPEVENT_GETEVENTPRODUCTLISTBYAPP,data,"专题活动商品列表");
    }

    /**
     * 进入买卖家交互界面调用的问答列表
     */
    public void commonAftersales_getListByApp(JSONObject data,ApiCallback callback){
        post(callback,AppUrl.COMMONAFTERSALES_GETLISTBYAPP,data,"买卖家交流列表");
    }

    /**
     * 添加买卖家交流的信息
     */

    public void commonAftersales_submitCommonByApp(JSONObject data,ApiCallback callback){
        post(callback,AppUrl.COMMONAFTERSALES_SUBMITCOMMONBYAPP,data,"买卖家交流列表");
    }

}
