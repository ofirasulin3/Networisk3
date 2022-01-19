package com.example.networisk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class WifiReceiver extends BroadcastReceiver {
    WifiManager wifiManager;
    ListView wifiDeviceList;
    Location focalLocation;
    Location currentLocation;
    //StringBuilder sb;

    public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList, Location FocalLocation, Location CurrentLocation) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
        this.focalLocation = FocalLocation;
        this.currentLocation = CurrentLocation;
    }
    public void onReceive(Context context, Intent intent) {
        //calculating distance (in meters) between currentLocation and focalLocation
        if(focalLocation!=null && currentLocation!=null){
            float dist = focalLocation.distanceTo(currentLocation);

            //Check if it's in a given radius
            if(dist>=200){
                Log.i("Prints", "currentLocation far from FocalLocation so doesn't update list.");
                return;
            }
            else{
                Log.i("Prints", "currentLocation is within radius");
            }
        }

        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            //sb = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();
            ArrayList<String> deviceList = new ArrayList<>();
            for (ScanResult scanResult : wifiList) {
                long microseconds = scanResult.timestamp;
                long days = TimeUnit.MICROSECONDS.toDays(microseconds);
                deviceList.add("                      " + scanResult.SSID
                        + "\nCapabilities: " + scanResult.capabilities
                        + "\nBSSID: " + scanResult.BSSID
                        + "\nLevel: " + scanResult.level
                        + "\nTimestamp: " + days +" days"
                        + "\nVenue Name: " + scanResult.venueName// This was deprecated in API 31
                        + "\nPasspoint friendly name: " + scanResult.operatorFriendlyName //newer name=getPasspointProviderFriendlyName
                        + "\nIs it passpoint? " + scanResult.isPasspointNetwork());
            }
            //Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, deviceList.toArray());
            wifiDeviceList.setAdapter(arrayAdapter);
        }
    }
}