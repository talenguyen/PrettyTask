package com.tale.prettytask.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tale.prettytask.OnResult;
import com.tale.prettytask.PrettyTask;
import com.tale.prettytask.tasks.Task;


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
            mPrettyTask = PrettyTask.create(
                    new Task<String>() {
                        @Override public String call() {
                            try {
                                Log.d(TAG, String.format("Start at time: %d", System.currentTimeMillis()));
                                Thread.sleep(5000);
                                return String.format("Time: %d", System.currentTimeMillis());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }
            ).onResult(
                    new OnResult<String>() {
                        @Override public void onSucceed(String result) {
                            Log.d(TAG, "onSucceed: " + result);
                            textView.setText(String.format("Succeed at: %d", System.currentTimeMillis()));
                        }

                        @Override public void onError(Throwable throwable) {
                            Log.d(TAG, "onSucceed: " + throwable.getMessage());
                        }

                        @Override public void onCompleted() {
                            Log.d(TAG, "onCompleted: " + System.currentTimeMillis());
                        }
                    }
            ).execute();
        }

        @Override public void onPause() {
            mPrettyTask.pause();
            super.onPause();
        }

        @Override public void onResume() {
            super.onResume();
            mPrettyTask.resume();
        }

        @Override public void onDestroyView() {
            mPrettyTask.stop();
            super.onDestroyView();
        }
    }
}
