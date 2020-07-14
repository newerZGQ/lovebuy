package com.example.zgq.lovebuy.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.zgq.lovebuy.data.Constants;
import com.example.zgq.lovebuy.model.consum.Consum;
import com.example.zgq.lovebuy.model.desire.MyConsumDesire;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 37902 on 2016/2/21.
 */
public class SyncDataTask {
    private Context context;
    private Consum consum;
    private MyConsumDesire desire;
    private int taskId;
    public static int CONSUMTASK = 1;
    public static int DESIRETASK = 2;
    public static int UPDATEDESIRE = 3;
    public static int UPDATECONSUM = 4;
    public static int DELETECONSUM = 5;

    public SyncDataTask(Context context,Consum consum) {
        this.context = context;
        this.consum = consum;
        this.taskId = CONSUMTASK;
    }
    public SyncDataTask(Context context,MyConsumDesire desire){
        this.context = context;
        this.desire = desire;
        this.taskId = DESIRETASK;
    }

    public SyncDataTask(Context context, MyConsumDesire desire, int updateDesireTask) {
        this.context = context;
        this.desire = desire;
        this.taskId = updateDesireTask;
    }
    public SyncDataTask(Context context,Consum consum,int deleteConsum){
        this.context = context;
        this.consum = consum;
        this.taskId = deleteConsum;
    }

    public void doExecute(){
        if (taskId == CONSUMTASK){
            new SyncConsumTask().execute((Void) null);
        }
        if (taskId == DESIRETASK){
            new SyncDesireTask().execute((Void) null);
        }
        if (taskId == UPDATEDESIRE){
            new UpdateDesireTask(desire).execute((Void) null);
        }
        if (taskId == DELETECONSUM){
            new DeleteConsumTask().execute((Void) null);
        }
    }

    public class SyncConsumTask extends AsyncTask<Void, Void, Boolean> {

        public SyncConsumTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            consum.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            return true;
        }
    }
    public class SyncDesireTask extends AsyncTask<Void, Void, Boolean> {

        public SyncDesireTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            desire.save(context, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            return true;
        }
    }
    public class UpdateDesireTask extends AsyncTask<Void, Void, Boolean> {
        private String id;
        private MyConsumDesire desire;
        public UpdateDesireTask(MyConsumDesire desire) {
            this.desire = desire;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BmobQuery<MyConsumDesire> query = new BmobQuery<MyConsumDesire>();
            query.addWhereEqualTo("date", desire.getDate());
            query.addWhereEqualTo("user", Constants.userName);
            query.setLimit(1);
            query.findObjects(context, new FindListener<MyConsumDesire>() {
                @Override
                public void onSuccess(List<MyConsumDesire> object) {
                    // TODO Auto-generated method stub
                    for (MyConsumDesire desire : object) {
                        id = desire.getObjectId();
                    }
                    desire.update(context, id, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                        }
                    });
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
    }
    public class DeleteConsumTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            BmobQuery<Consum> query = new BmobQuery<Consum>();
            query.addWhereEqualTo("date", consum.getDate());
            query.setLimit(1);
            query.findObjects(context, new FindListener<Consum>() {
                @Override
                public void onSuccess(List<Consum> object) {
                    String objectId = new String();
                    // TODO Auto-generated method stub
                    for (Consum consum : object) {
                        objectId = consum.getObjectId();
                    }
                    Consum gameScore = new Consum();
                    gameScore.setObjectId(objectId);
                    gameScore.delete(context, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            Log.i("bmob","删除成功");
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            new SaveObjectToSP(context, consum, SaveObjectToSP.CONSUMSAVETASK).execute((Void) null);
                        }
                    });
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                }
            });
            return true;
        }
    }
    public class SyncSharedPreferenceTask extends AsyncTask<Void, Void, Boolean> {

        public SyncSharedPreferenceTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            desire.save(context, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            return true;
        }
    }

}
