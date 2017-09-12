package harrel.idle;

import android.content.Context;
import android.graphics.Color;
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

import java.math.BigDecimal;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoldFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoldFragment extends ListFragment implements OnGoldDataChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainActivity mParent;
    private GoldEntry[] values;

    private MyListAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    public GoldFragment() {
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
    public static GoldFragment newInstance(String param1, String param2) {
        GoldFragment fragment = new GoldFragment();
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
        mParent.setOnGoldDataChangeListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gold, container,
                false);


        values = mParent.getGoldData();
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
                if(values[position].isAffordable()) {
                    mParent.addGoldCount(values[position].getCost().negate());
                    values[position].addCount(BigDecimal.ONE);
                    if(GoldEntry.lastOwnedPosition < position) {
                        GoldEntry.lastOwnedPosition = position;
                        mAdapter.notifyDataSetChanged();
                    }else {
                        onGoldDataChange(position);
                    }
                    mParent.recalculateGoldPerSec();
                }
            }
        });
    }

    public void onGoldDataChange(int position){

        int listPosition = position - getListView().getFirstVisiblePosition();
        View view = getListView().getChildAt(listPosition);

        if(view != null) {
            updateRow(view, position);
        }
    }

    private void updateRow(View view, int position){

        FontTextView cost = (FontTextView) view.findViewById(R.id.cost);
        cost.setText("Cost: " + FontTextView.valueToString(values[position].getCost()));
        if (!values[position].isAffordable())
            cost.setTextColor(Color.RED);
        else
            cost.setTextColor(getResources().getColor(R.color.colorFont));

        FontTextView owned = (FontTextView) view.findViewById(R.id.owned);
        owned.setText("Owned: " + values[position].getCount());

        view.setEnabled(values[position].isAffordable());
        if(values[position].isAffordable())
            view.setAlpha(1f);
        else
            view.setAlpha(0.65f);
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
        private final GoldEntry[] adapterValues;

        public MyListAdapter(Context context, GoldEntry[] values) {
            super(context, -1, values);
            this.context = context;
            this.adapterValues = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_gold, parent, false);
            FontTextView textName = (FontTextView)rowView.findViewById(R.id.name);
            textName.setText(adapterValues[position].getName());
            //FontTextView textCost = (FontTextView)rowView.findViewById(R.id.cost);
            //textCost.setText("Cost: " + FontTextView.goldToString(adapterValues[position].getCost()));
            FontTextView textPower = (FontTextView)rowView.findViewById(R.id.powerReq);
            textPower.setText("Required power: " + FontTextView.valueToString(adapterValues[position].getPowerReq()));
            FontTextView textGps = (FontTextView)rowView.findViewById(R.id.goldPerSec);
            textGps.setText("Zeni/s: " + FontTextView.valueToString(adapterValues[position].getGoldPerSec()));

            updateRow(rowView, position);

            return rowView;
        }

        @Override
        public int getCount(){
            return GoldEntry.lastOwnedPosition == adapterValues.length - 1 ? GoldEntry.lastOwnedPosition + 1 : GoldEntry.lastOwnedPosition + 2;
        }
    }
}
