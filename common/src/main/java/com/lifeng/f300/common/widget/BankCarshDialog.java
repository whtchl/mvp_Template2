package com.lifeng.f300.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.utils.JUtils;

import com.lifeng.f300.common.R;
import com.lifeng.f300.common.contract.HintDialogInterface;
import com.lifeng.f300.common.contract.OnLoginDialogInterface;
import com.lifeng.f300.common.entites.QueryMember;
import com.lifeng.f300.common.utils.BuildModle;
import com.lifeng.f300.common.utils.StringUtil;

/**
 * Created by happen on 2017/9/4.
 */

public class BankCarshDialog extends Dialog {
    public BankCarshDialog(Context context, int theme) {
        super(context, theme);
    }

    public static Dialog dialog; // 弹出框
    private static RelativeLayout closeRl; // 叉叉
    private static TextView transamtTv;
    private static LinearLayout vipInfoLl;
    private static TextView tvVipName;
    private static TextView showType;
    private static TextView detailName;
    private static LinearLayout vipInfoCard;
    private static TextView showTypeCard;
    private static TextView tvVipNumber;
    private static ImageView nfcPicture;
    private static AnimationDrawable rocketAnimation;

    public static void closeDialog() {
        if (null != rocketAnimation) {
            rocketAnimation.stop();
            rocketAnimation = null;
        }
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * @param context
     * @param theme
     * @param layout
     * @param transAmt
     *            总金额，消费或者充值或者撤消的
     * @param dialogInterface
     */
    @SuppressWarnings("deprecation")
    /*public static void showDialog(Context context, int theme, int layout, String transAmt, QueryMember queryMember,
                                  String cardRechargeNumber, boolean isRecharge, boolean isCancelTrans, String cardPrefix,
                                  final OnLoginDialogInterface dialogInterface) {*/
    public static void showDialog(Context context, int theme, int layout, String transAmt,  boolean isRecharge, boolean isCancelTrans,
                                  final OnLoginDialogInterface dialogInterface) {
        JUtils.Log("showDialog");
        try {
            if (context != null && context instanceof HintDialogInterface) {
                HintDialogInterface activity = (HintDialogInterface) context;
                if (activity.verifyDialogFlag()) {
                    JUtils.Log("showDialog1");
                    return;
                }
            }
            JUtils.Log("showDialog2");
            if (dialog != null) {
                JUtils.Log("showDialog3");
                dialog.dismiss();
                dialog = null;
            }
            JUtils.Log("showDialog4");
            dialog = new Dialog(context, theme);
            View view = LayoutInflater.from(context).inflate(layout, null);
            dialog.setContentView(view);
            closeRl = (RelativeLayout) view.findViewById(R.id.cancels);
            detailName = (TextView) view.findViewById(R.id.detail_name); // 标题
            transamtTv = (TextView) view.findViewById(R.id.transamt_tv);
            tvVipName = (TextView) view.findViewById(R.id.tv_vip_name);
            showType = (TextView) view.findViewById(R.id.show_type);
            nfcPicture = (ImageView) view.findViewById(R.id.nfc_picture);// 刷银行卡的图标
            tvVipNumber = (TextView) view.findViewById(R.id.tv_vip_number); // 会员卡号或者银行卡号
            showTypeCard = (TextView) view.findViewById(R.id.show_type_card); // 提示银行卡号或者会员卡号(标题)
            vipInfoLl = (LinearLayout) view.findViewById(R.id.vip_info_ll); // 会员名称
            vipInfoCard = (LinearLayout) view.findViewById(R.id.vip_info_card); // 会员卡号或者（撤消的时候）银行卡卡号

            if (null != showType) {
                if (isCancelTrans) {
                    showType.setText(context.getString(R.string.trans_cancel_total));
                } else {
                    showType.setText(isRecharge ? context.getString(R.string.recharge_trans_cash) : context
                            .getString(R.string.consum_trans_cash));
                }
            }

            if (null != detailName) {
                if (isCancelTrans) {
                    detailName.setText(context.getString(R.string.trans_cancel));
                } else {
                    detailName.setText(context.getString(R.string.bank_card_payment));
                }
            }

            // 叉叉
            if (null != closeRl) {
                closeRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogInterface != null) {
                            if (null != rocketAnimation) {
                                rocketAnimation.stop();
                                rocketAnimation = null;
                            }
                            dialogInterface.onCancelClick();
                        }
                    }
                });
            }

            if (null != transamtTv) {
                transamtTv.setText("¥" + transAmt);
            }

            /*if (null != queryMember) {
                vipInfoLl.setVisibility(View.VISIBLE);
                vipInfoCard.setVisibility(View.VISIBLE);
                tvVipName.setText(TextUtils.isEmpty(queryMember.MEMBERNAME) ? context.getString(R.string.name_nothing)
                        : queryMember.MEMBERNAME);
                showTypeCard.setText(isCancelTrans ? context.getString(R.string.bank_card) : context
                        .getString(R.string.card_number)); // 标题
                tvVipNumber.setText(isCancelTrans ? queryMember.bankCard
                        : (TextUtils.isEmpty(cardRechargeNumber) ? "暂无会员卡" : StringUtil.getRealCardNumber(
                        cardRechargeNumber, cardPrefix)));
            } else {*/
                vipInfoLl.setVisibility(View.GONE);
                vipInfoCard.setVisibility(View.GONE);
            //}

            if (isCancelTrans) {

            } else {

            }

           /* if (BuildModle.isF300OrS600()) {
                // nfcPicture.setBackgroundResource(R.anim.f300nfc_progress_round);
                nfcPicture.setBackgroundResource(R.drawable.nfc_logo);
                view.findViewById(R.id.magcard_picture).setVisibility(View.VISIBLE);
                view.findViewById(R.id.icc_picture).setVisibility(View.VISIBLE);
            } else if (BuildModle.isShengBen()) {
                nfcPicture.setBackgroundResource(R.anim.f400nfc_progress_round);
            } else if (BuildModle.isMoreFun()) {
                nfcPicture.setBackgroundResource(R.anim.f400nfc_progress_round);
            } else {
                nfcPicture.setBackgroundResource(R.anim.f300nfc_progress_round);
            }*/
            nfcPicture.setBackgroundResource(R.drawable.f300nfc_progress_round);
            //if (!BuildModle.isF300OrS600()) {
                if (null != rocketAnimation) {
                    rocketAnimation.stop();
                    rocketAnimation = null;
                }
                rocketAnimation = (AnimationDrawable) nfcPicture.getBackground();
                rocketAnimation.start();
           // }

            dialog.setCancelable(false);
            JUtils.Log("showDialog11");
            if (context instanceof Activity) {
                JUtils.Log("showDialog12");
                Activity activity = (Activity) context;
                WindowManager m = activity.getWindowManager();
                Window dialogWindow = dialog.getWindow();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.65
                dialogWindow.setAttributes(p);
                dialog.show();
            }
        } catch (Exception e) {
            JUtils.Log(e.getMessage()+ e);
        }
    }
}
