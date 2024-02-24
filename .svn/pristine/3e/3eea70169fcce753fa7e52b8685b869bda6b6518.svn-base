package adapter;


import fragment.AinoSofiaFragment;
import fragment.FarahFragment;
import fragment.JonHenryFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new JonHenryFragment();
		case 1:
			// Games fragment activity
			return new AinoSofiaFragment();
		case 2:
			// Movies fragment activity
			return new FarahFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
