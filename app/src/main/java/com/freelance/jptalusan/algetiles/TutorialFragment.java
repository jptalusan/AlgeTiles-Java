package com.freelance.jptalusan.algetiles;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

public class TutorialFragment extends Fragment {
    private int mPosition;

    public TutorialFragment(){
    }

    public static TutorialFragment newInstance(int pos){
        TutorialFragment frag = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("pos");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial, container, false);
        ImageView backgroundView = v.findViewById(R.id.imageview_card);
        VideoView videoView = v.findViewById(R.id.videoview_card);

        backgroundView.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);

//        if(videoView.isPlaying())
            videoView.stopPlayback();

        switch(mPosition){
            case 0:
                Picasso.with(getContext()).load(R.drawable.slide1).into(backgroundView);
                break;
            case 1:
                Picasso.with(getContext()).load(R.drawable.slide2).into(backgroundView);
                break;
            case 2:
                Picasso.with(getContext()).load(R.drawable.slide3).into(backgroundView);
                break;
            case 3:
                Picasso.with(getContext()).load(R.drawable.slide4).into(backgroundView);
                break;
            case 4:
                Picasso.with(getContext()).load(R.drawable.slide5).into(backgroundView);
                break;
            case 5:
                Picasso.with(getContext()).load(R.drawable.slide6).into(backgroundView);
                break;
            case 6:
                Picasso.with(getContext()).load(R.drawable.slide7).into(backgroundView);
                break;
            case 7:
                Picasso.with(getContext()).load(R.drawable.slide8).into(backgroundView);
                break;
            case 8:
                backgroundView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                MediaController mc= new MediaController(getActivity());

                String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.factor_mod;
                videoView.setVideoURI(Uri.parse(path));
                videoView.setMediaController(mc);
                videoView.start();
                break;
            case 9:
                backgroundView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                MediaController mc2= new MediaController(getActivity());

                String path2 = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.multiply_mod;
                videoView.setVideoURI(Uri.parse(path2));
                videoView.setMediaController(mc2);
                videoView.start();
                break;
            default:
                //set background view image 3
        }
        return v;
    }

//    @Override
//    protected void onHiddenChanged() {
//
//    }
}
