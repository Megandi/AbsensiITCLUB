package com.example.mgd.absensiitclub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import domain.Absensi;

public class MainActivity extends AppCompatActivity {

    Absensi absensi = new Absensi();
    DBAdapter dbAdapter = null;

    EditText txtnama;
    String keterangan ="'";
    String kett = "";
    ListView listdata;
    Button btnSimpan;
    Absensi editSiswa;

    private static final String OPTION[] = {"Edit", "Delete"};


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_1:
                if (checked)
                    keterangan = "Hadir";
                break;
            case R.id.radio_2:
                if (checked)
                    keterangan = "Sakit";
                break;
            case R.id.radio_3:
                if (checked)
                    keterangan = "Izin";
                break;
            case R.id.radio_4:
                if (checked)
                    keterangan = "Alfa";
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(getApplicationContext());
        btnSimpan = (Button) findViewById(R.id.btnsimpan);
        txtnama = (EditText) findViewById(R.id.txtnama);
        listdata = (ListView) findViewById(R.id.listdata);

        listdata.setOnItemClickListener(new ListItemClick());
        listdata.setAdapter(new ListDataAdapter(dbAdapter.getAllData()));
    }

    public class ListItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Absensi absensi = (Absensi) listdata.getItemAtPosition(position);
            showOptionDialog(absensi);
        }
    }

    public void showOptionDialog(Absensi absensi){
        final Absensi mAbsensi;
        mAbsensi = absensi;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose One")
                .setItems(OPTION, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int post) {
                        RadioGroup rb1 = (RadioGroup)findViewById(R.id.rgroup);
                        RadioButton rbu1 =(RadioButton)findViewById(R.id.radio_1);
                        RadioButton rbu2 =(RadioButton)findViewById(R.id.radio_2);
                        RadioButton rbu3 =(RadioButton)findViewById(R.id.radio_3);
                        RadioButton rbu4 =(RadioButton)findViewById(R.id.radio_4);

                        switch (post){
                            case 0:
                                editSiswa = mAbsensi;
                                txtnama.setText(mAbsensi.getNama());
                                kett = mAbsensi.getKeterangan().toString();
                                switch (kett){
                                    case "Alfa":
                                        rbu4.setChecked(true);
                                        rbu2.setChecked(false);
                                        rbu3.setChecked(false);
                                        rbu1.setChecked(false);
                                        break;
                                    case "Izin":
                                        rbu3.setChecked(true);
                                        rbu2.setChecked(false);
                                        rbu1.setChecked(false);
                                        rbu4.setChecked(false);
                                        break;
                                    case "Sakit":
                                        rbu2.setChecked(true);
                                        rbu1.setChecked(false);
                                        rbu3.setChecked(false);
                                        rbu4.setChecked(false);
                                        break;
                                    case "Hadir":
                                        rbu1.setChecked(true);
                                        rbu2.setChecked(false);
                                        rbu3.setChecked(false);
                                        rbu4.setChecked(false);
                                        break;
                                    default:
                                        break;
                                }
                                btnSimpan.setText("Edit");
                                break;
                            case 1:
                                dbAdapter.delete(mAbsensi);
                                listdata.setAdapter(new ListDataAdapter(dbAdapter.getAllData()));
                                break;

                            default:
                                break;
                        }
                    }
                });
        final Dialog d = builder.create();
        d.show();

    }

    public void save(View V){
        RadioGroup rb1 = (RadioGroup)findViewById(R.id.rgroup);
        RadioButton rbu1 =(RadioButton)findViewById(R.id.radio_1);

        if(txtnama.getText().length() ==0){
            txtnama.setError("Cannot Empty");

        } else {
            if (btnSimpan.getText().equals("Edit")){
                editSiswa.setNama(txtnama.getText().toString());
                editSiswa.setKeterangan(keterangan);
                dbAdapter.updateSiswa(editSiswa);
                btnSimpan.setText("Simpan");
            } else {
                absensi.setNama(txtnama.getText().toString());
                absensi.setKeterangan(keterangan);
                dbAdapter.save(absensi);
            }
            txtnama.setText("");
            rb1.clearCheck();

        }
        listdata.setAdapter(new ListDataAdapter(dbAdapter.getAllData()));
    }

    public class ListDataAdapter extends BaseAdapter {
        private List<Absensi> listabsensis;

        public ListDataAdapter (List<Absensi> listabsensis){
            this.listabsensis = listabsensis;
        }
        @Override
        public int getCount() {
            return this.listabsensis.size();
        }

        @Override
        public Object getItem(int position) {
            return this.listabsensis.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater
                        .from(getApplicationContext())
                        .inflate(R.layout.list_layout, parent, false);
            }
            final Absensi absensi = (Absensi) getItem(position);
            if (absensi != null){
                TextView labelNama = (TextView) convertView
                        .findViewById(R.id.LabelNama);
                labelNama.setText(absensi.getNama());
                TextView labelket = (TextView) convertView.findViewById(R.id.LabelKet);
                labelket.setText(absensi.getKeterangan());
            }

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
