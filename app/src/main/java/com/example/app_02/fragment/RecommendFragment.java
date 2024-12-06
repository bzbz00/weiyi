package com.example.app_02.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_02.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendFragment extends Fragment {

    private TextView currentSelectedTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecommendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_recomend.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendFragment newInstance(String param1, String param2) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //设置初始fragment为all
        loadFragment(new fragment_all());

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         //Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_recomend, container, false);

        //创建一个view控件的实例，注：该控件的实例必须在oncreateview方法中，且参数必须是LayoutInflater inflater, ViewGroup container,
        //                             Bundle savedInstanceState，利用rootview可以调用findViewById(),便于添加点击监听事件
        View rootview = inflater.inflate(R.layout.fragment_recomend, container, false);


        //获取textview控件的实例
        TextView textview_all, textview_story, textview_game, textview_knowledge, textview_play, textview_movie, textview_anime, textview_physical, textview_novel;

        //对实例初始化
        textview_all = rootview.findViewById(R.id.textview_all);
        textview_story = rootview.findViewById(R.id.textview_story);
        textview_game = rootview.findViewById(R.id.textview_game);
        textview_knowledge = rootview.findViewById(R.id.textview_knowledge);
        textview_play = rootview.findViewById(R.id.textview_play);
        textview_movie = rootview.findViewById(R.id.textview_movie);
        //textview_anime = rootview.findViewById(R.id.textview_anime);
        //textview_physical = rootview.findViewById(R.id.textview_physical);
        //textview_novel = rootview.findViewById(R.id.textview_novel);


        //设置点击事件，当点击textview跳转到指定的fragment
        textview_all.setOnClickListener(View ->{loadFragment(new fragment_all());});
        textview_story.setOnClickListener(View -> {loadFragment(new fragment_story());});
        textview_game.setOnClickListener(View -> {loadFragment(new fragment_game());});
        textview_knowledge.setOnClickListener(View -> {loadFragment(new fragment_knowledge());});
        textview_play.setOnClickListener(View -> {loadFragment(new fragment_play());});
        textview_movie.setOnClickListener(view -> {loadFragment(new fragment_movie());});
        //textview_anime.setOnClickListener(View -> {loadFragment(new fragment_anime());});
        //textview_physical.setOnClickListener(View -> {loadFragment(new fragment_physical());});
        //textview_novel.setOnClickListener(View -> {loadFragment(new fragment_novel());});

        return rootview;

    }

    // 定义一个方法用于加载并替换 Fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_container_recomend, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}