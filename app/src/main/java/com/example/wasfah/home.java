package com.example.wasfah;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

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
import com.example.wasfah.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


public class home extends Fragment {

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;

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
//        s = myFragment.findViewById(R.id.switch1);
//
//        s.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(s.isChecked()){
//                    Pref.setValue(getContext(),"language_checked", "true");
//                    String languageToLoad  = "ar"; // your language
//                    Locale locale = new Locale(languageToLoad);
//                    Locale.setDefault(locale);
//                    Configuration config = new Configuration();
//                    config.locale = locale;
//                    getContext().getResources().updateConfiguration(config,
//                            getContext().getResources().getDisplayMetrics());
//                    Fragment f= new home();
//                    getActivity().getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.fragment_container, f)
//                            .commit();
////                    final FragmentManager fragmentManager =  getActivity().getSupportFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    Fragment home=null;
////                    if (fragmentManager.findFragmentByTag("mHome") == null) {
////                        home = new home();
////                        fragmentTransaction.replace(R.id.fragment_container, home, "mHome");
////                    } else {
////                        fragmentTransaction.show(home);
////                    }
////                    fragmentTransaction.commit();
////                    ((MainActivity)getActivity()).restartActivity();
////                    FragmentTransaction ft = getActivity().getSupportFragmentManager();
////                    ft.replace(R.id.content_frame, fragment);
////                    ft.commit();
////                    getSupportFragmentManager().executePendingTransactions();
//                }
//                else {
//                    Pref.setValue(getContext(),"language_checked", "false");
//                    String languageToLoad  = "en"; // your language
//                    Locale locale = new Locale(languageToLoad);
//                    Locale.setDefault(locale);
//                    Configuration config = new Configuration();
//                    config.locale = locale;
//                    getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
//                }
//            }
//        });
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
}