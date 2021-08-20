  package com.sseung.akasoschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;

public class FireBaseFunction {

    static FirebaseDatabase db = FirebaseDatabase.getInstance();
    static DatabaseReference dbReference = db.getReference();

    static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageReference = storage.getReference();

    static HashMap<String, Schedule_Item> map = new HashMap<>();

    static boolean value = true;

    public static void createData() {
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//
//        // Add a new document with a generated ID
//        firestore.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("tlqkf", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("tlqkf", "Error adding document", e);
//                    }
//                });

        Schedule_Item item = new Schedule_Item(2021, 5, 28, "방송", "예능", "TBS 世界ふしぎ発見！", "20:00", "", "", "", "");
        firestore.collection("schedule").document("1").set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    public static void readImage(Context context, ImageView view, String image){
//        StorageReference ref = storage.getReference().child("schedule").child("image.jpg");

        StorageReference ref = storage.getReference();

        ref.child("schedule/" + image + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(view);
            }
        }).addOnCanceledListener(new OnCanceledListener(){

            @Override
            public void onCanceled() {
                Log.d("tlqkf", "fail");
            }
        });

        Log.d("tlqkf", "ref : " + ref);

//        Glide.with(context).load(ref).into(view);
    }

    public static void readData(){
        Log.d("tlqkf", "call readData");

        firestore.collection("schedule").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                map.putAll(UseFunction.loadDatabase());
                Log.d("tlqkf", "map size : " + map.size());

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("tlqkf", document.getId() + " => " + document.getData());

                    int number = Integer.parseInt((String) document.getData().get("number"));
                    int year = Integer.parseInt((String) document.getData().get("year"));
                    int month = Integer.parseInt((String) document.getData().get("month"));
                    int day = Integer.parseInt((String) document.getData().get("day"));
                    String division = (String) document.getData().get("division");
                    String detail = (String) document.getData().get("detail");
                    String name = (String) document.getData().get("name");
                    String time = (String) document.getData().get("time");
                    String image = (String) document.getData().get("image");
                    String uri = (String) document.getData().get("uri");
                    String sale = (String) document.getData().get("sale");
                    String source = (String) document.getData().get("source");

                    Log.d("tlqkf", "number : " + number);

                    if (map.get(Integer.toString(number)) == null) {

                        Log.d("tlqkf", "hashmap = null");

                        Schedule_Item item = new Schedule_Item(year, month, day, division, detail, name, time, image, uri, sale, source);
                        UseFunction.addData(number, item);

                        if (UseFunction.loadDayCount(item) == 1) {
                            Log.d("tlqkf2", "추가하기1");
                            setAlarm(item, number);
                        }
                    } else {
                        Schedule_Item item = map.get(Integer.toString(number));

                        Log.d("tlqkf", number + ", " + item.getYear() + ", " + item.getMonth() + ", " + item.getDay() + ", " + item.getDivision()
                                + ", " + item.getDetail() + ", " + item.getName() + ", " + item.getTime() + ", " + item.getImage() + ", " + item.getUri()
                                + ", " + item.getSale() + ", " + item.getSource());

                        if (year != item.getYear() || month != item.getMonth() || day != item.getDay()
                            || !division.equals(item.getDivision()) || !detail.equals(item.getDetail())
                            || !name.equals(item.getName()) || !time.equals(item.getTime()) || !image.equals(item.getImage())
                            || !uri.equals(item.getUri()) || !sale.equals(item.getSale()) || !source.equals(item.getSource())) {

                            Schedule_Item temp = new Schedule_Item(year, month, day, division, detail, name, time, image, uri, sale, source);
                            UseFunction.updateData(number, temp);

                            if (UseFunction.loadDayCount(temp) == 1) {
                                Log.d("tlqkf2", "추가하기2");
                                setAlarm(temp, number);
                            }
                        }


                    }
                }
            }
        });

        value = false;
    }

    public static void readUploadData(){
        Log.d("tlqkf", "call readUploadData");

        firestore.collection("upload").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("tlqkf", document.getId() + " => " + document.getData());

                    int number = Integer.parseInt(String.valueOf(document.getData().get("number")));
                    int upload_year = Integer.parseInt(String.valueOf(document.getData().get("uploadYY")));
                    int upload_month = Integer.parseInt(String.valueOf(document.getData().get("uploadMM")));
                    int upload_day = Integer.parseInt(String.valueOf(document.getData().get("uploadDD")));
                    int year = Integer.parseInt(String.valueOf(document.getData().get("year")));
                    int month = Integer.parseInt(String.valueOf(document.getData().get("month")));
                    int day = Integer.parseInt(String.valueOf(document.getData().get("day")));
                    String name = (String) document.getData().get("name");
                    String image = (String) document.getData().get("image");
                    String state = (String) document.getData().get("state");

                    Upload_Item item = new Upload_Item(number, upload_year, upload_month, upload_day, year, month, day, name, image, state);
                    UseFunction.addUploadList(number, item);

                }
            }
        });
    }

    public static void uploadData(Upload_Item item){

        firestore.collection("upload").document(Integer.toString(item.getNumber())).set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("tlqkf", "업로드 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tlqkf", "업로드 실패 : " + e);
                    }
                });
    }

    public static void uploadImage(Uri uri, String name){

        StorageReference imgRef= storage.getReference("upload/" + name);

        UploadTask uploadTask =imgRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("tlqkf", "이미지 업로드 성공");
            }
        });
    }

    public static void setAlarm(Schedule_Item item, int number){
        int year = item.getYear();
        int month = item.getMonth() - 1;
        int day = item.getDay();

        Calendar now = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal1.set(year,month, day, 00, 01);

        if (now.getTime().compareTo(cal1.getTime()) > 0) {
            return;
        }

        Log.d("tlqkf2", cal1.get(Calendar.YEAR) + ", " + cal1.get(Calendar.MONTH) + ", " + cal1.get(Calendar.DAY_OF_MONTH));
        AlarmManager alarmManager = (AlarmManager) UseFunction.mainContext.getSystemService(ALARM_SERVICE);
        Intent receiverIntent = new Intent(UseFunction.mainContext, BroadCast.class);

        long time = cal1.getTimeInMillis();
        receiverIntent.putExtra("time", time);
        PendingIntent pending = PendingIntent.getBroadcast(UseFunction.mainContext, number, receiverIntent, 0);
        alarmManager.set(AlarmManager.RTC, time, pending);
    }
}
