package com.shunlian.app.bean;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhang on 2017/4/17 12 : 01.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadPicEntity {
    public String domain;
    public List<String> relativePath;
    public List<String> newFileName;
    public List<String> fileType;
    public List<String> fileSize;
    public List<SizeInfo> sizeInfo;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SizeInfo {
        public int width;
        public int height;
    }
}
