package com.example.carolinamarin.stylestumble.categories;

import com.example.carolinamarin.stylestumble.data.CategoriesRepository.LoadCategoriesCallback;
import com.example.carolinamarin.stylestumble.data.Category;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verify;


/**
 * Created by carolinamarin on 2/23/16.
 */
public class CategoriesPresenterTest {

    private static List<Category> CATEGORIES = Lists.newArrayList(new Category("123", "Description1","12"),
            new Category("123", "Description2","11"));

    private CategoriesPresenter mCategoriesPresenter;

    @Mock
    private CategoriesRepository mCategoriesRepository;

    @Mock
    private CategoriesContract.View mCategoriesView;

    @Captor
    private ArgumentCaptor<LoadCategoriesCallback> mLoadCategoriesCallbackCaptor;

    @Before
    public void setupCategoriesPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mCategoriesPresenter = new CategoriesPresenter(mCategoriesRepository, mCategoriesView);
    }




    @Test
    public void loadCategoriesFromRepositoryAndLoadIntoView() {
        // Given an initialized NotesPresenter with initialized notes
        // When loading of Notes is requested
        mCategoriesPresenter.loadCategories(true);

//        // Callback is captured and invoked with stubbed notes
        verify(mCategoriesRepository).getCategories(mLoadCategoriesCallbackCaptor.capture());
        mLoadCategoriesCallbackCaptor.getValue().onCategoriesLoaded(CATEGORIES);
//
//        // Then progress indicator is hidden and notes are shown in UI
//        verify(mNotesView).setProgressIndicator(false);
        verify(mCategoriesView).showCategories(CATEGORIES);
    }

}
