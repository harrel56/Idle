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
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpgradesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpgradesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpgradesFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoldEntry[] values = new GoldEntry[1];

    private OnFragmentInteractionListener mListener;

    public UpgradesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoldFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpgradesFragment newInstance(String param1, String param2) {
        UpgradesFragment fragment = new UpgradesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gold, container,
                false);

        InputStream jsonStream = getResources().openRawResource(R.raw.gold_entries);
        try {
            JSONObject jsonObject = new JSONObject(convertStreamToString(jsonStream));
            JSONArray jsonEntries = jsonObject.getJSONArray("goldEntries");
            values = new GoldEntry[jsonEntries.length()];

            for(int i = 0; i < jsonEntries.length(); i++){
                values[i] = new GoldEntry(jsonEntries.getJSONObject(i));
                Log.d("obj", values[i].toString());
            }
        }catch(Exception e){
            Log.d("JSON ERROR", e.toString());
        }

        //setListAdapter(new MyListAdapter(getContext(), values));
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class MyListAdapter extends ArrayAdapter<GoldEntry> {
        private final Context context;
        private final GoldEntry[] values;

        public MyListAdapter(Context context, GoldEntry[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_gold, parent, false);
            FontTextView textName = (FontTextView)rowView.findViewById(R.id.name);
            textName.setText(values[position].getName());
            FontTextView textCost = (FontTextView)rowView.findViewById(R.id.cost);
            textCost.setText("Cost: " + values[position].getCost());
            FontTextView textPower = (FontTextView)rowView.findViewById(R.id.powerReq);
            textPower.setText("Required power: " + values[position].getPowerReq());
            FontTextView textGps = (FontTextView)rowView.findViewById(R.id.goldPerSec);
            textGps.setText("Gold/s: " + values[position].getGoldPerSec());
            return rowView;
        }
    }
}
