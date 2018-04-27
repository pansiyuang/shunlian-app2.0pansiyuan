package com.shunlian.app.utils;

import android.os.AsyncTask;

import java.io.File;

/**
 * Created by Administrator on 2018/4/26.
 */

public class ClearCacheUtil extends AsyncTask<Void, Void, Integer> {
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param voids The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Integer doInBackground(Void... voids) {
        executeClear();
        return 1000;
    }

    private void executeClear() {
        File file = new File(Constant.CACHE_PATH_EXTERNAL);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        if (i == 1000) {
            Common.staticToast("清除缓存成功");
        }
    }
}
