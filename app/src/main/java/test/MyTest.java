//package test;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.shunlian.app.R;
//import com.shunlian.app.ui.BaseActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//
//public class MyTest extends BaseActivity  {
//    @BindView(R.id.rv_test)
//    RecyclerView rv_test;
//
//
//    public static void startAct(Context context) {
//        Intent intent = new Intent(context, MyTest.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.act_ad_new;
//    }
//
//    @Override
//    protected void initData() {
//        rv_test.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        List<String> strings=new ArrayList<>();
////        strings.add("banner");
////        strings.add("nav");
////        strings.add("place");
////        strings.add("core");
////        strings.add("hot1");
////        strings.add("hot2");
////        strings.add("goods");
////        strings.add("cate");
////        strings.add("moreGoods");
//        strings.add("banner");
//        strings.add("nav");
//        strings.add("place");
//        strings.add("core");
//        strings.add("hot1");
//        strings.add("hot2");
//        strings.add("goods");
//        strings.add("cate");
//        strings.add("moreGoods");
//        rv_test.setAdapter(new NewFirstPageAdapter(this,true,strings));
//
//    }
//
//
//}
