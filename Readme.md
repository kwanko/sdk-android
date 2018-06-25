# Kwanko Android SDK



## Requirements

* Android 4.0.3 or later, at least API level 15
* Your slot UID from Kwanko

## SDK Instalation

After you have cloned the sdk source code into your local machine you will have to import the kwanko sdk module into your project. An official training on how to import an android module can be found here: https://developer.android.com/studio/projects/add-app-module.html#ImportAModule.

#### Library structure:
The Kwanko Sdk android library has the following structure:

* **kwanko-sdk_** - shell library which has the following dependencies **kwanko-base-sdk**, **kwanko-tracking-sdk** ;
* **kwanko-base-sdk** - library that contains all the core functionalities and functionalities to display banner, overly, native, parallax ads has as dependence kwanko-tracking-base;
* **kwanko-tracking-sdk** - library specialized in tracking;
* **kwanko-tracking-base-sdk** - core functionalities used in both **kwanko-base-sdk** and **kwanko-tracking-sdk**;
* **kwanko-mediation-admob-adapter** - library to add functionalities for **Admob** mediation. More details about this can be found in the section for **Mediation**.

The reason the kwanko android sdk is split in the above libraries is to offer some level of functional granulation to the developers. For instance if you want to use the library only to display ads and you are not interested in the tracking part than you don't have to import the tracking library intro your project, you will only need **kwanko-base-sdk** and **kwanko-tracking-base-sdk**. However if your interested in all of the **kwanko Android SDK** functionalities, you will have to add as a dependency to your project the **kwanko-sdk** shell library.
The reason why kwanko-sdk is called shell library is because, it by itself, does not contain any code, it only contains reference to **kwanko-base-sdk** and **kwanko-tracking-sdk**. Because of this when you are importing the **kwanko-sdk** modules you will have to also import the **kwanko-base-sdk**, **kwanko-tracking-base-sdk** modules.
After you have imported the desired kwanko sdk modules into your project, in your application build.gradle you will have to add dependencies to them.

```
compile project(path: ':kwanko-sdk') //for using all kwanko sdk functionalities
```
Or if you want to use only the tracking functionalities :
```
compile project(path: ':kwanko-tracking-sdk')
```

## Sample tutorial
* Start a new project Android Studio project
* Select name, compagny domain etc, click next
* Select Phone Android 5.0 (or other)
* You can select Basic Activity and click next and finish
* edit file settings.gradle, and add :

```
include':kwanko-sdk'
project(":kwanko-sdk").projectDir = new File("../SourceCode/kwanko-sdk")
include  ':kwanko-base-sdk'
project(":kwanko-base-sdk").projectDir = new File( "../SourceCode/kwanko-base-sdk")
include ':kwanko-tracking-sdk'
project(":kwanko-tracking-sdk").projectDir = new File( "../SourceCode/kwanko-tracking-sdk")
include ':kwanko-tracking-base-sdk'
project(":kwanko-tracking-base-sdk").projectDir = new File( "../SourceCode/kwanko-tracking-base-sdk")
include ':kwanko-mediation-admob-adapter'
project(":kwanko-mediation-admob-adapter").projectDir = new File( "../SourceCode/kwanko-mediation-admob-adapter")
```
* edit file app/build.gradle add in dependencies

```
compile project(path: ':kwanko-sdk')
compile project(path: ':kwanko-mediation-admob-adapter')
```
* and clic Sync button in android
* edit app/src/main/AndroidManifest.xml, add after application node

```
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```
* edit file app/res/layout/content_main.xml and add in android.support.constraint.ConstraintLayout node : 
```
<fr.kwanko.KwankoAd
    android:id="@+id/adView"
    android:layout_width="320px"
    android:layout_height="50px" />
```

* edit class MainActivity and following imports

