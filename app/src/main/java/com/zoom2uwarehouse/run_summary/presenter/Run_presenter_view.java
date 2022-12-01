package com.zoom2uwarehouse.run_summary.presenter;

import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public interface Run_presenter_view {

    void call_for_pickup(SharedPreferenceManager token);

    void call_for_progress(SharedPreferenceManager token);

}
