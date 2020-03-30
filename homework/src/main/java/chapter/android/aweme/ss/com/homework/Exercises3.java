package chapter.android.aweme.ss.com.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import chapter.android.aweme.ss.com.homework.model.Message;
import chapter.android.aweme.ss.com.homework.model.PullParser;

import java.io.InputStream;
import java.util.List;

/**
 * 大作业:实现一个抖音消息页面,
 * 1、所需的data数据放在assets下面的data.xml这里，使用PullParser这个工具类进行xml解析即可
 * <p>如何读取assets目录下的资源，可以参考如下代码</p>
 * <pre class="prettyprint">
 *
 *     @Override
 *     protected void onCreate(@Nullable Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_xml);
 *         //load data from assets/data.xml
 *         try {
 *             InputStream assetInput = getAssets().open("data.xml");
 *             List<Message> messages = PullParser.pull2xml(assetInput);
 *             for (Message message : messages) {
 *
 *             }
 *         } catch (Exception exception) {
 *             exception.printStackTrace();
 *         }
 *     }
 * </pre>
 * 2、所需UI资源已放在res/drawable-xxhdpi下面
 *
 * 3、作业中的会用到圆形的ImageView,可以参考 widget/CircleImageView.java
 */
public class Exercises3 extends AppCompatActivity implements NewAdapter.ListItemClickListener {
    private static final String TAG = "liuzhengqin";
    private static final int NUM_LIST_ITEMS = 1;
    private NewAdapter mAdapter;//适配器
    private RecyclerView mNumbersListView;//RecyclerView
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        mNumbersListView = findViewById(R.id.rv_list);
        Log.d(TAG, "进入R.layout.activity_tips");
        //load data from assets/data.xml
        try {
            InputStream assetInput = getAssets().open("data.xml");
            List<Message> messages = PullParser.pull2xml(assetInput);
            //配置recycleview
            mNumbersListView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);//创建一个layoutManager
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//为layoutManager其设置样式
            mNumbersListView.setLayoutManager(layoutManager);//为RecyclerView设置layoutManager
            mAdapter = new NewAdapter(messages,this);//创建Adapter，输入item总数和当前activity。Adapter负责在列表中展示每个item
            mNumbersListView.setAdapter(mAdapter);//为RecyclerView设置Adapter
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //显示通知
        Log.d(TAG, "onListItemClick: ");
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
        //开启另一个活动
        Intent intent = new Intent(this, chatroom.class);//创建intent
        intent.putExtra("user", clickedItemIndex+"");//把字符串放进intent
        startActivity(intent);
    }
}