```
import fr.kwanko.AdRequest;
import fr.kwanko.KwankoAd;
import fr.kwanko.KwankoTracking;
import fr.kwanko.overly.OverlyAd;
import fr.kwanko.overly.OverlyAdListener;
import fr.kwanko.params.AdSizeStrategy;
import fr.kwanko.params.KwankoConversion;
import fr.kwanko.params.ParamMap;
import fr.kwanko.params.Position;
import fr.kwanko.params.TrackingParams;
```

* edit class MainActivity and add field

```
private KwankoAd kwankoAd;
```

* add in onCreate method

```
kwankoAd = KwankoAd.class.cast(findViewById(R.id.adView));
ParamMap params = new TrackingParams.Builder()
        .setPosition(Position.TOP)
        .setAdHeight(100)
        .setAdWidth(200)
        .setAdSizeStrategy(AdSizeStrategy.PIXEL)
        .build();
kwankoAd.load(new AdRequest.Builder()
        .trackingParams(params)
        .slotId("your slot id")
        .refreshDelay(10L)
        .build());
```
* add listeners on your MainActivity class

```
@Override
protected void onPause() {
    super.onPause();
    kwankoAd.onPause();
}

@Override
protected void onResume() {
    super.onResume();
    kwankoAd.onResume();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    kwankoAd.onDestroy();
}
```

### for Overlay
* add in MainActivity field:

```
private OverlyAd overlyAd = new OverlyAd(this);

```
* add in onCreate method : 

```
overlyAd.setListener(new OverlyAdListener() {
    @Override
    public void onOverlyAdLoaded() {
        super.onOverlyAdLoaded();
        overlyAd.show();
    }

    @Override
    public void onError(Exception e) {
        super.onError(e);
    }

    @Override
    public void onOverlyAdClosed() {
        super.onOverlyAdClosed();
    }

    @Override
    public void onOverlyAdOpen() {
        super.onOverlyAdOpen();
    }
});
```

* modify default handler button click :

```
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        ParamMap params = new TrackingParams.Builder()
                .setPosition(Position.DEFAULT)
                .setAdHeight(200)
                .setAdSizeStrategy(AdSizeStrategy.PIXEL)
                .setAdWidth(200)
                .build();
        overlyAd.load(new AdRequest.Builder()
                .trackingParams(params)
                .slotId("your slot id")
                .build());
    }
});
```

* modify listeners on your MainActivity class

```
@Override
protected void onPause() {
    super.onPause();
    kwankoAd.onPause();
    overlyAd.onPause();
}

@Override
protected void onResume() {
    super.onResume();
    kwankoAd.onResume();
    overlyAd.onResume();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    kwankoAd.onDestroy();
    overlyAd.onDestroy();
}
```

### for Tracking

* modify handler button click for send a tracking event:

```
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        ParamMap params = new TrackingParams.Builder()
                .setPosition(Position.DEFAULT)
                .setAdHeight(200)
                .setAdSizeStrategy(AdSizeStrategy.PIXEL)
                .setAdWidth(200)
                .build();
        overlyAd.load(new AdRequest.Builder()
                .trackingParams(params)
                .slotId("E119093Ef9d94b4c272a041d")
                .build());
                
        KwankoTracking
            .with(MainActivity.this)
            .notifySimpleAction(new KwankoConversion.Builder()
                    .setLabel("actionId")
                    .setConversionId("trackingId")
                    .setAlternativeId("alternativeId")
                    .setIsRepeatable(true)
                    .setMode("inapp")
                    .build());
    }
});


```


## Displaying ads


### Banner ads

In order to show  a Banner Ad into an application you will have to put the  KwankoAd  view into your layout. The view can be inserted programmatically, however a more cleaner approach is to use the view inside the layout xml files.
An example of xml usage is the following:

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.ads.kwanko.kwankoads.MainActivity">


    <fr.kwanko.KwankoAd
        android:id="@+id/adView"
        android:layout_width="match_parent"
        android:layout_height="60dp" />
