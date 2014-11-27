package com.tale.prettytask;

import android.os.Bundle;
import android.os.SystemClock;
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

import com.tale.prettytask.functions.Action1;
import com.tale.prettytask.functions.Function0;
import com.tale.prettytask.functions.Function1;


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

        private TaskHandler taskHandler;

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
            taskHandler = Async.create(
                    new Function0<Long>() {
                        @Override public Long call() {
                            SystemClock.sleep(5000);
                            return System.currentTimeMillis();
                        }
                    }
            ).map(
                    new Function1<Long, Long>() {
                        @Override public Long call(Long integer) {
                            Log.d("PrettyTask", "map: " + integer);
                            return integer + 1111;
                        }
                    }
            ).map(
                    new Function1<String, Long>() {
                        @Override public String call(Long integer) {
                            Log.d("PrettyTask", "map: " + integer);
                            return String.valueOf(integer);
                        }
                    }
            )
                    .execute(
                            new Action1<String>() {
                                @Override public void call(String s) {
                                    Log.d("PrettyTask", "onResult: " + s);
                                    textView.setText(s);
                                }
                            }, null, null
                    );
        }

        @Override public void onPause() {
            super.onPause();
            taskHandler.pause();
        }

        @Override public void onResume() {
            super.onResume();
            taskHandler.resume();
        }

        @Override public void onDestroyView() {
            super.onDestroyView();
            taskHandler.stop();
        }
    }
}
