package com.openxu.md;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;
import com.openxu.md.adapter.CommandItemDecoration;
import com.openxu.md.adapter.CommandRecyclerAdapter;
import com.openxu.md.adapter.ViewHolder;
import com.openxu.md.bean.MainItem;
import com.openxu.md.chartactivity.ChartActivity1;
import com.openxu.md.chartactivity.ChartViewPagerActivity;
import com.openxu.md.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.openxu.md.adapter.CommandItemDecoration.VERTICAL_LIST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        List<MainItem> list = new ArrayList<>();
        list.add(new MainItem(1, "单个控件交互"));
        list.add(new MainItem(2, "多个控件交互"));
        list.add(new MainItem(3, "嵌套滚动"));
        list.add(new MainItem(4, "图表事件1"));
        list.add(new MainItem(5, "图表事件2"));
        list.add(new MainItem(6, "图表事件3"));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new CommandItemDecoration(this, VERTICAL_LIST, Color.RED,1));
        binding.recyclerView.setAdapter(new CommandRecyclerAdapter<MainItem>(this, R.layout.main_item, list) {
            @Override
            public void convert(ViewHolder holder, MainItem o, int position) {
                holder.getBinding().setVariable(BR.item, o);
            }

            @Override
            public void onItemClick(MainItem data, int position) {
                switch (data.getId()){
                    case 1:
                        startActivity(new Intent(MainActivity.this, OneViewActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, MoreViewActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, NestedScrollActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, ChartActivity1.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, ChartActivity1.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, ChartViewPagerActivity.class));
                        break;
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