</RelativeLayout>
```

Having the view declared in the xml the next step is to obtain a reference in the java code and call the load method with the tracking parameters as follows:

```
ParamMap params = new TrackingParams.Builder()
		.setPosition(Position.TOP)
		.setAdHeight(100)
		.setAdWidth(200)
		.setAdSizeStrategy(AdSizeStrategy.PIXEL)
		.build();
KwankoAd ad = KwankoAd.class.cast(findViewById(R.id.adView));
ad.load(new AdRequest.Builder()
		.trackingParams(params)
		.slotId(/*your slotId here*/)
		.refreshDelay(/*refresh delay in sec*/)
		.build());
```


### Overlay ads

Overlay Ads are ads that are showing inside an activity over the activity content, however the user can still operate will the content that is not behind the overlay ad. In order to integrate this type of ad you will need to write the following lines of code:

```
OverlyAd ad = new OverlyAd(this);
```
```
ad.setListener(new OverlyAdListener() {
    @Override
    public void onOverlyAdLoaded() {
        super.onOverlyAdLoaded();
        ad.show();
    }

    @Override
    public void onError(Exception e) {
        super.onError(e);
    }

    @Override
    public void onOverlyAdClosed() {
        super.onOverlyAdClosed();
    }

    @Override
    public void onOverlyAdOpen() {
        super.onOverlyAdOpen();
    }
});
ParamMap params = new TrackingParams.Builder()
    .setPosition(Position.BOTTOM)
    .build();
ad.load(new AdRequest.Builder()
        .trackingParams(params)
        .slotId(/*your slotId here*/)
        .build());
```


### Automatically Reopen overlay after close

You can reopen overlay by using a hander and adding onOverlyAdClosed listener :

```
private final Handler handler = new Handler();

```
```
OverlyAd ad = new OverlyAd(this);

ParamMap params = new TrackingParams.Builder()
    .setPosition(Position.BOTTOM)
    .build();
AdRequest adRequest = new AdRequest.Builder()
    .trackingParams(params)
    .slotId(/*your slotId here*/)
    .build();

ad.load(adRequest);

ad.setListener(new OverlyAdListener() {
    //...
    @Override
    public void onOverlyAdClosed() {
        super.onOverlyAdClosed();
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            ad.load(adRequest);
          }
        }, 60*1000);
    }
    //...
});

ad.load(adRequest);
```

Don't forget to clear handler on activity destroy
```
@Override
protected void onDestroy() {
  super.onDestroy();
  handler.removeCallbacksAndMessages(null);
}
```

### Mediation
The Kwanko SDK can be used to display ads from Admob via mediation process.

Currently the sdk supports the following ads types:
* Banner;
* Interstitial;

The code for mediating Admob ads resides in a special Android library model called,  kwanko-mediation-admob-adapter, so the first thing that you need to do is to add the following line to your app level build.gradle dependencies:
```
compile project(path: ':kwanko-mediation-admob-adapter')
```

Also besides integrating the special adapter library you will have to integrate the Admob SDK library to your project by adding the following line into your app level build.gradle dependencies:
```
compile 'com.google.android.gms:play-services-ads:10.2.0'
```

After you have done integrating the kwanko-mediation-admob-adapter and the admob sdk you can easily show Admob into your application using the API to show normal banner and interstitial ads.

#### Banner
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.ads.kwanko.kwankoads.MainActivity">


    <com.ads.kwanko.kwankoandroidsdk.KwankoAd
        android:id="@+id/adView"
        android:layout_width="match_parent"
        android:layout_height="175dp"
        android:layout_alignParentBottom="true"
        android:layout_centerVertical="true"
        android:layout_centerHorizontal="true" />
</RelativeLayout>
```
```
ParamMap params =  new TrackingParams.Builder().build();
KwankoAd ad = KwankoAd.class.cast(findViewById(R.id.adView));
ad.load(params);
```

#### interstitial
```
OverlyAd ad = new OverlyAd(this);
```

