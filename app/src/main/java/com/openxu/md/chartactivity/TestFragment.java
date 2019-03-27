package com.openxu.md.chartactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openxu.md.R;
import com.openxu.md.databinding.FragmentChartBinding;
import com.openxu.md.util.FConversUtils;
import com.openxu.md.view.chart.BarBean;
import com.openxu.md.view.chart.piechart.ChartLable;
import com.openxu.md.view.chart.piechart.PieChartBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * Author: openXu
 * Time: 2019/3/26 18:28
 * class: TestFragment
 * Description:
 */
public class TestFragment extends Fragment {
    private int color;
    public static TestFragment newInstance(int color) {
        Bundle args = new Bundle();
        args.putInt("color", color);
        TestFragment pageFragment = new TestFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        color = getArguments().getInt("color");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentChartBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart, container, false);
//        binding.tv1.setBackgroundColor(color);
//        binding.tv2.setBackgroundColor(color);
        binding.barChart.setLoading(true);
        binding.barChart.setDebug(false);
        binding.barChart.setBarNum(2);
        binding.barChart.setBarSpace(0);
        binding.barChart.setBarItemSpace(30);
        binding.barChart.setShowLable(false);
        binding.barChart.setBaseLineAndText(false);
        binding.barChart.setBarColor(new int[]{Color.parseColor("#f9e5c9"), Color.parseColor("#c7e3ae")});
        binding.barChart.setLoading(false);
        List<String> strXList = new ArrayList<>();
        List<List<BarBean>> dataList1 = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            List<BarBean> list = new ArrayList<>();
            list.add(new BarBean(new Random().nextInt(100), ""));
            list.add(new BarBean(new Random().nextInt(100), ""));
            dataList1.add(list);
            strXList.add((i+1) + "月");
        }
        binding.barChart.setData(dataList1, strXList);

        binding.myPieChart.setDebug(false);
        binding.myPieChart.setLoading(true);
        binding.myPieChart.setRingWidth(FConversUtils.dip2px(getContext(), 16));
        binding.myPieChart.setLoading(false);
        List<Object> dataList = new ArrayList<>();
        List<ChartLable> tableList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add(new PieChartBean(new Random().nextInt(10), "name"+i));
        }
        tableList.add(new ChartLable("分类", FConversUtils.sp2px(getContext(), 12),
                getResources().getColor(R.color.text_color_light_gray)));
        binding.myPieChart.setChartData(PieChartBean.class, "num", "name", dataList, tableList);


        return binding.getRoot();
    }
}
