package com.casting.commonmodule.view.component;

import android.support.v4.app.Fragment;
import android.view.View;

public class CommonFragment extends Fragment {



    @SuppressWarnings("unchecked")
    protected <V extends View> V find(int id)
    {
        return (getActivity() != null ? (V) getActivity().findViewById(id) : null);
    }
}
