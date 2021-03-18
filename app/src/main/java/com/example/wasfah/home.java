package com.example.wasfah;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

import java.util.Locale;


public class home extends Fragment {

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;
    Switch s;

    public home() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static home getInstance(){return  new home();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        myFragment=inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);
        s = myFragment.findViewById(R.id.switch1);

        if (Pref.getValue(getContext(),"language_checked", "false").equalsIgnoreCase("true"))
        {
            s.setChecked(true);
            setApplicationLocale("ar");
        }
        else
        {
            s.setChecked(false);
            setApplicationLocale("en");
        }

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(s.isChecked()){
                    Pref.setValue(getContext(),"language_checked", "true");
                    setApplicationLocale("ar");                }
                else {
                    Pref.setValue(getContext(),"language_checked", "false");

                    setApplicationLocale("en");

                }

            }
        });
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
        adapter.addFragment(new HealthyFragment(),getString(R.string.Healthy));
        adapter.addFragment(new ItalianFragment(),getString(R.string.Italian));
        adapter.addFragment(new IndianFragment(),getString(R.string.Indian));
        adapter.addFragment(new SaudiFragment(),getString(R.string.Saudi));
        adapter.addFragment(new JapaneseFragment(),getString(R.string.Japanese));
        adapter.addFragment(new ChineseFragment(),getString(R.string.Chinese));
        adapter.addFragment(new AmericanFragment(),getString(R.string.American));
        adapter.addFragment(new FrenchFragment(),getString(R.string.French));
        adapter.addFragment(new DrinksFragment(),getString(R.string.Drinks));
        adapter.addFragment(new DesertsFragment(),getString(R.string.Deserts));
        adapter.addFragment(new BakeriesFragment(),getString(R.string.Bakeries));
        adapter.addFragment(new OthersFragment(),getString(R.string.Others));

        viewPager.setAdapter(adapter);
    }

    public void setApplicationLocale(String locale) {
        Resources resources = myFragment.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(locale.toLowerCase()));
        } else {
            config.locale = new Locale(locale.toLowerCase());
        }
        resources.updateConfiguration(config, dm);

    }



}