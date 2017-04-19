package Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.chris.mystats_univeristy.UcasTips;
import FragmentSelecters.UcasTipsFragmentSelecter;

/**
 * Created by Terence Lawson on 18/04/2017.
 */

public class UcasTipsAdapter extends FragmentPagerAdapter{
    private String[] title = new String[]{"Intro","Choosing your Course","Personal Statement", "Interview" };
    Context context;
    private int pageCount = 4;

    public UcasTipsAdapter(FragmentManager fm, UcasTips ucasTips) {
        super(fm);
    }
    /**
     * sends  position to the fragment selector.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        UcasTipsFragmentSelecter frag = new UcasTipsFragmentSelecter(position);
        return frag;    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int pos){
        return title[pos];
    }}

