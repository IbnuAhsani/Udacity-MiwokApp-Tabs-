package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by test-pc on 19-Jan-18.
 */

public class FragmentPageAdapter extends FragmentPagerAdapter {

    //Context of the App
    private Context context;

    /**
     * Create a new {@link FragmentPageAdapter} object.
     *
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public FragmentPageAdapter(FragmentManager fm, Context context)
        {
            super(fm);
            this.context = context;
        }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new FamilyMembersFragment();
        } else if (position == 2) {
            return new ColorsFragment();
        } else {
            return new PhrasesFragment();
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            {
                return context.getString(R.string.category_numbers);
            }
        else if(position == 1)
            {
                return context.getString(R.string.category_colors);
            }
        else if(position == 2)
            {
                return context.getString(R.string.category_family);
            }
        else
            {
                return context.getString(R.string.category_phrases);
            }
    }
}
