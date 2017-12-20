

/*
 * Create on 2016-11-21 下午3:36
 * FileName: DateDialog.java
 * Author: huang qiqiang
 * Contact: http://www.huangqiqiang.cn
 *
 */


package com.lifeng.f300.common.datetime;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lifeng.f300.common.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @version V1.0 <描述当前版本功能>
 * @FileName:DateDialog.java
 * @author: 黄其强
 * @date: 2016/11/21  15:29
 */
public class DateDialog extends Dialog {
    public static final int MODE_1 = 001; // 日期 时间
    public static final int MODE_2 = 002;// 日期
    public static final int MODE_3 = 003;// 时间
    public static final int MODE_4 = 004; // 年月
    private int mMode = MODE_1;
    private String mTitle = "初始时间";

    InterfaceDateDialog interfaceDateDialog;

    public DateDialog(Context context, InterfaceDateDialog interfaceDateDialog) {
        super(context);
        this.interfaceDateDialog = interfaceDateDialog;
    }

    public DateDialog(Context context, String title, int mode, InterfaceDateDialog interfaceDateDialog) {
        super(context);
        this.interfaceDateDialog = interfaceDateDialog;
        mMode = mode;
        mTitle = title;

    }

    public DateDialog(Context context, int theme) {
        super(context, theme);
    }

    private DatePicker mDp_datePicker; //
    private TimePicker mTp_timePicker;
    private TextView mTv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date);
        mDp_datePicker = (DatePicker) findViewById(R.id.dp_datePicker);
        mTp_timePicker = (TimePicker) findViewById(R.id.tp_timePicker);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_title.setText(mTitle);
        mTp_timePicker.setIs24HourView(true);
        setCanceledOnTouchOutside(true);


        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        switch (mMode) {
            case MODE_1:


                resizePikcer(mDp_datePicker);//调整datepicker大小
                resizePikcer(mTp_timePicker);//调整timepicker大小
                findViewById(R.id.btn_succes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = "";
                        date = String.format("%d-%02d-%02d", mDp_datePicker.getYear(), mDp_datePicker.getMonth() + 1, mDp_datePicker.getDayOfMonth())
                                + " " + String.format("%02d:%02d", mTp_timePicker.getCurrentHour(), mTp_timePicker.getCurrentMinute());
                        interfaceDateDialog.getTime(date);
                        dismiss();

                    }
                });
                break;
            case  MODE_2:
                mTp_timePicker.setVisibility(View.GONE);
                findViewById(R.id.btn_succes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = "";
                        date = String.format("%d-%02d-%02d", mDp_datePicker.getYear(), mDp_datePicker.getMonth() + 1, mDp_datePicker.getDayOfMonth());
                        interfaceDateDialog.getTime(date);
                        dismiss();

                    }
                });
                break;
            case MODE_3:

                mDp_datePicker.setVisibility(View.GONE);
                findViewById(R.id.btn_succes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = "";
                        date =String.format("%02d:%02d", mTp_timePicker.getCurrentHour(), mTp_timePicker.getCurrentMinute());
                        interfaceDateDialog.getTime(date);
                        dismiss();

                    }
                });
                break;
            case MODE_4:
                mTp_timePicker.setVisibility(View.GONE);
                hideDay( mDp_datePicker);
                findViewById(R.id.btn_succes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = "";
                        date = String.format("%d-%02d", mDp_datePicker.getYear(), mDp_datePicker.getMonth() + 1, mDp_datePicker.getDayOfMonth());
                        interfaceDateDialog.getTime(date);
                        dismiss();

                    }
                });
                break;
        }


    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    child.setPadding(0, 0, 0, 0);

                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                } else if (child instanceof TextView) {
                    child.setVisibility(View.GONE);
                }

            }
        }
        return npList;
    }

    /**
     * 隐藏了 分
     * @param mDatePicker
     */
    private void hideDay(DatePicker mDatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resizeNumberPicker(NumberPicker np) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        np.setLayoutParams(params);
    }

    public interface InterfaceDateDialog {
        void getTime(String dateTime);
    }


}


//调用方法
/*
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_time1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog=   new DateDialog(MainActivity.this,"获取当前时间日期", DateDialog.MODE_1, new DateDialog.InterfaceDateDialog() {
                    @Override
                    public void getTime(String dateTime) {
                        Toast.makeText(MainActivity.this,dateTime,Toast.LENGTH_LONG).show();
                    }
                } );
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.show();
            }
        });



        findViewById(R.id.btn_time2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog=    new DateDialog(MainActivity.this,"获取当前时间日期", DateDialog.MODE_2, new DateDialog.InterfaceDateDialog() {
                    @Override
                    public void getTime(String dateTime) {
                        Toast.makeText(MainActivity.this,dateTime,Toast.LENGTH_LONG).show();
                    }
                } );
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.show();
            }
        });

        findViewById(R.id.btn_time3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog=     new DateDialog(MainActivity.this,"获取当前时间日期", DateDialog.MODE_3, new DateDialog.InterfaceDateDialog() {
                    @Override
                    public void getTime(String dateTime) {
                        Toast.makeText(MainActivity.this,dateTime,Toast.LENGTH_LONG).show();
                    }
                } );
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.show();
            }
        });
        findViewById(R.id.btn_time4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog=     new DateDialog(MainActivity.this,"获取当前时间日期", DateDialog.MODE_4, new DateDialog.InterfaceDateDialog() {
                    @Override
                    public void getTime(String dateTime) {
                        Toast.makeText(MainActivity.this,dateTime,Toast.LENGTH_LONG).show();
                    }
                } );
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.show();
            }
        });
    }

}*/
