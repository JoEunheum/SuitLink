package com.example.suitlink;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AddressActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "AddressActivity";

    // 우체국 오픈api 인증키
    private String key = "6416b6f0570947ff51561181270637";

    private TextView addressEdit;
    private Button searchBtn;
    private ListView addressListView;

    private ArrayAdapter<String> addressListAdapter;

    // 사용자가 입력한 주소
    private String putAddress;
    // 우체국으로부터 반환 받은 우편주소 리스트
    private ArrayList<String> addressSearchResultArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_test);

        addressEdit = (EditText)findViewById(R.id.addressedit);
        searchBtn = (Button)findViewById(R.id.btnsearch);
        addressListView = (ListView)findViewById(R.id.addresslist);

        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress(addressEdit.getText().toString());
            }
        });
    }

    public void getAddress(String kAddress)
    {
        putAddress = kAddress;
        new GetAddressDataTask().execute();
//       try{
//           StringBuffer sb = new StringBuffer(3);
//           sb.append("http://biz.epost.go.kr/KpostPortal/openapi");
//           sb.append("?regkey=" + key + "&target=postNew&query=");
//           sb.append(URLEncoder.encode(putAddress, "EUC-KR"));
//           String query = sb.toString();
//           URL url = new URL(query);
//           HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//           urlConn.setRequestMethod("POST");
//           urlConn.setRequestProperty("accept-language", "ko");
//
//
//       }catch (MalformedURLException e){
//           e.printStackTrace();
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
    }

    private class GetAddressDataTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            ArrayList<String> addressInfo = new ArrayList<>();

            HttpURLConnection conn = null;
            try
            {
                StringBuffer sb = new StringBuffer(3);
                sb.append("http://biz.epost.go.kr/KpostPortal/openapi");
                sb.append("?regkey=" + key + "&target=postNew&query=");
                sb.append(URLEncoder.encode(putAddress, "EUC-KR"));
                String query = sb.toString();

                URL url = new URL(query);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("accept-language", "ko");
                conn.setRequestMethod("POST");
                conn.connect();

                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                byte[] bytes = new byte[4096];
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (true)
                {
                    int read = in.read(bytes);
                    if (read < 0)
                        break;
                    baos.write(bytes, 0, read);
                }
                String xmlData = baos.toString("utf-8");
                baos.close();
                in.close();
                conn.disconnect();

                Document doc = docBuilder.parse(new InputSource(new StringReader(xmlData)));
                Element el = (Element) doc.getElementsByTagName("itemlist").item(0);
                for (int i = 0; i < (el).getChildNodes().getLength(); i++)
                {
                    Node node = (el).getChildNodes().item(i);
                    if (!node.getNodeName().equals("item"))
                    {
                        continue;
                    }
                    String address = node.getChildNodes().item(1).getFirstChild().getNodeValue();
                    String post = node.getChildNodes().item(3).getFirstChild().getNodeValue();
                    Log.w("jaeha", "address = " + address);
                    addressInfo.add(address + "\n우편번호:" + post.substring(0, 3) + "-" + post.substring(3));
                }
                addressSearchResultArr = addressInfo;
                publishProgress();
            } catch (Exception e){
                Log.d("doInBackground()", "e : " + e);
            } finally{
                try{
                    if (conn != null)
                        conn.disconnect();
                } catch (Exception e){
                    Log.e(TAG, "doInBackground: ",e );
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);

            String[] addressStrArray = new String[addressSearchResultArr.size()];
            addressStrArray = addressSearchResultArr.toArray(addressStrArray);

            addressListAdapter = new ArrayAdapter<String>(AddressActivity.this, android.R.layout.simple_list_item_1, addressStrArray);
            addressListView.setAdapter(addressListAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressListView.setOnItemClickListener(AddressActivity.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String arr = addressSearchResultArr.get(position);
        Log.d("테스트",arr); // 14950\n우편번호:경기도- 시흥시 은행로65번길 4-1 (은행동, 타원에스라이프APT) // 이거닷
        String cut=arr.substring(11);
        Log.d("자르는 테스트",cut);
        Intent result = new Intent();
        result.putExtra("address",cut);
        setResult(500, result);
        finish();
    }
}