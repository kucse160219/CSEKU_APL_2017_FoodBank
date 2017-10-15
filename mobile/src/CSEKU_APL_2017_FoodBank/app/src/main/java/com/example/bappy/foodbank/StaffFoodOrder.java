package com.example.bappy.foodbank;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bappy.foodbank.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.example.bappy.foodbank.R.id.showall;

public class StaffFoodOrder extends AppCompatActivity {

    TextView txt;
    String foodorder,datet,type,name,role,res,staff_admin;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    StaffFoodAdapter staffFoodAdapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int show_all_value = 0;

    FoodOrderListAdapter foodOrderListAdapter;
    AlertDialog.Builder orderbuilder;
    AlertDialog mydialog;

    ArrayList<FoodOrderListClass> addfoodorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_food_order);
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        addfoodorder=new ArrayList<FoodOrderListClass>();

        type=getIntent().getExtras().getString("type");
        name=getIntent().getExtras().getString("username");
        res=getIntent().getExtras().getString("res");
        role=getIntent().getExtras().getString("role");
        datet=getIntent().getExtras().getString("datet");
        foodorder=getIntent().getExtras().getString("order_details");
        staff_admin=getIntent().getExtras().getString("staff_admin");
        txt=(TextView)findViewById(R.id.txtviw);
        txt.setText(name+"\n"+role+"\n"+datet);
        listView=(ListView)findViewById(R.id.foodstafflist);
        staffFoodAdapter=new StaffFoodAdapter(this,R.layout.staff_food_layout);
        listView.setAdapter(staffFoodAdapter);
        try {
            jsonObject=new JSONObject(foodorder);
            jsonArray=jsonObject.getJSONArray("Server_response");

            int count=0;
            String clientid,clientname,orderdate,ispaid,phone,deliverydate,isdelivery,price,orderfrom,staff;
            while(count<jsonArray.length())
            {
                JSONObject jo=jsonArray.getJSONObject(count);
                clientid=jo.getString("clientid");
                clientname=jo.getString("clientname");
                orderdate=jo.getString("orderdate");
                ispaid=jo.getString("ispaid");
                phone=jo.getString("phonenumber");
                deliverydate=jo.getString("deliverydate");
                isdelivery=jo.getString("isdelivery");
                price=jo.getString("price");
                orderfrom=jo.getString("orderplace");
                staff=jo.getString("staffrole");
                StaffFood staffFood=new StaffFood(clientid,clientname,orderdate,ispaid,phone,deliverydate,isdelivery,price,orderfrom,staff);
                staffFoodAdapter.add(staffFood);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_staff_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.my_profile:
                startActivity(new Intent(this, ShowProfile.class));
                return true;
            case R.id.new_restaurant:
                startActivity(new Intent(this, CreateNewRestaurant.class));
                return true;
            case R.id.edit_profile:
                Intent intent=new Intent(this, EditChangeProfile.class);
                intent.putExtra("op_type","Edit");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class StaffFoodAdapter extends ArrayAdapter {
        List list = new ArrayList();
        Context ct;
        String username, price, quantity, phone, address, deliverydate;
        int quantityy, fullprice;

        public StaffFoodAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            ct = context;
        }

        @Override
        public void add(@Nullable Object object) {
            super.add(object);
            list.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View stafffoodview;
            stafffoodview = convertView;
            final StaffFoodHolder staffFoodHolder;
            if (stafffoodview == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                stafffoodview = layoutInflater.inflate(R.layout.staff_food_layout, parent, false);
                staffFoodHolder = new StaffFoodHolder();
                staffFoodHolder.clientname = (TextView) stafffoodview.findViewById(R.id.sname);
                staffFoodHolder.orderdate = (TextView) stafffoodview.findViewById(R.id.sorderdate);
                staffFoodHolder.ispaid = (TextView) stafffoodview.findViewById(R.id.spaid);
                staffFoodHolder.phone = (TextView) stafffoodview.findViewById(R.id.snumber);
                staffFoodHolder.deliverydate = (TextView) stafffoodview.findViewById(R.id.sdeliverydate);
                staffFoodHolder.isdelivery = (TextView) stafffoodview.findViewById(R.id.sdeliverytype);
                staffFoodHolder.price = (TextView) stafffoodview.findViewById(R.id.sprice);
                staffFoodHolder.orderplace = (TextView) stafffoodview.findViewById(R.id.sorderplace);
                staffFoodHolder.staffrole = (TextView) stafffoodview.findViewById(R.id.staff);
                stafffoodview.setTag(staffFoodHolder);
            } else {
                staffFoodHolder = (StaffFoodHolder) stafffoodview.getTag();
            }

            final StaffFood staffFood = (StaffFood) this.getItem(position);
            staffFoodHolder.clientname.setText(staffFood.getClientname());
            staffFoodHolder.orderdate.setText(staffFood.getOrderdate());
            staffFoodHolder.ispaid.setText(staffFood.getIspaid());
            staffFoodHolder.phone.setText(staffFood.getPhone());
            staffFoodHolder.deliverydate.setText(staffFood.getDeliverydate());
            staffFoodHolder.isdelivery.setText(staffFood.getIsdelivery());
            staffFoodHolder.price.setText("Price: "+staffFood.getPrice());
            staffFoodHolder.orderplace.setText(staffFood.getOrderplace());
            staffFoodHolder.staffrole.setText(staffFood.getStaff());

            staffFoodHolder.orderdate.setVisibility(View.GONE);
            staffFoodHolder.ispaid.setVisibility(View.GONE);
            staffFoodHolder.phone.setVisibility(View.GONE);
            staffFoodHolder.deliverydate.setVisibility(View.GONE);
            staffFoodHolder.isdelivery.setVisibility(View.GONE);
            staffFoodHolder.price.setVisibility(View.GONE);
            staffFoodHolder.orderplace.setVisibility(View.GONE);
            stafffoodview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder paidbuilder = new AlertDialog.Builder(ct);
                    //setting the alertdialog title
                    paidbuilder.setTitle("Attention");
                    //setting the body message
                    if (staffFood.getIspaid().equals("Not Paid")) {
                        if (role.equals("Admin") && type.equals("D")) {
                            paidbuilder.setMessage("Do You Want To Make It Paid?");
                            //set state for cancelling state
                            paidbuilder.setCancelable(true);

                            //setting activity for positive state button
                            paidbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    username = staffFood.getClientname();
                                    price = staffFood.getPrice();
                                    phone = staffFood.getPhone();
                                    address = staffFood.getOrderplace();
                                    deliverydate = staffFood.getOrderdate();
                                    quantityy = Integer.parseInt(quantity);
                                    staffFoodHolder.ispaid.setText("Paid");
                                    new BackgroundTask2().execute(username, price, phone, address, deliverydate);
                                }
                            });
                            //setting activity for negative state button
                            paidbuilder.setNegativeButton("NO, Later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            //alertdialog create
                            AlertDialog mydialog = paidbuilder.create();
                            //for working the alertdialog state
                            mydialog.show();
                        } else if (role.equals("Admin") && type.equals("A") || type.equals("O"))
                            Toast.makeText(getContext(), "Clicked on " + staffFood.getClientname() + "\nyou can make paid from delivery list", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(), "Clicked on " + staffFood.getClientname() + "\nOnly admin can make it paid", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Clicked on " + staffFood.getClientname() + "\nit is paided...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            final Button show_all=(Button)stafffoodview.findViewById(showall);
            show_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(show_all_value==0) {
                        staffFoodHolder.orderdate.setVisibility(View.VISIBLE);
                        staffFoodHolder.ispaid.setVisibility(View.VISIBLE);
                        staffFoodHolder.phone.setVisibility(View.VISIBLE);
                        staffFoodHolder.deliverydate.setVisibility(View.VISIBLE);
                        staffFoodHolder.isdelivery.setVisibility(View.VISIBLE);
                        staffFoodHolder.price.setVisibility(View.VISIBLE);
                        staffFoodHolder.orderplace.setVisibility(View.VISIBLE);
                        show_all.setText("Hide All");
                        show_all_value =1;
                    }
                    else
                    {
                        staffFoodHolder.orderdate.setVisibility(View.GONE);
                        staffFoodHolder.ispaid.setVisibility(View.GONE);
                        staffFoodHolder.phone.setVisibility(View.GONE);
                        staffFoodHolder.deliverydate.setVisibility(View.GONE);
                        staffFoodHolder.isdelivery.setVisibility(View.GONE);
                        staffFoodHolder.price.setVisibility(View.GONE);
                        staffFoodHolder.orderplace.setVisibility(View.GONE);
                        show_all.setText("Show All");
                        show_all_value =0;
                    }
                }
            });

            Button foodlist=(Button)stafffoodview.findViewById(R.id.foodlist);
            foodlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String id=staffFood.getClientid();
                    new FoodOrderList().execute(id);
                }
            });
            return stafffoodview;
        }

        class StaffFoodHolder {
            TextView clientname,orderdate, ispaid, phone, deliverydate, isdelivery, price, orderplace, staffrole;
        }
    }

    public class FoodOrderList extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/FoodOrderList.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String clientid=params[0];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("clientid", "UTF-8") + "=" + URLEncoder.encode(clientid, "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String OrderList = stringBuilder.toString().trim();

                jsonObject = new JSONObject(OrderList);
                jsonArray = jsonObject.getJSONArray("Server_response");

                int count = 0;
                String foodname,quantity,price;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    foodname=jo.getString("foodname");
                    quantity = jo.getString("quantity");
                    price = jo.getString("price");
                    FoodOrderListClass foodOrderListClass=new FoodOrderListClass(foodname,quantity,price);
                    addfoodorder.add(foodOrderListClass);
                    count++;
                }
                return true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            ListView listViewOrder = new ListView(StaffFoodOrder.this);
            foodOrderListAdapter = new FoodOrderListAdapter(StaffFoodOrder.this, R.layout.food_order_list_layout, addfoodorder);
            listViewOrder.setAdapter(foodOrderListAdapter);
            //Toast.makeText(this, "cart ok", Toast.LENGTH_SHORT).show();
            orderbuilder = new AlertDialog.Builder(StaffFoodOrder.this);
            orderbuilder.setCancelable(true);
            orderbuilder.setTitle("Order List");
            if (addfoodorder.isEmpty())
                orderbuilder.setMessage("it can't read any read any item");
            else
                orderbuilder.setView(listViewOrder);

            orderbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addfoodorder.clear();
                    dialog.cancel();
                }
            });
            //alertdialog create
            mydialog = orderbuilder.create();
            //for working the alertdialog state
            mydialog.show();

        }
    }

    public class FoodOrderListClass{
        String foodname,quantity,price;

        public FoodOrderListClass(String foodname, String quantity, String price) {
            this.foodname = foodname;
            this.quantity = quantity;
            this.price = price;
        }

        public String getFoodname() {
            return foodname;
        }

        public void setFoodname(String foodname) {
            this.foodname = foodname;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public class FoodOrderListAdapter extends ArrayAdapter {
        ArrayList<FoodOrderListClass> list = new ArrayList();
        Context ct;

        public FoodOrderListAdapter(@NonNull Context context, @LayoutRes int resource,ArrayList<FoodOrderListClass> string) {
            super(context, resource);
            ct = context;
            list=string;
        }

        @Override
        public void add(@Nullable Object object) {
            super.add(object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View foodorderlistview;
            foodorderlistview = convertView;
            final FoodOrderHolder foodOrderHolder;
            if (foodorderlistview == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                foodorderlistview = layoutInflater.inflate(R.layout.food_order_list_layout, parent, false);
                foodOrderHolder = new FoodOrderHolder();
                foodOrderHolder.foodname = (TextView) foodorderlistview.findViewById(R.id.foodnameorder);
                foodOrderHolder.quantity = (TextView) foodorderlistview.findViewById(R.id.quantityorder);
                foodOrderHolder.price = (TextView) foodorderlistview.findViewById(R.id.priceorder);
                foodorderlistview.setTag(foodOrderHolder);
            } else {
                foodOrderHolder = (FoodOrderHolder) foodorderlistview.getTag();
            }

            final FoodOrderListClass foodOrderListClass = (FoodOrderListClass) this.getItem(position);
            foodOrderHolder.foodname.setText("Food Name: "+foodOrderListClass.getFoodname());
            foodOrderHolder.quantity.setText("Quantity: "+foodOrderListClass.getQuantity());
            foodOrderHolder.price.setText("Price: "+foodOrderListClass.getPrice());

            return foodorderlistview;
        }

        class FoodOrderHolder {
            TextView foodname,quantity,price;
        }
    }

        class BackgroundTask2 extends AsyncTask<String,Void,Boolean>
        {
            AlertDialog.Builder alert;
            String json_url;
            String JSON_STRING;
            String resu;

            @Override
            protected void onPreExecute() {
                alert = new AlertDialog.Builder(StaffFoodOrder.this);
                alert.setTitle("Paid Status");
                json_url="http://"+getString(R.string.ip_address)+"/FoodBank/Paid.php";
            }

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    String username = params[0];
                    String price = params[1];
                    String phone=params[2];
                    String address=params[3];
                    String deliveryrdate=params[4];
                    URL url = new URL(json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputstream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                    String postdata = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
                            + "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8")
                            + "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")
                            + "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8")
                            + "&" + URLEncoder.encode("deliveryrdate", "UTF-8") + "=" + URLEncoder.encode(deliveryrdate, "UTF-8");
                    bufferedwritter.write(postdata);
                    bufferedwritter.flush();
                    bufferedwritter.close();
                    outputstream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null) {
                        stringBuilder.append(JSON_STRING + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    resu= stringBuilder.toString().trim();
                    return true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if(result.equals("true")) {
                    alert.setMessage(res);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // new BackgroundtaskOrderdate().execute("D",name,res,role,datet);
                            Toast.makeText(StaffFoodOrder.this, "OK", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog mydialog = alert.create();
                    mydialog.show();
                }
                else
                    Toast.makeText(StaffFoodOrder.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            }
        }

    public class StaffFood {

        String clientid,clientname,orderdate,ispaid,phone,deliverydate,isdelivery,price,orderplace,staff;

        public StaffFood(String clientid,String clientname, String orderdate, String ispaid, String phone, String deliverydate, String isdelivery, String price,String orderplace,String dateti) {
            this.clientid=clientid;
            this.clientname = clientname;
            this.orderdate = orderdate;
            this.ispaid = ispaid;
            this.phone = phone;
            this.deliverydate = deliverydate;
            this.isdelivery = isdelivery;
            this.price = price;
            this.orderplace=orderplace;
            this.staff=dateti;
        }

        public String getClientid() {
            return clientid;
        }

        public void setClientid(String clientid) {
            this.clientid = clientid;
        }

        public String getClientname() {
            return clientname;
        }

        public void setClientname(String clientname) {
            this.clientname = clientname;
        }

        public String getOrderdate() {
            return orderdate;
        }

        public void setOrderdate(String orderdate) {
            this.orderdate = orderdate;
        }

        public String getIspaid() {
            return ispaid;
        }

        public void setIspaid(String ispaid) {
            this.ispaid = ispaid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDeliverydate() {
            return deliverydate;
        }

        public void setDeliverydate(String deliverydate) {
            this.deliverydate = deliverydate;
        }

        public String getIsdelivery() {
            return isdelivery;
        }

        public void setIsdelivery(String isdelivery) {
            this.isdelivery = isdelivery;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOrderplace() {
            return orderplace;
        }

        public void setOrderplace(String orderplace) {
            this.orderplace = orderplace;
        }

        public String getStaff() {
            return staff;
        }

        public void setStaff(String staff) {
            this.staff = staff;
        }
    }

    @Override
    public void onBackPressed() {
        if(staff_admin.equals("2")) {
            Intent intent = new Intent(StaffFoodOrder.this, OnlyStaff.class);
            intent.putExtra("username", name);
            intent.putExtra("resname", res);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(StaffFoodOrder.this, Adminstaff.class);
            intent.putExtra("username", name);
            intent.putExtra("resname", res);
            intent.putExtra("role", role);
            startActivity(intent);
        }
        finish();
    }
}
