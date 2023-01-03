package com.oneness.fdxmerchant.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.oneness.fdxmerchant.Fragments.TabFragments.CanceledOrdersFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.DeliveredOrdersFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.NewOrdersFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OngoingOrdersFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    Context mContext;
    int mTotalTabs;

    public TabLayoutAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
       // this.mContext = fragmentManager;
        this.mTotalTabs = tabCount;
    }


    //String uId;




    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("asasas" , position + "");
        switch (position) {
            case 0:
                return new NewOrdersFragment();
            case 1:
                return new OngoingOrdersFragment();
            case 2:
                return new DeliveredOrdersFragment();
            case 3:
                return new CanceledOrdersFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}
