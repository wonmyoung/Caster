package com.casting.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.casting.R;
import com.casting.commonmodule.view.list.CommonRecyclerAdapter;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.global.ItemConstant;

public class ItemViewAdapter extends CommonRecyclerAdapter implements ItemConstant
{

    private ItemBindStrategy    mItemBindStrategy;

    public ItemViewAdapter(Context context, ItemBindStrategy bindStrategy)
    {
        super(context);

        mItemBindStrategy = bindStrategy;
    }

    @Override
    protected CompositeViewHolder createBodyViewHolder(ViewGroup parent, int viewType)
    {
        Context c = getContext();

        View v = null;

        switch (viewType)
        {
            case CAST_CARD:
                v = inflateView(R.layout.view_item_cast_card, parent);
                break;

            case CAST_CARD_WIDE_THIN:
                v = inflateView(R.layout.view_item_cast_card_wide_thin, parent);
                break;

            case CAST_CARD_WIDE:
                v = inflateView(R.layout.view_item_cast_card_wide, parent);
                break;

            case RANKING:
                v = inflateView(R.layout.view_item_cast_ranking, parent);
                break;

            case NEWS:
                v = inflateView(R.layout.view_item_news, parent);
                break;

            case TIME_LINE:
                v = inflateView(R.layout.view_item_timeline, parent);
                break;

            case TIME_LINE_WRITE:
                v = inflateView(R.layout.view_item_timeline_write, parent);
                break;

            case TIME_LINE_GROUP:
                v = inflateView(R.layout.view_item_timeline_group, parent);
                break;

            case TIME_LINE_REPLY:
                v = inflateView(R.layout.view_item_timeline_reply, parent);
                break;

            case TIME_LINE_REPLY_WRITE:
                v = inflateView(R.layout.view_item_timeline_reply_insert, parent);
                break;

            case CURRENT_CASTING_STATUS:
                v = inflateView(R.layout.view_item_current_casting_status, parent);
                break;

            case FOLLOWING_INFO_ITEM:
                v = inflateView(R.layout.view_item_following_info, parent);
                break;

            case ALARM:
                v = inflateView(R.layout.view_item_alarm, parent);
                break;

            case GRAPH_LINE:
                v = inflateView(R.layout.view_item_line_graph, parent);
                break;

            case DOUBLE_PIE_CHART:
                v = inflateView(R.layout.view_item_pie_chart, parent);
                break;

            case SELECT_HORIZONTAL_OPTIONS:
                v = inflateView(R.layout.view_item_selectable_horizontal_options, parent);
                break;

            case SELECT_VERTICAL_OPTIONS:
                v = inflateView(R.layout.view_item_selectable_vertical_options, parent);
                break;

            case SELECT_SCROLLABLE_OPTION:
                v = inflateView(R.layout.view_item_scrollable_options, parent);
                break;

            case SELECT_BOOLEAN_OPTION:
                v = inflateView(R.layout.view_item_selectable_boolean_options, parent);
                break;

            case INSERT_BUYING_CAP:
                v = inflateView(R.layout.view_item_betting_cap, parent);
                break;

            case INSERT_REASON_MESSAGE:
                v = inflateView(R.layout.view_item_reason_write, parent);
                break;

            case RANKING_LIST:
                v = inflateView(R.layout.view_item_cast_ranking_list, parent);
                break;

            default:
                v = new View(c);
                break;
        }

        return new CompositeViewHolder(v);
    }

    @Override
    protected void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {
        if (mItemBindStrategy != null) {
            mItemBindStrategy.bindBodyItemView(holder, position, viewType, item);
        }
    }

    public ItemBindStrategy getItemBindStrategy()
    {
        return mItemBindStrategy;
    }

    public void setItemBindStrategy(ItemBindStrategy itemBindStrategy)
    {
        mItemBindStrategy = itemBindStrategy;
    }
}
