package com.lifeng.f300.server.service;

import com.google.gson.Gson;
import com.lifeng.f300.common.entites.TransResponse;
import com.lifeng.f300.config.API;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by happen on 2017/8/7.
 */

public class WrapperConverter implements Converter {
    private static final String CHARSET_DEFAULT = "utf-8";
    private static final int BUFFER_SIZE = 0x400;
    private Gson gson;

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        String result = getString(body);
        //JUtils.Log("result:"+result);
        TransResponse transResponse;
        transResponse = getGson().fromJson(result,TransResponse.class);
        if(null!=transResponse && API.CODE.SUCCEED.equals(transResponse.STATUS)){
            return transResponse;
        }else{
            /*ToastMakeUtils.showToast(TimesCountActivity.this, transResponse != null ? transResponse.MESSAGE
                    : getString(R.string.parse_error));*/
            throw  new ServiceException( transResponse != null ? transResponse.STATUS:"[",
                                         transResponse != null ? transResponse.MESSAGE:"数据解析错误]");
        }

        /*try {
            transResponse = getGson().fromJson(result,TransResponse.class);

            return transResponse;
        }catch (Exception  e){
            throw new ServiceException(API.CODE.ANALYSIS_ERROR,"数据解析错误");
        }*/
    }



    public Gson getGson(){
        if (gson==null)gson = new Gson();
        return gson;
    }

    private String getString(TypedInput body) throws ConversionException {
        String charset = CHARSET_DEFAULT;
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType(), charset);
        }
        InputStreamReader isr = null;
        String result;
        try {
            isr = new InputStreamReader(body.in(), charset);
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[BUFFER_SIZE];
            for (; ; ) {
                int res = isr.read(buf, 0, buf.length);
                if (res < 0) {
                    break;
                }
                sb.append(buf, 0, res);
            }
            result = sb.toString();
            return result;
        } catch (IOException e) {
            throw new ConversionException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    @Override
    public TypedOutput toBody(Object o) {
        try {
            return new JsonTypedOutPut(getGson().toJson(o).getBytes(CHARSET_DEFAULT), CHARSET_DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutPut implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        public JsonTypedOutPut(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }
}
