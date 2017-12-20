package com.shunlian.app.utils.upload;

import com.shunlian.app.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadFileRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    private ProgressListener mProgressListener;

    private BufferedSink bufferedSink;

    //每个RequestBody对应一个tag，存放在map中，保证计算的时候不会出现重复
    private String tag;

    public UploadFileRequestBody(File file, ProgressListener progressListener, String tag) {
        this.mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        this.mProgressListener = progressListener;
        this.tag = tag;
    }

    //其实只是添加一个回调和tag标识，实际起作用的还是requestBody
    public UploadFileRequestBody(RequestBody requestBody, ProgressListener progressListener, String tag) {
        this.mRequestBody = requestBody;
        this.mProgressListener = progressListener;
        this.tag = tag;
    }

    //返回了requestBody的类型，想什么form-data/MP3/MP4/png等等等格式
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    //返回了本RequestBody的长度，也就是上传的totalLength
    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer) {
            mRequestBody.writeTo(sink);
            return;
        }
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;

                DecimalFormat df = new DecimalFormat("0.00");//格式化小数
                String num = df.format((float) bytesWritten / contentLength);
                int progress = (int) (Double.valueOf(num) * 100);
                //回调上传接口
                mProgressListener.onProgress(progress, tag);
                mProgressListener.onDetailProgress(bytesWritten, contentLength, tag);
            }
        };
    }
}