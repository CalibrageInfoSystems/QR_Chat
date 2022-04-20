package com.cis.qrchat.common;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;



public abstract class BaseFragment extends Fragment {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup parent, Bundle savedInstanseState)
    {
        View view = provideYourFragmentView(inflater,parent,savedInstanseState);
        return view;
    }

    public abstract View provideYourFragmentView(LayoutInflater inflater,ViewGroup parent, Bundle savedInstanceState);

}


