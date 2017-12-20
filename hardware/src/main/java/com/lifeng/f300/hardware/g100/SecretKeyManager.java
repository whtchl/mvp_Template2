package com.lifeng.f300.hardware.g100;

import com.jude.utils.JUtils;
import com.witsi.sdk.mpos.WtCallback;
import com.witsi.sdk.mpos.WtDevContrl;

/**
 * Created by happen on 2017/10/17.
 */

public class SecretKeyManager {

    /**
     * Describe : 加载密钥回调<br/>
     * Date : 2015年10月19日下午2:34:00 <br/>
     * Version : 1.0 <br/>
     *
     * @author XiongWei
     */
    public interface OnLoadSecretKeyCallback {
        /**
         * 加载是否成功
         *
         * @param state
         * true 成功 false 失败
         */
        void onLoadState(boolean state);
    }

    private static final String TAG = SecretKeyManager.class.getSimpleName();

    // 主密钥(开发测试使用,出厂时使用PC端灌注主密钥)
    private String masterKey = "F616DD76F290635EF616DD76F290635E";
    private String masterKeyVerbuf = "0000000000000000";   //先用固定的做解密  “2a919440ab304f6ab21f4700b1c6ba12”
    private String masterKeyValbuf = "82E13665";   //用固定的做校验

    // 工作密钥
    private String[] workKey = { "950973182317F80B950973182317F80B", // PIN密钥
            "F679786E2411E3DEF679786E2411E3DE", // mac密钥
            "A0C45C59F1E549BBA0C45C59F1E549BB" // track密钥
    };
    private String workKeyVerbuf[] = { "0000000000000000", "0000000000000000", "0000000000000000" };   //用主密钥做解密
    private String workKeyValbuf[] = { "00962B60", "ADC67D84", "E2F24340" };	//用主密钥做校验

    /** 加载密钥中API接口中的索引号 */
    private static final int KEY_GROUP_INDEX = 1;

    private WtDevContrl wtDevContrl;

    private OnLoadSecretKeyCallback masterKeyCallback;
    private OnLoadSecretKeyCallback workKeyCallback;
    private OnLoadSecretKeyCallback aidCallback;

    public void setOnLoadWorkKeyCallback(OnLoadSecretKeyCallback callback) {
        workKeyCallback = callback;
    }

    public void setOnLoadMasterKeyCallback(OnLoadSecretKeyCallback callback) {
        masterKeyCallback = callback;
    }

    public void setOnLoadAidKeyCallback(OnLoadSecretKeyCallback callback) {
        aidCallback = callback;
    }

    /**
     *
     * @param workKey
     *            密钥内容
     * @param workKeyVerbuf
     *            工作密钥加密规则
     * @param workKeyValbuf
     *            校验码
     */
    public void setWorkKey(String[] workKey, String[] workKeyVerbuf, String[] workKeyValbuf) {
        this.workKey = workKey;
        this.workKeyVerbuf = workKeyVerbuf;
        this.workKeyValbuf = workKeyValbuf;
    }

    /**
     * 设置主密钥
     * @param masterKey
     * @param masterKeyVerbuf
     * @param masterKeyValbuf
     */
    public void setMasterKey(String masterKey,String masterKeyVerbuf,String masterKeyValbuf) {
        this.masterKey = masterKey;
        this.masterKeyVerbuf = masterKeyVerbuf;
        this.masterKeyValbuf = masterKeyValbuf;
    }

    public SecretKeyManager(WtDevContrl wtDevContrl) {
        this.wtDevContrl = wtDevContrl;
    }

    /**
     * 返回当前设置的密钥
     *
     * @return
     */
    public int getCurrentKey() {
        return wtDevContrl.getCurrKey();
    }

    /**
     * 是否已经完成密钥设置
     *
     * @return boolean
     */
    public boolean isSetSecretKey() {
        return wtDevContrl.getCurrKey() >= 0;
    }

    /**
     * 加载主密钥
     */
    public void loadMasterKey() {
        JUtils.Log("start load master key");
        new Thread() {
            public void run() {
                wtDevContrl.clearKeyGroup(KEY_GROUP_INDEX);
                wtDevContrl.loadMainKey(KEY_GROUP_INDEX, masterKey, masterKeyVerbuf, masterKeyValbuf,
                        new WtCallback.WtLoadKeyCallback() {
                            @Override
                            public void onGetLoadKeyState(WtCallback.WtKeyType kType, WtCallback.WtWorkKeyType type, WtCallback.WtResult result, int code) {
                                // 加载主密钥成功
                                if (WtCallback.WtResult.WT_SUCC == result) {
                                    JUtils.Log("Load master key success");
                                } else {
                                    JUtils.Log("Load master key error, result: " + result + ", code: " + code);
                                }
                                if (null != masterKeyCallback) {
                                    masterKeyCallback.onLoadState(WtCallback.WtResult.WT_SUCC == result);
                                }
                            }
                        });
            }
        }.start();

    }

    /**
     * 加载工作密钥
     */
    public void loadWorkKey() {
        JUtils.Log("start load work key ###");
        new Thread() {
            public void run() {
                wtDevContrl.loadWorkKey(KEY_GROUP_INDEX, workKey, workKeyVerbuf, workKeyValbuf,
                        new WtCallback.WtLoadKeyCallback() {
                            @Override
                            public void onGetLoadKeyState(WtCallback.WtKeyType kType, WtCallback.WtWorkKeyType type, WtCallback.WtResult result, int code) {
                                if (WtCallback.WtResult.WT_SUCC == result) {
                                    JUtils.Log("Load work key success.");
                                    wtDevContrl.setCurrKey(KEY_GROUP_INDEX);
                                } else {
                                    JUtils.Log("Load work key error, result: " + result + ", code: " + code);
                                }

                                if (null != workKeyCallback) {
                                    workKeyCallback.onLoadState(WtCallback.WtResult.WT_SUCC == result);
                                }
                            }
                        });
            }
        }.start();
    }

    /**
     * 加载aid密钥
     */
    public void loadAidKey(final String[] paramList) {
        JUtils.Log("start load aid key ###");
        new Thread() {
            public void run() {
                wtDevContrl.emvAidUpdateTask(paramList,new WtCallback.WtEmvParamUpdate() {
                    @Override
                    public void onErr(WtCallback.WtEmvParamUpdateErrResult result, int index) {
                        JUtils.Log("Load work aid error, result: " + result + ", code: " + index);
                        aidCallback.onLoadState(false);
                    }

                    @Override
                    public void onSucc() {
                        JUtils.Log("Load Aid key success.");
                        if (null != aidCallback) {
                            aidCallback.onLoadState(true);
                        }
                    }
                });
            }
        }.start();
    }
}