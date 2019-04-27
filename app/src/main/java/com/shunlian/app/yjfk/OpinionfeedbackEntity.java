package com.shunlian.app.yjfk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shunlian.app.bean.BaseEntity;

import java.util.List;

/**
 * Created by Administrator on 2019/4/17.
 * {
 "code": 1000,
 "message": "成功",
 "data": {
 "announcement_content": "浙江顺联网络科技有限公司是中国领先的互联网技术公司，由郭洪安先生于2004年创立，是浙江省商务厅首批认定的电子商务服务企业。\\n浙江顺联网络科技有限公司是中国领先的互联网技术公司，由郭洪安先生于2004年创立，是浙江省商务厅首批认定的电子商务服务企业。",//公告内容
 "ads": [//广告数组
 {
 "image": "http://img.v2.shunliandongli.com/uploads/20190402/20190402095613677f.jpg?w=1920&h=1080",
 "link": {
 "type": "url",
 "item_id": "www.baidu.com"
 }
 }
 ],
 "ads_count": 1//广告数量
 },
 "agent": "ShunLian iPhone 10.3.3/2.6.2.1",
 "client_type": "ios"
 }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpinionfeedbackEntity {
    public String announcement_content;
    public List<adss> ads;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class adss{
        public String image;
        public typebean link;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class typebean{
            public String type;
            public String item_id;
        }
    }

    public int ads_count;

}
