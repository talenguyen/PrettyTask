package com.tale.prettytask.sample;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tale.prettytask.Action;
import com.tale.prettytask.OnResult;
import com.tale.prettytask.PrettyTask;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private String TAG = PlaceholderFragment.class.getSimpleName();
        private PrettyTask<String> mPrettyTask;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
        ) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            final TextView textView = (TextView) view.findViewById(R.id.tvMessage);
            textView.setText("Start");
            PrettyTask.create(new Action<String>() {
                @Override public String call() {
                    return "Success";
                }
            }).onResult(new OnResult<String>() {
                @Override public void onSucceed(String result) {
                    SystemClock.sleep(3000);
                    textView.setText(result);
                }

                @Override public void onError(Throwable throwable) {

                }

                @Override public void onCompleted() {

                }
            }).execute();
        }

        @Override public void onPause() {
            super.onPause();
        }

        @Override public void onResume() {
            super.onResume();
        }

        @Override public void onDestroyView() {
            super.onDestroyView();
        }
    }
}
