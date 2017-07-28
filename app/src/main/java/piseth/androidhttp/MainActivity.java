package piseth.androidhttp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://raw.github.com/square/okhttp/master/README.md")
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();

                    Log.d("ANSWER", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();



    }

    public void getExample(View v) {

        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {

                try {
                    GetExample example = new GetExample();
                    String response = example.run("https://api.github.com/users/ashokslsk");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


    }
    public void postExample(View v) throws IOException {
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                try {
                    PostExample example = new PostExample();
                    String json= "{'winCondition':'HIGH_SCORE',"
                                + "'name':'Bowling',"
                                + "'round':4,"
                                + "'lastSaved':1367702411696,"
                                + "'dateStarted':1367702378785,"
                                + "'players':["
                                + "{'name':'" + "Piseth" + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                                + "{'name':'" + "Dara" + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                                + "]}";
                    String response = example.post("http://www.roundsapp.com/post", json);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public class GetExample {
        OkHttpClient client = new OkHttpClient();
        public String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d("Body:", bodyToString(request));
                Log.d("ANSWER", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class PostExample {
        OkHttpClient client = new OkHttpClient();
        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d("Body:", bodyToString(request));
                Log.d("ANSWER", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Debug:");
        builder.setMessage(message);
        builder.show();
    }

    public void debug(String obj){
        try {
            Log.d("ANSWER:", obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String bodyToString(final Request request){
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public void postData() {
        OkHttpClient client = new OkHttpClient();
        RequestBody formboday = new FormBody.Builder().add("id","107").add("cat_id","100").build();
        Request req = new Request.Builder().post(formboday).url(getResources().getString(R.string.base_url) + "index.php/C_android/getDataFromCategoryDestination").build();

        Log.d("Request:", bodyToString(req));
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Error:", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("errors:");
                }
                final String responsebody = response.body().string();
                Log.d("ANSWER", responsebody);
            }
        });
    }
    public void getData(){
        OkHttpClient client = new OkHttpClient();
        Request req= new Request.Builder().url(getResources().getString(R.string.base_url) + "index.php/C_android/getDataFromDestination").build();
        //Request req= new Request.Builder().url("https://raw.github.com/square/okhttp/master/README.md").build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("error");
                }
                final String responsebody = response.body().string();
                Log.d("ANSWER", responsebody);
            }
        });
    }
}
