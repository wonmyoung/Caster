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

            case CAST_CARD_THIN:
                v = inflateView(R.layout.view_item_cast_card_wide_thin, parent);
                break;

            case CAST_CARD_LONG:
                v = inflateView(R.layout.view_item_cast_card_wide, parent);
                break;

            case CHART_ITEM:
                v = inflateView(R.layout.view_item_chart, parent);
                break;

            case TIME_LINE:
                v = inflateView(R.layout.view_item_timeline, parent);
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
