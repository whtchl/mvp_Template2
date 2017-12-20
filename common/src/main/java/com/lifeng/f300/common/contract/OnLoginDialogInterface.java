package com.lifeng.f300.common.contract;

import android.widget.EditText;

/**
 * Created by happen on 2017/8/11.
 */

public interface OnLoginDialogInterface {

    public void onConfirmClick(EditText etUserName, EditText etOldPassword, EditText etPassword, EditText etPasswordSure);

    public void onCancelClick();

    public void onSureClick();
}
