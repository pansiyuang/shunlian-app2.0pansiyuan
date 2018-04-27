package com.shunlian.app.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11.
 */

public class EmojisUtils {
    private static Map<String, Integer> emojisMap;
    private static Map<Integer, String> emojisMap1;

    public static int emojisIndex(String key) {
        if (emojisMap == null) {
            emojisMap = emojisMap();
        }
        Integer index = emojisMap.get(key);
        return index;
    }

    public static String emojisName(Integer index) {
        if (emojisMap1 == null) {
            emojisMap1 = emojismap();
        }
        String name = emojisMap1.get(index);
        return name;
    }

    public static Map<String, Integer> emojisMap() {
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

    public static Map<Integer, String> emojismap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "[微笑]");
        map.put(1, "[撇嘴]");
        map.put(2, "[色]");
        map.put(3, "[发呆]");
        map.put(4, "[得意]");
        map.put(5, "[流泪]");
        map.put(6, "[害羞]");
        map.put(7, "[闭嘴]");
        map.put(8, "[睡]");
        map.put(9, "[大哭]");
        map.put(10, "[尴尬]");
        map.put(11, "[发怒]");
        map.put(12, "[调皮]");
        map.put(13, "[呲牙]");
        map.put(14, "[惊讶]");
        map.put(15, "[难过]");
        map.put(16, "[酷]");
        map.put(17, "[冷汗]");
        map.put(18, "[抓狂]");
        map.put(19, "[吐]");
        map.put(20, "[偷笑]");
        map.put(21, "[可爱]");
        map.put(22, "[白眼]");
        map.put(23, "[傲慢]");
        map.put(24, "[饥饿]");
        map.put(25, "[困]");
        map.put(26, "[惊恐]");
        map.put(27, "[流汗]");
        map.put(28, "[憨笑]");
        map.put(29, "[大兵]");
        map.put(30, "[奋斗]");
        map.put(31, "[咒骂]");
        map.put(32, "[疑问]");
        map.put(33, "[嘘]");
        map.put(34, "[晕]");
        map.put(35, "[折磨]");
        map.put(36, "[衰]");
        map.put(37, "[骷髅]");
        map.put(38, "[敲打]");
        map.put(39, "[再见]");
        map.put(40, "[擦汗]");
        map.put(41, "[抠鼻]");
        map.put(42, "[鼓掌]");
        map.put(43, "[糗大了]");
        map.put(44, "[坏笑]");
        map.put(45, "[左哼哼]");
        map.put(46, "[右哼哼]");
        map.put(47, "[哈欠]");
        map.put(48, "[鄙视]");
        map.put(49, "[委屈]");
        map.put(50, "[快哭了]");
        map.put(51, "[阴险]");
        map.put(52, "[亲亲]");
        map.put(53, "[吓]");
        map.put(54, "[可怜]");
        map.put(55, "[菜刀]");
        map.put(56, "[西瓜]");
        map.put(57, "[啤酒]");
        map.put(58, "[篮球]");
        map.put(59, "[乒乓]");
        map.put(60, "[咖啡]");
        map.put(61, "[饭]");
        map.put(62, "[猪头]");
        map.put(63, "[玫瑰]");
        map.put(64, "[凋谢]");
        map.put(65, "[示爱]");
        map.put(66, "[爱心]");
        map.put(67, "[心碎]");
        map.put(68, "[蛋糕]");
        map.put(69, "[闪电]");
        map.put(70, "[炸弹]");
        map.put(71, "[刀]");
        map.put(72, "[足球]");
        map.put(73, "[瓢虫]");
        map.put(74, "[便便]");
        map.put(75, "[月亮]");
        map.put(76, "[太阳]");
        map.put(77, "[礼物]");
        map.put(78, "[拥抱]");
        map.put(79, "[强]");
        map.put(80, "[弱]");
        map.put(81, "[握手]");
        map.put(82, "[胜利]");
        map.put(83, "[抱拳]");
        map.put(84, "[勾引]");
        map.put(85, "[拳头]");
        map.put(86, "[差劲]");
        map.put(87, "[爱你]");
        map.put(88, "[NO]");
        map.put(89, "[OK]");
        map.put(90, "[爱情]");
        map.put(91, "[飞吻]");
        map.put(92, "[跳跳]");
        map.put(93, "[发抖]");
        map.put(94, "[怄火]");
        map.put(95, "[转圈]");
        map.put(96, "[磕头]");
        map.put(97, "[回头]");
        map.put(98, "[跳绳]");
        map.put(99, "[挥手]");
        map.put(100, "[激动]");
        map.put(101, "[街舞]");
        map.put(102, "[献吻]");
        map.put(103, "[左太极]");
        map.put(104, "[右太极]");
        map.put(105, "[嘿哈]");
        map.put(106, "[捂脸]");
        map.put(107, "[奸笑]");
        map.put(108, "[机智]");
        map.put(109, "[皱眉]");
        map.put(110, "[耶]");
        map.put(111, "[红包]");
        map.put(112, "[鸡]");
        return map;
    }

    public static Bitmap getEmojiBitmap(Context context, int index) {
        InputStream is = null;
        Bitmap resizedBitmap = null;
        try {
            AssetManager am = context.getAssets();
            is = am.open(String.format("emojis/%d.png", index));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Matrix matrix = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // 缩放图片的尺寸
            int i1 = TransformUtil.dip2px(context, 28);
            float scaleWidth = (float) i1 / width;
            float scaleHeight = (float) i1 / height;
            matrix.postScale(scaleWidth, scaleHeight);
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resizedBitmap;
    }
}
