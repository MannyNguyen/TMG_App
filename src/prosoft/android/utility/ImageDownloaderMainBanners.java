package prosoft.android.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloaderMainBanners extends Activity {
	int count = 0;
	Map<String,Bitmap> imageCache;
	public ImageDownloaderMainBanners(){
		imageCache = new HashMap<String, Bitmap>();
	}
	
	//download function
	public void download(String url, ImageView imageView) {
		
	     if (cancelPotentialDownload(url, imageView)) {
	    	 
	    	 //Caching code right here
	    	 String filename = String.valueOf(url.hashCode());
	    	 System.out.println("filename = "+filename);
	    	 File f = new File(getCacheDirectory(imageView.getContext()), filename);

	    	  // Is the bitmap in our memory cache?
	    	 Bitmap bitmap = null;
	    	 
	    	  bitmap = (Bitmap)imageCache.get(f.getPath());
	    	  
	    	  if(bitmap == null){
	    		  bitmap = BitmapFactory.decodeFile(f.getPath());
	    		 
	    		  if(bitmap != null){
	    			  imageCache.put(f.getPath(), bitmap);
	    			  
	    		  }
	    	  
	    	  }
	    	  //No? download it
	    	  if(bitmap == null){
	    		  System.out.println("bitmap == null");
	    		  BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
	    		  DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
	    		  imageView.setImageDrawable(downloadedDrawable);
	    		  task.execute(url);
	    	  }else{
	    		  //Yes? set the image
	    		  System.out.println("bitmap != null - set image bit map");
	    		  imageView.setImageBitmap(bitmap);
	    		  count --;
	    	  }
	     }
	}
	
	//cancel a download (internal only)
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		
	    BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

	    if (bitmapDownloaderTask != null) {
	        String bitmapUrl = bitmapDownloaderTask.url;
	        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
	            bitmapDownloaderTask.cancel(true);
	        } else {
	            // The same URL is already being downloaded.
	            return false;
	        }
	    }
	    return true;
	}
	
	//gets an existing download if one exists for the imageview
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		 System.out.println("BitmapDownloaderTask");
	    if (imageView != null) {
	    	System.out.println("BitmapDownloaderTask1");
	        Drawable drawable = imageView.getDrawable();
	        if (drawable instanceof DownloadedDrawable) {
	        	System.out.println("BitmapDownloaderTask2");
	            DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
	            return downloadedDrawable.getBitmapDownloaderTask();
	        }
	        else
	        	System.out.println("BitmapDownloaderTask3");
	    }
	    else
	    	System.out.println("BitmapDownloaderTask4");
	    return null;
	}
 
	//our caching functions
	// Find the dir to save cached images
	private static File getCacheDirectory(Context context){
		File cacheDir;
    
			cacheDir = context.getCacheDir();

		if(!cacheDir.exists())
			cacheDir.mkdirs();
			return cacheDir;
	}
	
	private void writeFile(Bitmap bmp, File f) {
		  FileOutputStream out = null;

		  try {
		    out = new FileOutputStream(f);
		    bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
		  } catch (Exception e) {
		    e.printStackTrace();
		    count--;
		  }
		  finally { 
		    try { if (out != null ) out.close(); }
		    catch(Exception ex) {} 
		  }
	}
	///////////////////////

	//download asynctask
    public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    	
    	private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {  
          count ++ ;
   		  System.out.println("imageCache.size() put = "+ count);
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
             // params comes from the execute() call: params[0] is the url.
        	 url = (String)params[0];
        	 System.out.println("doInBackground");
             return downloadBitmap(params[0]);
             
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if(imageViewReference  == null)
            	System.out.println("abc");
            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                if (this == bitmapDownloaderTask) {
                    imageView.setImageBitmap(bitmap);
                    
                    //cache the image
                    System.out.println("imageCache.size() = "+ imageCache.size()+ "=" +(count -1));
                    String filename = String.valueOf(url.hashCode());
       	    	 	File f = new File(getCacheDirectory(imageView.getContext()), filename);
       	    	 	
       	    	 	imageCache.put(f.getPath(), bitmap);
       	    	 	
                    writeFile(bitmap, f);
                									}
                else
                {
                	System.out.println("complete");
                }
            									}
            
        	}
        
        
    }
    
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
//            super(Color.WHITE);
        	
            bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }
    
    //the actual download code
    static Bitmap downloadBitmap(String url) {
    	System.out.println("downloadBitmap");
    	HttpParams params = new BasicHttpParams();
    	params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    	HttpClient client = new DefaultHttpClient(params);
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) { 
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
                return null;
            }
            
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent(); 
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();  
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + e.toString());
        } finally {
            if (client != null) {
                //client.close();
            }
        }
        return null;
    }
}

