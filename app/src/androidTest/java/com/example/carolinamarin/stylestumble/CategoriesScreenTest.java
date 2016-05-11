package com.example.carolinamarin.stylestumble;

import android.test.suitebuilder.annotation.LargeTest;

/**
 * Created by carolinamarin on 2/24/16.
 */

//@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategoriesScreenTest {

//    /**
//     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
//     *
//     * <p>
//     * View constraints:
//     * <ul>
//     * <li>View must be a child of a {@link RecyclerView}
//     * <ul>
//     *
//     * @param itemText the text to match
//     * @return Matcher that matches text in the given view
//     */
////    private Matcher<View> withItemText(final String itemText) {
////        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
////        return new TypeSafeMatcher<View>() {
////            @Override
////            public boolean matchesSafely(View item) {
////                return allOf(
////                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
////                        withText(itemText)).matches(item);
////            }
////
////            @Override
////            public void describeTo(Description description) {
////                description.appendText("is isDescendantOfA RV with text " + itemText);
////            }
////        };
////    }
//
//    /**
//     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
//     *
//     * <p>
//     * Rules are interceptors which are executed for each test method and are important building
//     * blocks of Junit tests.
//     */
////    @Rule
////    public ActivityTestRule<NotesActivity> mNotesActivityTestRule =
////            new ActivityTestRule<>(NotesActivity.class);
//
//    @Test
//    public void clickAddCategoryButton_opensAddCategoryUi() throws Exception {
//        // Click on the add note button
//        onView(withId(R.id.fab)).perform(click());
//
//        // Check if the add note screen is displayed
//       // onView(withId(R.id.add_note_title)).check(matches(isDisplayed()));
//    }
//
////    @Test
////    public void addNoteToNotesList() throws Exception {
////        String newNoteTitle = "Espresso";
////        String newNoteDescription = "UI testing for Android";
////
////        // Click on the add note button
////        onView(withId(R.id.fab_add_notes)).perform(click());
////
////        // Add note title and description
////        onView(withId(R.id.add_note_title)).perform(typeText(newNoteTitle)); // Type new note title
////        onView(withId(R.id.add_note_description)).perform(typeText(newNoteDescription),
////                closeSoftKeyboard()); // Type new note description and close the keyboard
////
////        // Save the note
////        onView(withId(R.id.fab_add_notes)).perform(click());
////
////        // Scroll notes list to added note, by finding its description
////        onView(withId(R.id.notes_list)).perform(
////                scrollTo(hasDescendant(withText(newNoteDescription))));
////
////        // Verify note is displayed on screen
////        onView(withItemText(newNoteDescription)).check(matches(isDisplayed()));
////    }

}