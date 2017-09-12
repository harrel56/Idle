package harrel.idle;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;



public class PowerLevelFragment extends ListFragment implements OnPowerDataChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PowerEntry[] values;

    private MainActivity mParent;
    private MyListAdapter mAdapter;
    private AnimatingProgressBar mBar;

    public PowerLevelFragment() {
        // Required empty public constructor
    }

    public static PowerLevelFragment newInstance(String param1, String param2) {
        PowerLevelFragment fragment = new PowerLevelFragment();
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

        mParent = (MainActivity)getActivity();
        mParent.setOnPowerDataChangeListener(this);
        mBar = new AnimatingProgressBar(mParent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gold, container,
                false);


        values = mParent.getPowerData();
        mAdapter = new MyListAdapter(getContext(), values);
        setListAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(view.isEnabled()) {
                    ListView list = PowerLevelFragment.this.getListView();
                    for (int i = 0; i < list.getChildCount(); i++) {
                        View v = list.getChildAt(i);
                        if (v != view && v != null) {
                            v.setEnabled(false);
                            v.setAlpha(0.6f);
                        }
                    }
                    AnimatingProgressBar bar = (AnimatingProgressBar) view.findViewById(R.id.progressBar);
                    bar.setAnimate(false);
                    bar.setProgress(0);
                    bar.setAnimate(true);
                    values[position].setTimeDone(0);
                    mParent.setCurrentTrainingIndex(position);

                    FontButton button = (FontButton) view.findViewById(R.id.cancel);
                    button.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onPowerDataChange(int position, int progress){

        int listPosition = position - getListView().getFirstVisiblePosition();
        View view = getListView().getChildAt(listPosition);

        if(view != null) {
            AnimatingProgressBar bar = (AnimatingProgressBar) view.findViewById(R.id.progressBar);
            if(progress < 100) {
                bar.setProgress(progress);
            }else{
                bar.setProgress(100);
                values[position].setTimeDone(-1);
                mParent.setCurrentTrainingIndex(-1);

                FontButton button = (FontButton) view.findViewById(R.id.cancel);
                button.setVisibility(View.GONE);

                ListView list = PowerLevelFragment.this.getListView();
                for (int i = 0; i < list.getChildCount(); i++) {
                    View v = list.getChildAt(i);
                    if(v != null) {
                        v.setEnabled(true);
                        v.setAlpha(1f);
                    }
                }
            }
        }
    }

    public class MyListAdapter extends ArrayAdapter<PowerEntry> {
        private final Context context;
        private final PowerEntry[] values;

        public MyListAdapter(Context context, PowerEntry[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_power, parent, false);
            FontTextView textName = (FontTextView)rowView.findViewById(R.id.name);
            textName.setText(values[position].getName());
            FontTextView textPowerReq = (FontTextView)rowView.findViewById(R.id.powerReq);
            textPowerReq.setText("Required power: " + FontTextView.valueToString(values[position].getPowerReq()));
            FontTextView textPowerPerSec = (FontTextView)rowView.findViewById(R.id.powerPerSec);
            textPowerPerSec.setText("Power/s: " + FontTextView.valueToString(values[position].getPowerPerSec()));
            FontTextView textTime = (FontTextView)rowView.findViewById(R.id.time);
            textTime.setText("Time: " + FontTextView.timeToString(values[position].getTime()));

            AnimatingProgressBar bar = (AnimatingProgressBar) rowView.findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);
            if(values[position].getTimeDone() >= 0) {

                bar.setAnimate(false);
                bar.setProgress((int) (values[position].getTimeDone() / values[position].getTime() * 100));
                bar.setAnimate(true);

                FontButton button = (FontButton) rowView.findViewById(R.id.cancel);
                button.setVisibility(View.VISIBLE);
            }

            if(mParent.getCurrentTrainingIndex() != -1 && mParent.getCurrentTrainingIndex() != position){
                rowView.setEnabled(false);
                rowView.setAlpha(0.6f);
            }else{
                rowView.setEnabled(true);
                rowView.setAlpha(1f);
            }

            return rowView;
        }
    }
}
