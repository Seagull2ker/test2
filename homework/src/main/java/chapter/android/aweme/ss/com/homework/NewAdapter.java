package chapter.android.aweme.ss.com.homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import chapter.android.aweme.ss.com.homework.model.Message;
import java.util.List;

//创建一个继承RecyclerView.Adapter的适配器（抽象类）
public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NumberViewHolder> {

    private static final String TAG = "NewAdapter";//标签
    private final ListItemClickListener mOnClickListener;//监听器，即activity
    private static int viewHolderCount;//viewHolder的数量
    private List<Message> mDatas;//数据

    //构造函数
    public NewAdapter(List<Message> messages, ListItemClickListener listener) {
        this.mDatas = messages;//传递数据
        mOnClickListener = listener;//点击监听器，即activity
        viewHolderCount = 0;//viewHolder计数器设初值
    }

    /*
     * 一般会预留2~4个ViewHolder，off screen的数量由mCachedSize来决定
     *
     * The number of ViewHolders that have been created. Typically, you can figure out how many
     * there should be by determining how many list items fit on your screen at once and add 2 to 4
     * to that number. That isn't the exact formula, but will give you an idea of how many
     * ViewHolders have been created to display any given RecyclerView.
     *
     * Here's some ASCII art to hopefully help you understand:
     *
     *    ViewHolders on screen:
     *
     *        *-----------------------------*
     *        |         ViewHolder index: 0 |
     *        *-----------------------------*
     *        |         ViewHolder index: 1 |
     *        *-----------------------------*
     *        |         ViewHolder index: 2 |
     *        *-----------------------------*
     *        |         ViewHolder index: 3 |
     *        *-----------------------------*
     *        |         ViewHolder index: 4 |
     *        *-----------------------------*
     *        |         ViewHolder index: 5 |
     *        *-----------------------------*
     *        |         ViewHolder index: 6 |
     *        *-----------------------------*
     *        |         ViewHolder index: 7 |
     *        *-----------------------------*
     *
     *    Extra ViewHolders (off screen)
     *
     *        *-----------------------------*
     *        |         ViewHolder index: 8 |
     *        *-----------------------------*
     *        |         ViewHolder index: 9 |
     *        *-----------------------------*
     *        |         ViewHolder index: 10|
     *        *-----------------------------*
     *        |         ViewHolder index: 11|
     *        *-----------------------------*
     *
     *    index:12 from where?
     *
     *    Total number of ViewHolders = 12
     *
     *
     *    不做特殊处理：最多缓存多少个ViewHolder N(第一屏可见) + 2 mCachedSize + 5*itemType RecyclePool
     *
     *    找到position一致的viewholder才可以复用，新的位置由于position不一致，所以不能复用，重新创建新的
     *    这也是为什么 mCachedViews一开始缓存的是0、1    所以 8、9、10需要被创建，
     *    那为什么10 和 11也要被创建？
     *
     *    当view完全不可见的时候才会被缓存回收，这与item触发getViewForPosition不同，
     *    当2完全被缓存的时候，实际上getViewForPosition已经触发到11了，此时RecyclePool有一个viewholder(可以直接被复用)
     *    当12触发getViewForPosition的时候，由于RecyclePool里面有，所以直接复用这里的viewholder
     *    问题？复用的viewholder到底是 0 1 2当中的哪一个？
     *
     *
     *    RecycleView 对比 ListView 最大的优势,缓存的设计,减少bindView的处理
     */

    //这个方法主要为每个Item inflater生成出一个View，返回的是一个ViewHolder。
    //该方法把View直接封装在ViewHolder中，然后我们面向的是ViewHolder这个实例，当然这个ViewHolder需要我们自己去编写。
    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.im_list_item;//item的xml布局资源信息
        LayoutInflater inflater = LayoutInflater.from(context);//创建一个LayoutInflater
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);//创建一个View对象
        NumberViewHolder viewHolder = new NumberViewHolder(view);//使用NumberViewHolder内部类，创建一个ViewHolder对象

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + viewHolderCount);
        viewHolderCount++;//记录viewHolder数量加一
        return viewHolder;//将ViewHolder返回
    }

    //适配渲染数据到ViewHolder中
    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder numberViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: #" + position);
        numberViewHolder.bind(position);
    }

    //返回数据集大小，即item的总数，总共有多少个条目
    @Override
    public int getItemCount() {return mDatas.size(); }

    //ViewHolder类，一个继承RecyclerView.ViewHolder的内部类
    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //自定义若干TextView成员
        private final TextView TitleView;
        private final TextView TimeView;
        private final TextView DescriptionView;
        private final ImageView HeadIconView;
        private final ImageView OfficialIconView;
        //private final TextView IconView;

        //构造函数
        public NumberViewHolder(@NonNull View itemView) {//传入一个view
            super(itemView);
            itemView.setOnClickListener(this);//为传入的view设置点击监听，为此下面需重写onClick()函数
            //将自定义的两个TextView与相应xml成员绑定
            TitleView = (TextView) itemView.findViewById(R.id.tv_title);
            TimeView = (TextView) itemView.findViewById(R.id.tv_time);
            DescriptionView = (TextView) itemView.findViewById(R.id.tv_description);
            HeadIconView = (ImageView) itemView.findViewById(R.id.iv_avatar);
            OfficialIconView = (ImageView) itemView.findViewById(R.id.robot_notice);
        }

        //绑定数据
        public void bind(int position) {
            TitleView.setText(mDatas.get(position).getTitle());
            TimeView.setText(mDatas.get(position).getTime());
            DescriptionView.setText(mDatas.get(position).getDescription());
            switch(mDatas.get(position).getIcon()){
                case "TYPE_ROBOT" :
                    HeadIconView.setImageResource(R.drawable.session_robot);
                    OfficialIconView.setImageResource(R.drawable.im_icon_notice_official);
                    break; //可选
                case "TYPE_GAME" :
                    HeadIconView.setImageResource(R.drawable.icon_micro_game_comment);
                    OfficialIconView.setImageResource(R.drawable.im_icon_notice_official);
                    break; //可选
                case "TYPE_SYSTEM" :
                    HeadIconView.setImageResource(R.drawable.session_system_notice);
                    OfficialIconView.setImageResource(R.drawable.im_icon_notice_official);
                    break; //可选
                case "TYPE_STRANGER" :
                    HeadIconView.setImageResource(R.drawable.session_stranger);
                    break; //可选
                case "TYPE_USER" :
                    HeadIconView.setImageResource(R.drawable.icon_girl);
                    break; //可选
                default : //可选
            }
        }

        //首写点击函数，因为上面设置了为传入的view设置了监听，所以点击view就会调用此函数
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();//获取Adapter位置
            if (mOnClickListener != null) {//不为空
                mOnClickListener.onListItemClick(clickedPosition);//以clickedPosition为参数，调用接口中的onListItemClick函数
            }
        }
    }

    //接口
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