```
ad.setListener(new OverlyAdListener() {
    @Override
    public void onOverlyAdLoaded() {
        super.onOverlyAdLoaded();
        ad.show();
    }

    @Override
    public void onError(Exception e) {
        super.onError(e);
    }

    @Override
    public void onOverlyAdClosed() {
        super.onOverlyAdClosed();
    }

    @Override
    public void onOverlyAdOpen() {
        super.onOverlyAdOpen();
    }
});
```
```
ad.load(new AdRequest.Builder()
        .trackingParams(createTrackingParams())
        .slotId(/*your slotId here*/)
        .build());
```
```
private ParamMap createTrackingParams(){

    return new TrackingParams.Builder()
            .setPosition(Position.BOTTOM)//to show the ad at the bottom
            .build();
}
```

### Tracking parameters
For every ad requests the ads need an instance of TrackingParams. In the following table you can find a complete list of tracking parameters (params for short) that can be passed to the ad. These tracking params are then transmitted to the ad server.


The list of all custom parameters which are filled by the developer or by the SDK is:


| Params | Description | Value |  Filled by
| -------- | -------- | -------- | -------- |
| nativeAd   | his param allow to specify to ad server that you want an native ad   | string   | Filled by developer   |
| categories   |   | string  | Filled by developer    |
| categories   |   | string  | Filled by developer    |
| lat | This param represent the latitude of the user | float | Filled by SDK  |
| long | This param represent the longitude of the user | float | Filled by SDK |
| devicetype | This param represent the type of the device. (Ex: 1 => Computer, 1 =>Tablet, 3 => Mobile, 4 => Television, 5 => Other) | int | Filled by SDK |
| make | This param represent the brand of device. Ex: Samsung | string | Filled by SDK |
| language   |  This param represent the language | string  |Filled by SDK  |
| ua   |  This param represent the user agent | string  |Filled by SDK  |
| domain   |  This represents the app name | string  |Filled by SDK  |
| adWidth   |  Represent the width of the ad. Can be dp or ratio. | int  |Filled by SDK |
| adHeight   |  Represent the height of the ad. Can be dp or ratio. | int  |Filled by SDK |
| screenWidth   |  Represent the width of the screen. Always in dp | int  |Filled by SDK  |
| screenHeight   | Represent the height of the screen. Always in dp | int  |Filled by SDK  |
| adSizeStrategy   |  Represent the strategy of the size. Ex: pixel / ratio | string  |Filled by SDK  |
| format   |  This param represent the type of ad. Could have one of this 4 values: inline, overlay, parallax, native. Ex: format: "overlay" | string  |Filled by SDK  |
| adPosition   |  Represent the position of the overlay ad. Can have only 2 values: top or bottom | string  |Filled by developer  |
| customParams   |  Represent a dictionary. (Ex: {"foo" : "bar","foo2" : "bar2","foo3" : "bar3","foo4" : "bar4","foo5" : "bar5"} | dict  |Filled by developer  |
| domain   |  This represents the app name | string  |Filled by SDK  |
| antenneID | Represent an ID of a Carrier Antenna from which the mobile phone get the signal | string | Filled by SDK |
| radioType | Represents the network radio type the user is connected to at the time the SDK makes the ad request. See Radio type sheet(Server column). | int |Filled by SDK |
| wifiAccessPoints | https://developers.google.com/maps/documentation/geolocation/intro?hl=fr#wifi_access_point_object | array | Filled by SDK |
| cellTowers | https://developers.google.com/maps/documentation/geolocation/intro?hl=fr#cell_tower_object | array | Filled by SDK |
| carriers | Ex: SFR / ORANGE / BOUYGUES | string | Filled by SDK |
| homeMobileCountryCode | The mobile country(MCC) code consists of 3 decimal digits and is used in combination with mobile network code(MNC) to uniquely identify the mobile network operator. The mobile country code consists of 3 decimal digits | int | Filled by SDK |
| homeMobileNetworkCode | Mobile network code consists of 2 or 3 decimal digits. See MCC above | int | Filled by SDK |
| connectivity | Represent the network of the device. (Ex: EDGE / 2G / 3G / 4G / Wifi) | int | Filled by SDK |
| model | Represent the model of the device. (Ex: Iphone) | string | Filled by SDK |
| os | Represent the operating system. (Ex: IOS/ Android/ etc..) | string |Filled by SDK |
| osv | Represent the operating system version(Ex: 8.1) | string | Filled by SDK |
| forceGeoloc | If this is set then the SDK will get the geolocation of the user, when the website where the SDK is used not asking for geolocation. (Ex: forceGeoloc: true) | bool | Filled by SDK|

### Tracking actions
#### Tracking simple actions
In order to track a simple action, mostly install of the application, you will need to call notifySimpleAction  method from KwankoTracking object passing a KwankoConversion object as parameter. An example of usage is displayed below:
```
KwankoTracking
        .with(context)
        .notifySimpleAction(new KwankoConversion.Builder()
                .setAction(ACTION)
                .setConversionId(TRACKING_ID)
                .setAlternativeId(ALT_ID)
                .setIsRepeatable(true)
                .setMode("inapp")
                .build());
```
Also in order to receive from Google Play the referrer parameter when the application is installed you need to add the following receiver in your manifest file:
```
<receiver
    android:name="fr.kwanko.services.KwankoReferrerReceiver"
    android:exported="true"
    android:enabled="true">
    <intent-filter>
        <action android:name="com.android.vending.INSTALL_REFERRER"/>
    </intent-filter>
</receiver>
```

#### Tracking a sale action
In order to track sale action you will need to call notifySaleAction method from KwankoTracking object passing a KwankoRemarketing object as parameter. An example of usage is displayed below:
```
Map<String,String> customParams = new HashMap<>();
customParams.put("action_type","purchase");
customParams.put("product_category","shoes");
customParams.put("value","15.99");
customParams.put("product_id","222");
KwankoTracking.with(this)
        .notifySaleAction(new KwankoRemarketing.Builder()
                .setAmount("34.2f")
                .setCurrency("RON")
                .setEventId(EVENT_ID)
                .setConversionId(TRACKING_ID)
                .setMode("inapp")
                .setIsRepeatable(true)
                .setCustomParams(customParams)
                .build());
```
Both KwankoConversion  and KwankoRemarketing  are basically collection of parameters.

The complete list of parameters and their explication can be found in the following table:



| Params/method to call from builder | Description | Value | Filled by |
| -------- | -------- | -------- | -------- |
| mclic/setConversionId |{TRACKING_ID} : Is a unique ID to identify the "Tracking Object", it's like the slotUID which is the unique Id of the adSlot.Every tracking ID will be given to the developer (as SlotUID, it will be created on our frontoffice). | String | Filled by the developer |
| userId | AndroidID | String | Filled by the SDK |
| cible | {IDCIBLE} :This parameter will be fallbacked in app when the customer download and open the app. | String | Filled by the SDK |
| altid/setAlternativeId | {EMAIL} : if for example the simple action is a signup and the developer have the email | String | Filled by the developer |
| action/setLabel | {ACTION} : Will be install / register / form.... | String | Filled by the developer |
| mode | The mode which has the value = "inapp" | String | Filled by the developer |
| argann/setEventId | {EVENTID}: As we are talking about a sale action, the developer must give something unique , can be a transaction ID, customer ID  | String | Filled by the developer |
| argmon/setAmount | {AMOUNT} : Amount Without Tax of the transaction, without shipping | String| Filled by the developer |
| nacur/setCurrency | {CURRENCY} : Currency values needs to respect the Currency ISO 4217  | String | Filled by the developer |
| argmodp/setPaymentMethod | The payment method name | String| Filled by the developer |
