package com.shunlian.app.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11.
 */

public class EmojisUtils {
    private static Map<String, Integer> emojisMap;

    public static int emojisIndex(String key){
        if (emojisMap == null){
            emojisMap = emojisMap();
        }
        Integer index = emojisMap.get(key);
        return index;
    }

    public static Map<String, Integer> emojisMap(){
        Map<String, Integer> emojisMap = new HashMap<>();
        emojisMap.put("[微笑]", 0);
        emojisMap.put("[撇嘴]", 1);
        emojisMap.put("[色]", 2);
        emojisMap.put("[发呆]", 3);
        emojisMap.put("[得意]", 4);
        emojisMap.put("[流泪]", 5);
        emojisMap.put("[害羞]", 6);
        emojisMap.put("[闭嘴]", 7);
        emojisMap.put("[睡]", 8);
        emojisMap.put("[大哭]", 9);
        emojisMap.put("[尴尬]", 10);
        emojisMap.put("[发怒]", 11);
        emojisMap.put("[调皮]", 12);
        emojisMap.put("[呲牙]", 13);
        emojisMap.put("[惊讶]", 14);
        emojisMap.put("[难过]", 15);
        emojisMap.put("[酷]", 16);
        emojisMap.put("[冷汗]", 17);
        emojisMap.put("[抓狂]", 18);
        emojisMap.put("[吐]", 19);
        emojisMap.put("[偷笑]", 20);
        emojisMap.put("[可爱]", 21);
        emojisMap.put("[白眼]", 22);
        emojisMap.put("[傲慢]", 23);
        emojisMap.put("[饥饿]", 24);
        emojisMap.put("[困]", 25);
        emojisMap.put("[惊恐]", 26);
        emojisMap.put("[流汗]", 27);
        emojisMap.put("[憨笑]", 28);
        emojisMap.put("[大兵]", 29);
        emojisMap.put("[奋斗]", 30);
        emojisMap.put("[咒骂]", 31);
        emojisMap.put("[疑问]", 32);
        emojisMap.put("[嘘]", 33);
        emojisMap.put("[晕]", 34);
        emojisMap.put("[折磨]", 35);
        emojisMap.put("[衰]", 36);
        emojisMap.put("[骷髅]", 37);
        emojisMap.put("[敲打]", 38);
        emojisMap.put("[再见]", 39);
        emojisMap.put("[擦汗]", 40);
        emojisMap.put("[抠鼻]", 41);
        emojisMap.put("[鼓掌]", 42);
        emojisMap.put("[糗大了]", 43);
        emojisMap.put("[坏笑]", 44);
        emojisMap.put("[左哼哼]", 45);
        emojisMap.put("[右哼哼]", 46);
        emojisMap.put("[哈欠]", 47);
        emojisMap.put("[鄙视]", 48);
        emojisMap.put("[委屈]", 49);
        emojisMap.put("[快哭了]", 50);
        emojisMap.put("[阴险]", 51);
        emojisMap.put("[亲亲]", 52);
        emojisMap.put("[吓]", 53);
        emojisMap.put("[可怜]", 54);
        emojisMap.put("[菜刀]", 55);
        emojisMap.put("[西瓜]", 56);
        emojisMap.put("[啤酒]", 57);
        emojisMap.put("[篮球]", 58);
        emojisMap.put("[乒乓]", 59);
        emojisMap.put("[咖啡]", 60);
        emojisMap.put("[饭]", 61);
        emojisMap.put("[猪头]", 62);
        emojisMap.put("[玫瑰]", 63);
        emojisMap.put("[凋谢]", 64);
        emojisMap.put("[示爱]", 65);
        emojisMap.put("[爱心]", 66);
        emojisMap.put("[心碎]", 67);
        emojisMap.put("[蛋糕]", 68);
        emojisMap.put("[闪电]", 69);
        emojisMap.put("[炸弹]", 70);
        emojisMap.put("[刀]", 71);
        emojisMap.put("[足球]", 72);
        emojisMap.put("[瓢虫]", 73);
        emojisMap.put("[便便]", 74);
        emojisMap.put("[月亮]", 75);
        emojisMap.put("[太阳]", 76);
        emojisMap.put("[礼物]", 77);
        emojisMap.put("[拥抱]", 78);
        emojisMap.put("[强]", 79);
        emojisMap.put("[弱]", 80);
        emojisMap.put("[握手]", 81);
        emojisMap.put("[胜利]", 82);
        emojisMap.put("[抱拳]", 83);
        emojisMap.put("[勾引]", 84);
        emojisMap.put("[拳头]", 85);
        emojisMap.put("[差劲]", 86);
        emojisMap.put("[爱你]", 87);
        emojisMap.put("[NO]", 88);
        emojisMap.put("[OK]", 89);
        emojisMap.put("[爱情]", 90);
        emojisMap.put("[飞吻]", 91);
        emojisMap.put("[跳跳]", 92);
        emojisMap.put("[发抖]", 93);
        emojisMap.put("[怄火]", 94);
        emojisMap.put("[转圈]", 95);
        emojisMap.put("[磕头]", 96);
        emojisMap.put("[回头]", 97);
        emojisMap.put("[跳绳]", 98);
        emojisMap.put("[挥手]", 99);
        emojisMap.put("[激动]", 100);
        emojisMap.put("[街舞]", 101);
        emojisMap.put("[献吻]", 102);
        emojisMap.put("[左太极]", 103);
        emojisMap.put("[右太极]", 104);
        emojisMap.put("[嘿哈]", 105);
        emojisMap.put("[捂脸]", 106);
        emojisMap.put("[奸笑]", 107);
        emojisMap.put("[机智]", 108);
        emojisMap.put("[皱眉]", 109);
        emojisMap.put("[耶]", 110);
        emojisMap.put("[红包]", 111);
        emojisMap.put("[鸡]", 112);
        return emojisMap;
    }
}
