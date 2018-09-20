package com.casting.model;

public class DoublePieChartItem extends CommonGraphItem {

    private PieChartItem    PieChartItem1;
    private PieChartItem    PieChartItem2;

    private String ItemTitle;
    private String ItemBottomTitle;

    @Override
    public int getItemType()
    {
        return DOUBLE_PIE_CHART;
    }

    public PieChartItem getPieChartItem1() {
        return PieChartItem1;
    }

    public void setPieChartItem1(PieChartItem pieChartItem1) {
        PieChartItem1 = pieChartItem1;
    }

    public PieChartItem getPieChartItem2() {
        return PieChartItem2;
    }

    public void setPieChartItem2(PieChartItem pieChartItem2) {
        PieChartItem2 = pieChartItem2;
    }

    public String getItemTitle() {
        return ItemTitle;
    }

    public void setItemTitle(String itemTitle) {
        ItemTitle = itemTitle;
    }

    public String getItemBottomTitle() {
        return ItemBottomTitle;
    }

    public void setItemBottomTitle(String itemBottomTitle) {
        ItemBottomTitle = itemBottomTitle;
    }
}
