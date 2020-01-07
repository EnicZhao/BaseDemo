package com.project.enic.basedemo.base_module.api.download;

import com.project.enic.basedemo.base_module.api.Request;
import com.project.enic.basedemo.base_module.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadUtil {
    private Retrofit retrofit;
    private DownloadListener listener;
    public DownloadUtil(DownloadListener listener) {
        String domain_url = Config.API_DOMAIN;
        this.listener = listener;
        DownloadInterceptor mInterceptor = new DownloadInterceptor(listener);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(domain_url)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 开始下载
     *
     */
    public void startDownload(File file,String ufis,String contentType) {
        String url = Config.API_DOMAIN + Config.API.DOWNLOAD_FILE + "?fjid=" + ufis;
        retrofit.create(Request.class).download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFile(inputStream, file);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }


    private void writeFile(InputStream inputString,File file) {
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b,0,len);
            }
            inputString.close();
            fos.close();
        } catch (FileNotFoundException e) {
            listener.onFail("下载失败");
        } catch (IOException e) {
            listener.onFail("下载失败");
        }
    }

}
