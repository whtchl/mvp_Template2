package com.lifeng.f300.common.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * 异步图片加载
 * 
 * @author wangkeze
 * 
 */
public class MyAsynaTask extends AsyncTask<String, Integer, Bitmap> {
	
	private ImageView imageView;
	private int resourceId;
	
	/**
	 * @param imageView  显示的控件
	 * @param progressBar 进度条
	 * @param button 按钮
	 * @param resourceId 默认图片
	 */
	public MyAsynaTask(ImageView imageView, ProgressBar progressBar,
			Button button,int resourceId) {
		super();
		this.imageView = imageView;
		this.resourceId = resourceId;
//		this.progressBar = progressBar;
//		this.button = button;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		/*
		 * 改方法在执行实际的后台操作时被UI线程调用，可以在该方法中做一些准备工作，比如 Toast.makeText(context,
		 * "准备下载", Toast.LENGTH_LONG).show();
		 */
		super.onPreExecute();
	}

	@Override
	protected Bitmap doInBackground(String... params) {// 输入编变长的可变参数
														// 和ＵＩ线程中的Asyna.execute()对应
		// TODO Auto-generated method stub
		/*
		 * 该方法在OnpreExecute执行以后马上执行，改方法执行在后台线程当中，负责耗时的计算，
		 * 可以调用publishProcess方法来实时更新任务进度
		 */
		Bitmap bitmap = null;
		try {
			URL url = new URL(params[0]);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			// progressBar.setMax(connection.getContentLength());

			InputStream inputStream = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
			LogUtils.d("wang", "bitmap：doInBackground"+bitmap);//访问的不是图片地址为null
			// inputStream.close();
			/*
			 * 以下完全为了演示进度条，如果为了显示进度条可以把这个去掉
			 */
			/*
			 * byte []buf=new byte[1024*4];
			 * inputStream=connection.getInputStream(); int len=0; while
			 * ((inputStream.read(buf))!=-1) { len+=buf.length;
			 * publishProgress(len); }
			 */

			inputStream.close();

		} catch (Exception e) {
			LogUtils.d("wang", "异常了bitmap=null");//地址为空，就会异常
			// TODO: handle exception
			bitmap=null;
		}
		return bitmap;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		/*
		 * 当publichProcess 呗调用以后，ＵＩ线程将调用这个有方法在界面上展示任务的情况，比如一个额进度条。这里是更新进度条
		 */
		int value = values[0];
		// progressBar.setProgress(value);
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		/*
		 * 在doInbackground执行完成以后，onPostExecute将被调用，后台的结果将返回给UI线程，将获得图片显示出来
		 */
		LogUtils.d("wang", "bitmap：onPostExecute"+result);
		if (null != result) {
			imageView.setImageBitmap(result);
		}else{
			imageView.setImageResource(resourceId);
		}
		// button.setText("下载完成");
		super.onPostExecute(result);
	}

}
