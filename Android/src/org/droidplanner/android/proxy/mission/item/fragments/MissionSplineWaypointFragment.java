package org.droidplanner.android.proxy.mission.item.fragments;

import org.droidplanner.R;
import org.droidplanner.android.widgets.SeekBarWithText.SeekBarWithText;
import org.droidplanner.android.widgets.SeekBarWithText.SeekBarWithText.OnTextSeekBarChangedListener;
import org.droidplanner.android.widgets.spinnerWheel.AbstractWheel;
import org.droidplanner.android.widgets.spinnerWheel.OnWheelChangedListener;
import org.droidplanner.android.widgets.spinnerWheel.WheelHorizontalView;
import org.droidplanner.android.widgets.spinnerWheel.adapters.NumericWheelAdapter;
import org.droidplanner.core.helpers.units.Altitude;
import org.droidplanner.core.mission.MissionItemType;
import org.droidplanner.core.mission.waypoints.SplineWaypoint;
import org.droidplanner.core.mission.waypoints.Waypoint;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This class renders the detail view for a spline waypoint mission item.
 */
public class MissionSplineWaypointFragment extends MissionDetailFragment implements OnWheelChangedListener {

	@Override
	protected int getResource() {
		return R.layout.fragment_editor_detail_spline_waypoint;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        final Context context = getActivity().getApplicationContext();

		typeSpinner.setSelection(commandAdapter.getPosition(MissionItemType.SPLINE_WAYPOINT));

		SplineWaypoint item = (SplineWaypoint) this.itemRender.getMissionItem();

        final NumericWheelAdapter delayAdapter = new NumericWheelAdapter(context, 0, 60, "%d s");
        delayAdapter.setItemResource(R.layout.wheel_text_centered);
        final WheelHorizontalView delayPicker = (WheelHorizontalView) view.findViewById(R.id
                .waypointDelayPicker);
        delayPicker.setViewAdapter(delayAdapter);
        delayPicker.setCurrentItem(delayAdapter.getItemIndex((int) item.getDelay()));
        delayPicker.addChangingListener(this);

        final NumericWheelAdapter altitudeAdapter = new NumericWheelAdapter(context, 0, 200,
                "%d m");
        altitudeAdapter.setItemResource(R.layout.wheel_text_centered);
        final WheelHorizontalView altitudePicker = (WheelHorizontalView) view.findViewById(R.id
                .altitudePicker);
        altitudePicker.setViewAdapter(altitudeAdapter);
        altitudePicker.setCurrentItem(altitudeAdapter.getItemIndex((int)item.getCoordinate()
                .getAltitude().valueInMeters()));
        altitudePicker.addChangingListener(this);
	}

    @Override
    public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
        final SplineWaypoint item = (SplineWaypoint) this.itemRender.getMissionItem();

        switch(wheel.getId()){
            case R.id.altitudePicker:
                item.getCoordinate().getAltitude().set(newValue);
                break;

            case R.id.waypointDelayPicker:
                item.setDelay(newValue);
                break;
        }

        item.getMission().notifyMissionUpdate();
    }
}
