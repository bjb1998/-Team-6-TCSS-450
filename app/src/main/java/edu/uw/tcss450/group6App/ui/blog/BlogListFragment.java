package edu.uw.tcss450.group6App.ui.blog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentBlogListBinding;

/**
 * A simple {@link Fragment} subclass.

 */
public class BlogListFragment extends Fragment {

    private edu.uw.tcss450.group6App.ui.blog.BlogListViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        //if (view instanceof RecyclerView) {
            //Try out a grid layout to achieve rows AND columns. Adjust the widths of the
            //cards on display
// ((RecyclerView) view).setLayoutManager(new GridLayoutManager(getContext(), 2));
            //Try out horizontal scrolling. Adjust the widths of the card so that it is
            //obvious that there are more cards in either direction. i.e. don't have the cards
            //span the entire witch of the screen. Also, when considering horizontal scroll
            //on recycler view, ensure that thre is other content to fill the screen.
// ((LinearLayoutManager)((RecyclerView) view).getLayoutManager())
// .setOrientation(LinearLayoutManager.HORIZONTAL);
          //  ((RecyclerView) view).setAdapter(
            //        new BlogRecyclerViewAdapter(BlogGenerator.getBlogList()));
        //}
        return inflater.inflate(R.layout.fragment_blog_list, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(edu.uw.tcss450.group6App.ui.blog.BlogListViewModel.class);
        mModel.connectGet();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentBlogListBinding binding = FragmentBlogListBinding.bind(getView());

        mModel.addBlogListObserver(getViewLifecycleOwner(), blogList -> {
            if (!blogList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new edu.uw.tcss450.group6App.ui.blog.BlogRecyclerViewAdapter(blogList)
                );
                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }


}