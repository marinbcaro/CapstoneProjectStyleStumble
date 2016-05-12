package com.chessen.project.stylestumble.categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chessen.project.stylestumble.R;
import com.chessen.project.stylestumble.data.Category;
import com.chessen.project.stylestumble.products.ProductsActivity;
import com.chessen.project.stylestumble.util.ThreeTwoImageView;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoriesFragment extends Fragment implements CategoriesContract.View  {

    private CategoriesContract.UserActionsListener mCategoriesListener;
    private CategoriesAdapter mCategoryAdapter;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryAdapter = new CategoriesAdapter(getActivity(),new ArrayList<Category>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCategoriesListener.loadCategories(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
        mCategoriesListener = new CategoriesPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.categories_list);

        recyclerView.setAdapter(mCategoryAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }


    /**
     * Listener for clicks on notes in the RecyclerView.
     */
    CategoryItemListener mItemListener = new CategoryItemListener() {
        @Override
        public void onCategoryClick(Category clickedCategory) {
            mCategoriesListener.showProducts(clickedCategory);
        }
    };

    @Override
    public void showAllProducts(String catId){
        Intent intent = new Intent(getContext(), ProductsActivity.class);
        intent.putExtra(ProductsActivity.CAT_ID, catId);
        startActivity(intent);
    }

    @Override
    public void showCategories(){

        ArrayList<Category> cateList=new ArrayList<>();
        Category women=new Category("women","Women", "women");
        Category men=new Category("men","Men", "men");
        Category kids=new Category("kids-and-baby","Kids", "kids-and-baby");

        cateList.add(women);
        cateList.add(men);
        cateList.add(kids);

        mCategoryAdapter.replaceData(cateList);
    }


    private static class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

        private List<Category> mCategories;
        private CategoryItemListener mItemListener;
        Context mContext;

        public CategoriesAdapter(Context context,List<Category> categories, CategoryItemListener itemListener) {
            setList(categories);
            mItemListener = itemListener;
            mContext=context;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View categoryView = inflater.inflate(R.layout.item_note, parent, false);

            return new ViewHolder(categoryView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Category category = mCategories.get(position);

           viewHolder.description.setText(category.getDescription());

            if(category.name.equals("women")){
                viewHolder.image.setImageResource(R.drawable.woman_category);

                viewHolder.image.setContentDescription("Category Women");
            }
            if(category.name.equals("men")){
                viewHolder.image.setImageResource(R.drawable.man_category);
                viewHolder.image.setContentDescription("Category Men");
            }
            if(category.name.equals("kids-and-baby")){
                viewHolder.image.setImageResource(R.drawable.kids_category);
                viewHolder.image.setContentDescription("Category Kids");
            }

        }

        public void replaceData(List<Category> categories) {
            setList(categories);
            notifyDataSetChanged();
        }

        private void setList(List<Category> categories) {
            mCategories = checkNotNull(categories);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }

        public Category getItem(int position) {
            return mCategories.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title;

            public TextView description;
            private CategoryItemListener mItemListener;
            private ThreeTwoImageView image;

            public ViewHolder(View itemView, CategoryItemListener listener) {
                super(itemView);
                mItemListener = listener;
                description = (TextView) itemView.findViewById(R.id.category_detail_description);
                image=(ThreeTwoImageView)itemView.findViewById(R.id.image_category);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Category category = getItem(position);
                mItemListener.onCategoryClick(category);

            }
        }
    }
    public interface CategoryItemListener {

        void onCategoryClick(Category clickedCategory);
    }

}
