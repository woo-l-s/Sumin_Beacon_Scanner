package com.example.sm_beacon_scanner_01;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    private BeaconManager beaconManager = null;
    private static final String IBEACON_LAYOUT =  "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String TAG = "MainActivity";

    ArrayList<Beacon> beaconList = new ArrayList<>();
    BeaconAdapter beaconAdapter;
    ListView listView;

    TextView tvFloor, tvBeacons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFloor = findViewById(R.id.tvFloor);
        tvBeacons = findViewById(R.id.tvBeacons);
        listView = findViewById(R.id.listView);
        beaconAdapter = new BeaconAdapter(getApplicationContext(), beaconList);
        listView.setAdapter(beaconAdapter);


        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234); //권한 요청

        beaconManager = BeaconManager.getInstanceForApplication(this); //싱글톤 비콘 매니저 객체
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT)); //아이비콘 레이아웃 설정
        beaconManager.bind(this); //비콘매니저를 beaconConsumer 인터페이스를 구현한 클래스와 bind시켜야함

        startBeaconMonitoring();
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect called");
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d(TAG, "didRangeBeaconsInRegion called");

                if (beacons != null && !beacons.isEmpty()) {
                    beaconList = (ArrayList<Beacon>) beacons;
                    Log.d(TAG, beaconList.toString());
                    Log.d(TAG, "인식되는 비콘 갯수: " + String.valueOf(beaconList.size()));
                    String beaconStr = "";
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < beaconList.size(); i++) {
                        Log.d(TAG, i+"'s beacon UUID:" + beaconList.get(i).getId1() + ", Major: " + beaconList.get(i).getId2() +
                                ", Minor: " + beaconList.get(i).getId3() + ", RSSI: " + beaconList.get(i).getRssi());

                        double roundedDist = Math.round(beaconList.get(i).getDistance()*100)/100.0; // 소수점 두자리에서 반올림한 값으로 출력
                        sb.append(i + "번쨰 beacon UUID:" + beaconList.get(i).getId1()
                                + "\nMajor: " + beaconList.get(i).getId2()
                                + "\nMinor: " + beaconList.get(i).getId3()
                                + "\nRSSI: " + beaconList.get(i).getRssi()
                                + "\n거리: " + roundedDist + "\n\n");

                    }
                    tvBeacons.setText(sb.toString());
//                        showAlert("didRangeBeaconsInRegion 호출됨", "Ranging region " + region.getUniqueId() +
//                                " Beacon detected UUID/Major/Minor: " + beacon.getId1() +
//                                "/" + beacon.getId2() + "/" + beacon.getId3());
//                    }
//                    rangingMessageRaised = true;
//                    for (Beacon beacon : beacons) {
//                        showAlert("didRangeBeaconsInRegion 호출됨", "Ranging region " + region.getUniqueId() +
//                                " Beacon detected UUID/Major/Minor: " + beacon.getId1() +
//                                "/" + beacon.getId2() + "/" + beacon.getId3());
//                    }
//                    rangingMessageRaised = true;

                    beaconAdapter.notifyDataSetChanged();

                    //층
                    Collections.sort(beaconList, new Comparator<Beacon>() {
                        @Override
                        public int compare(Beacon o1, Beacon o2) {
                            if (o1.getRssi() < o2.getRssi()) return 1;
                            else return -1;
                        }
                    });
                    if (beaconList.get(0).getId3().toInt() == 64105) tvFloor.setText("2층");
                    else if (beaconList.get(0).getId3().toInt() == 64101) tvFloor.setText("1층");
                    else  tvFloor.setText("?층");


                }
            }
        });
    }
    Region beaconRegion;

    private void startBeaconMonitoring() {
        Log.d(TAG, "startBeaconMonitoring 호출됨");
        try {
//             beaconRegion = new Region("MyBeacons", Identifier.parse("0AC59CA4-DFA6-442C-8C65-22247851344C"), Identifier.parse("4"), Identifier.parse("200"));
//             beaconRegion = new Region("MyBeacons", null, null, null);
            beaconRegion = new Region("MyBeacons", null, Identifier.parse("40010"), null);

//            beaconManager.startMonitoringBeaconsInRegion(beaconRegion); //장치가 비콘 영역에 입장/퇴장하는 것을 모니터링 시작
            beaconManager.startRangingBeaconsInRegion(beaconRegion);   //비콘영역 ranging 시작
        } catch(RemoteException e) {
            e.printStackTrace();
        }
    }
}