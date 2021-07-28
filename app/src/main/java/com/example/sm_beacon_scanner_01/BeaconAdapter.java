package com.example.sm_beacon_scanner_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

public class BeaconAdapter extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Beacon> beaconList = null;

    public BeaconAdapter(Context context, ArrayList<Beacon> beaconList) {
        this.context = context;
        this.beaconList = beaconList;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return beaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return beaconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = layoutInflater.inflate(R.layout.item_beacon, null);
//
//        TextView tvUuid = view.findViewById(R.id.tvUuid);
//        TextView tvMajor = view.findViewById(R.id.tvMajor);
//        TextView tvMinor = view.findViewById(R.id.tvMinor);
//        TextView tvRssi = view.findViewById(R.id.tvRssi);
//        TextView tvTx = view.findViewById(R.id.tvTx);
//        TextView tvFloor = view.findViewById(R.id.tvFloor);
//
//        tvUuid.setText((CharSequence) beaconList.get(position).getId1());
//        tvUuid.setText((CharSequence) beaconList.get(position).getId2());
//        tvUuid.setText((CharSequence) beaconList.get(position).getId3());
//        tvUuid.setText(beaconList.get(position).getRssi());
//        tvUuid.setText(beaconList.get(position).getTxPower());
//        tvUuid.setText("아직처리x");
//
//        return view;
//    }
@Override
public View getView(int position, View convertView, ViewGroup parent) {
    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beacon, parent, false);

    TextView tvUuid = (TextView)convertView.findViewById(R.id.tvUuid);
    TextView tvMajor = convertView.findViewById(R.id.tvMajor);
    TextView tvMinor = convertView.findViewById(R.id.tvMinor);
    TextView tvRssi = convertView.findViewById(R.id.tvRssi);
    TextView tvTx = convertView.findViewById(R.id.tvTx);
    TextView tvFloor = convertView.findViewById(R.id.tvFloor);

    tvUuid.setText((CharSequence) beaconList.get(position).getId1());
    tvMajor.setText((CharSequence) beaconList.get(position).getId2());
    tvMinor.setText((CharSequence) beaconList.get(position).getId3());
    tvRssi.setText(beaconList.get(position).getRssi());
    tvTx.setText(beaconList.get(position).getTxPower());
    tvFloor.setText("아직처리x");

    return convertView;
}
}
