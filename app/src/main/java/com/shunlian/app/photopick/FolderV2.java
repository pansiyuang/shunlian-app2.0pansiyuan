package com.shunlian.app.photopick;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class FolderV2 {
    public String name;
    public String path;
    public ImageVideo cover;
    public List<ImageVideo> images;

    @Override
    public String toString() {
        return "FolderV2{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", images=" + images.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        try {
            FolderV2 other = (FolderV2) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
