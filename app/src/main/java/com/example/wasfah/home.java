package com.example.wasfah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wasfah.HomeFragments.AmericanFragment;
import com.example.wasfah.HomeFragments.BakeriesFragment;
import com.example.wasfah.HomeFragments.ChineseFragment;
import com.example.wasfah.HomeFragments.DesertsFragment;
import com.example.wasfah.HomeFragments.DrinksFragment;
import com.example.wasfah.HomeFragments.FrenchFragment;
import com.example.wasfah.HomeFragments.HealthyFragment;
import com.example.wasfah.HomeFragments.IndianFragment;
import com.example.wasfah.HomeFragments.ItalianFragment;
import com.example.wasfah.HomeFragments.JapaneseFragment;
import com.example.wasfah.HomeFragments.OthersFragment;
import com.example.wasfah.HomeFragments.SaudiFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


public class home extends Fragment {

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;

    public home() {
    }

    public static home getInstance(){return  new home();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        myFragment=inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        HomeAdapter adapter=new HomeAdapter(getChildFragmentManager());
        adapter.addFragment(new HealthyFragment(),"Healthy");
        adapter.addFragment(new ItalianFragment(),"Italian");
        adapter.addFragment(new IndianFragment(),"Indian");
        adapter.addFragment(new SaudiFragment(),"Saudi");
        adapter.addFragment(new JapaneseFragment(),"Japanese");
        adapter.addFragment(new ChineseFragment(),"Chinese");
        adapter.addFragment(new AmericanFragment(),"American");
        adapter.addFragment(new FrenchFragment(),"French");
        adapter.addFragment(new DrinksFragment(),"Drinks");
        adapter.addFragment(new DesertsFragment(),"Deserts");
        adapter.addFragment(new BakeriesFragment(),"Bakeries");
        adapter.addFragment(new OthersFragment(),"Others");

        viewPager.setAdapter(adapter);
    }
}